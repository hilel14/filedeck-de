package org.hilel14.filedeck.de;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author hilel14
 */
public class DataTool {

    static final Logger LOGGER = Logger.getLogger(DataTool.class.getName());
    private final BasicDataSource dataSource;

    public DataTool(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String[] getAllUsers() throws SQLException {
        // collect all from users table
        String qry = "SELECT user_name FROM fd5_users";
        List<String> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(qry);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("user_name"));
            }
        }
        // add zombies from jobs table
        qry = "SELECT DISTINCT user_name FROM fd5_jobs";
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(qry);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String user = rs.getString("user_name");
                if (!users.contains(user)) {
                    users.add(rs.getString("user_name"));
                }
            }
        }
        // sort users
        Collections.sort(users);
        // add special experssion to define all users
        users.add(0, "all");
        // return results as array (for DefaultComboBoxModel constructure)
        return users.toArray(new String[0]);
    }

    public String[] getAllEnvelopes() throws SQLException {
        String qry = "SELECT envelope_text FROM fd5_envelope_codes ORDER BY envelope_code ASC";
        List<String> envelopes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(qry);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                envelopes.add(rs.getString("envelope_text"));
            }
        }
        return envelopes.toArray(new String[0]);
    }

    public Object[][] getJobs(String user, String status) throws SQLException {
        if (status.equals("any")) {
            if (user.equals("all")) {
                // any status, all users
                String qry = "SELECT * FROM fd5_jobs";
                try (Connection connection = dataSource.getConnection();) {
                    PreparedStatement statement = connection.prepareStatement(qry);
                    ResultSet rs = statement.executeQuery();
                    return resultSetToArray(rs);
                }
            } else {
                // any status, specific user
                String qry = "SELECT * FROM fd5_jobs WHERE user_name = ?";
                try (Connection connection = dataSource.getConnection();) {
                    PreparedStatement statement = connection.prepareStatement(qry);
                    statement.setString(1, user);
                    ResultSet rs = statement.executeQuery();
                    return resultSetToArray(rs);
                }
            }
        } else {
            if (user.equals("all")) {
                // specific status, all users
                String qry = "SELECT * FROM fd5_jobs WHERE status_code = ?";
                try (Connection connection = dataSource.getConnection();) {
                    PreparedStatement statement = connection.prepareStatement(qry);
                    statement.setString(1, status);
                    ResultSet rs = statement.executeQuery();
                    return resultSetToArray(rs);
                }
            } else {
                // specific status and user
                String qry = "SELECT * FROM fd5_jobs WHERE status_code = ? AND user_name = ?";
                try (Connection connection = dataSource.getConnection();) {
                    PreparedStatement statement = connection.prepareStatement(qry);
                    statement.setString(1, status);
                    statement.setString(2, user);
                    ResultSet rs = statement.executeQuery();
                    return resultSetToArray(rs);
                }
            }
        }
    }

    public void addUser(String userName) throws SQLException {
        String sql = "INSERT INTO fd5_users (user_name) VALUES (?)";
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName.toLowerCase());
            statement.executeUpdate();
        }
    }

    public void createJob(String paperCode, String userName) throws SQLException {
        String sql = "INSERT INTO fd5_jobs (paper_code, user_name) VALUES (?,?)";
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, paperCode);
            statement.setString(2, userName);
            statement.executeUpdate();
        }
    }

    public void updateJobStatus(String paperCode, String status) throws SQLException {
        String qry = "UPDATE fd5_jobs SET status_code = ? WHERE paper_code = ?";
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(qry);
            statement.setString(1, status);
            statement.setString(2, paperCode);
            statement.executeUpdate();
        }
    }

    public void deleteJob(String paperCode) throws SQLException {
        String qry = "DELETE FROM fd5_jobs WHERE paper_code = ?";
        try (Connection connection = dataSource.getConnection();) {
            PreparedStatement statement = connection.prepareStatement(qry);
            statement.setString(1, paperCode);
            statement.executeUpdate();
        }
    }

    private Object[][] resultSetToArray(ResultSet rs) throws SQLException {
        List<String[]> jobs = new ArrayList<>();
        while (rs.next()) {
            String[] job = new String[4];
            job[0] = rs.getString("paper_code");
            job[1] = rs.getString("status_code");
            job[2] = rs.getString("start_time");
            job[3] = rs.getString("user_name");
            jobs.add(job);
        }
        return jobs.toArray(new Object[0][0]);
    }

}
