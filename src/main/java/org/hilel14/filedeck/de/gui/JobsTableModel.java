package org.hilel14.filedeck.de.gui;

import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import org.hilel14.filedeck.de.DataTool;

/**
 *
 * @author hilel14
 */
public class JobsTableModel extends DefaultTableModel {

    final Class[] types = new Class[]{String.class, String.class, String.class, String.class};

    public JobsTableModel(String user, String status) throws SQLException {
        DataTool dataTool = new DataTool();
        Object[][] data = dataTool.getJobs(user, status);
        String[] columnNames = new String[]{"Job", "Status", "Start", "User"};
        setDataVector(data, columnNames);
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
