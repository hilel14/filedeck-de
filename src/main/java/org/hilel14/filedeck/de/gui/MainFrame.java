package org.hilel14.filedeck.de.gui;

import java.awt.event.ItemEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.hilel14.filedeck.de.Config;
import org.hilel14.filedeck.de.DataTool;
import org.hilel14.filedeck.de.JobsManager;

/**
 * *
 * @author hilel14
 */
public class MainFrame extends javax.swing.JFrame {

    static final Logger LOGGER = java.util.logging.Logger.getLogger(MainFrame.class.getName());
    static Preferences preferences = Preferences.userNodeForPackage(MainFrame.class);
    final Config config;
    final JobsManager jobsManager;

    /**
     * Creates new form MainFrame
     *
     * @param config
     */
    public MainFrame(Config config) {
        this.config = config;
        this.jobsManager = new JobsManager(config);
        initComponents();
        setBoundsFromPrefs();
        try {
            initControlPanel();
            fillJobsTable();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    "Unable to start FileDeck",
                    "FileDeck sartup error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initControlPanel() throws IOException, SQLException {
        DataTool dataTool = new DataTool(config.getDataSource());
        fillUsersCombo(dataTool);
        fillStatusCombo(dataTool);
    }

    private void fillUsersCombo(DataTool dataTool) throws SQLException {
        String[] users = dataTool.getAllUsers();
        userComboBox.setModel(new DefaultComboBoxModel(users));
        userComboBox.setSelectedItem(preferences.get("MainFrame.userName", "admin"));
    }

    private void fillStatusCombo(DataTool dataTool) throws SQLException {
        String[] statusCodes = jobsManager.getAllStatusCodes();
        jobStatusComboBox.setModel(new DefaultComboBoxModel(statusCodes));
        jobStatusComboBox.setSelectedItem(preferences.get("MainFrame.jobStatus", "any"));
    }

    private void fillJobsTable() {
        String user = userComboBox.getSelectedItem().toString();
        String status = jobStatusComboBox.getSelectedItem().toString();
        try {
            jobsTable.setModel(new JobsTableModel(user, status, config));
            updateControls();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    "Unable to get jobs from database",
                    "FileDeck operation error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateControls() {
        int selectedRow = jobsTable.getSelectedRow();
        String jobStatus = "unknown";
        if (selectedRow >= 0) {
            int col = jobsTable.getColumn("Job").getModelIndex();
            Object paperCode = jobsTable.getValueAt(selectedRow, col);
            infoLabel.setText(String.format("Selected job: %1$s", paperCode.toString()));
            col = jobsTable.getColumn("Status").getModelIndex();
            jobStatus = jobsTable.getValueAt(selectedRow, col).toString();
        } else {
            infoLabel.setText(String.format(
                    "There are %1$s jobs for %2$s in %3$s status at %4$s",
                    jobsTable.getModel().getRowCount(),
                    userComboBox.getSelectedItem(),
                    jobStatusComboBox.getSelectedItem(),
                    new Date()
            ));
        }
        deleteJobButton.setEnabled(selectedRow >= 0);
        moveToQaButton.setEnabled(selectedRow >= 0 && jobStatus.equals("dev"));
        moveToDevButton.setEnabled(selectedRow >= 0 && jobStatus.equals("qa"));
        copyToEvoButton.setEnabled(selectedRow >= 0 && jobStatus.equals("qa"));
        releaseVersionButton.setEnabled(selectedRow >= 0 && jobStatus.equals("qa"));
    }

    private void setBoundsFromPrefs() {
        // frame size and location
        int x = preferences.getInt("MainFrame.left", 10);
        int y = preferences.getInt("MainFrame.top", 25);
        int w = preferences.getInt("MainFrame.width", 600);
        int h = preferences.getInt("MainFrame.height", 300);
        setBounds(x, y, w, h);
    }

    private void storePreferences() {
        // frame size and location
        preferences.putInt("MainFrame.left", getBounds().x);
        preferences.putInt("MainFrame.top", getBounds().y);
        preferences.putInt("MainFrame.width", getBounds().width);
        preferences.putInt("MainFrame.height", getBounds().height);
        // user and status
        preferences.put("MainFrame.userName", userComboBox.getSelectedItem().toString());
        preferences.put("MainFrame.jobStatus", jobStatusComboBox.getSelectedItem().toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ControlPanel = new javax.swing.JPanel();
        userLabel = new javax.swing.JLabel();
        userComboBox = new javax.swing.JComboBox<>();
        jobStatusLabel = new javax.swing.JLabel();
        jobStatusComboBox = new javax.swing.JComboBox<>();
        refreshButton = new javax.swing.JButton();
        moveToQaButton = new javax.swing.JButton();
        moveToDevButton = new javax.swing.JButton();
        copyToEvoButton = new javax.swing.JButton();
        releaseVersionButton = new javax.swing.JButton();
        deleteJobButton = new javax.swing.JButton();
        contentScrollPane = new javax.swing.JScrollPane();
        jobsTable = new javax.swing.JTable();
        StatusPanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        toolsMenu = new javax.swing.JMenu();
        newJobMenuItem = new javax.swing.JMenuItem();
        newUserMenuItem = new javax.swing.JMenuItem();
        settingsMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FileDeck Desktop Edition");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        userLabel.setText("Designer");
        ControlPanel.add(userLabel);

        userComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3" }));
        userComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                userComboBoxItemStateChanged(evt);
            }
        });
        ControlPanel.add(userComboBox);

        jobStatusLabel.setText("Status");
        ControlPanel.add(jobStatusLabel);

        jobStatusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3" }));
        jobStatusComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jobStatusComboBoxItemStateChanged(evt);
            }
        });
        ControlPanel.add(jobStatusComboBox);

        refreshButton.setText("Refresh");
        refreshButton.setToolTipText("Refresh jobs table");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        ControlPanel.add(refreshButton);

        moveToQaButton.setText("QA");
        moveToQaButton.setToolTipText("Move job to QA");
        moveToQaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveToQaButtonActionPerformed(evt);
            }
        });
        ControlPanel.add(moveToQaButton);

        moveToDevButton.setText("Dev");
        moveToDevButton.setToolTipText("Return job to Dev");
        moveToDevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveToDevButtonActionPerformed(evt);
            }
        });
        ControlPanel.add(moveToDevButton);

        copyToEvoButton.setText("Evo");
        copyToEvoButton.setToolTipText("Copy press folder to Evo");
        copyToEvoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyToEvoButtonActionPerformed(evt);
            }
        });
        ControlPanel.add(copyToEvoButton);

        releaseVersionButton.setText("Release");
        releaseVersionButton.setToolTipText("Release job version");
        releaseVersionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseVersionButtonActionPerformed(evt);
            }
        });
        ControlPanel.add(releaseVersionButton);

        deleteJobButton.setForeground(java.awt.Color.red);
        deleteJobButton.setText("Delete!");
        deleteJobButton.setToolTipText("Delete job");
        deleteJobButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJobButtonActionPerformed(evt);
            }
        });
        ControlPanel.add(deleteJobButton);

        getContentPane().add(ControlPanel, java.awt.BorderLayout.NORTH);

        contentScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        jobsTable.setAutoCreateRowSorter(true);
        jobsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", ""},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Job", "Status", "Start", "User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jobsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobsTableMouseClicked(evt);
            }
        });
        jobsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jobsTableKeyReleased(evt);
            }
        });
        contentScrollPane.setViewportView(jobsTable);

        getContentPane().add(contentScrollPane, java.awt.BorderLayout.CENTER);

        infoLabel.setText("There are %1$s jobs for %2$s in status %3$s at %4$s");
        StatusPanel.add(infoLabel);

        getContentPane().add(StatusPanel, java.awt.BorderLayout.SOUTH);

        toolsMenu.setText("Tools");

        newJobMenuItem.setText("New job...");
        newJobMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newJobMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(newJobMenuItem);

        newUserMenuItem.setText("New user...");
        newUserMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(newUserMenuItem);

        settingsMenuItem.setText("Settings...");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(settingsMenuItem);

        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        storePreferences();
    }//GEN-LAST:event_formWindowClosing

    private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsMenuItemActionPerformed
        SettingsDialog settingsDialog = new SettingsDialog(this, config);
        settingsDialog.setVisible(true);
    }//GEN-LAST:event_settingsMenuItemActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        fillJobsTable();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void jobsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobsTableMouseClicked
        if (evt.getClickCount() == 2) {
            //openJobFolder();
            int selectedRow = jobsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int col = jobsTable.getColumn("Job").getModelIndex();
                String paperCode = jobsTable.getValueAt(selectedRow, col).toString();
                col = jobsTable.getColumn("Status").getModelIndex();
                String jobStatus = jobsTable.getValueAt(selectedRow, col).toString();
                try {
                    jobsManager.openJobFolder(paperCode, jobStatus);
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,
                            "Unable to to open job folder",
                            "FileDeck operation error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        updateControls();
    }//GEN-LAST:event_jobsTableMouseClicked

    private void jobsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jobsTableKeyReleased
        updateControls();
    }//GEN-LAST:event_jobsTableKeyReleased

    private void moveToQaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToQaButtonActionPerformed
        int row = jobsTable.getSelectedRow();
        int col = jobsTable.getColumn("Job").getModelIndex();
        String paperCode = jobsTable.getValueAt(row, col).toString();
        try {
            jobsManager.moveJobToQa(paperCode);
            fillJobsTable();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    "Error when trying to move job " + paperCode + " to QA",
                    "FileDeck operation error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_moveToQaButtonActionPerformed

    private void moveToDevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToDevButtonActionPerformed
        int row = jobsTable.getSelectedRow();
        int col = jobsTable.getColumn("Job").getModelIndex();
        String paperCode = jobsTable.getValueAt(row, col).toString();
        try {
            jobsManager.moveJobToDev(paperCode);
            fillJobsTable();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    "Error when trying to move job " + paperCode + " to Dev",
                    "FileDeck operation error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_moveToDevButtonActionPerformed

    private void userComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_userComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            fillJobsTable();
        }
    }//GEN-LAST:event_userComboBoxItemStateChanged

    private void jobStatusComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jobStatusComboBoxItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            fillJobsTable();
        }
    }//GEN-LAST:event_jobStatusComboBoxItemStateChanged

    private void deleteJobButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteJobButtonActionPerformed
        int row = jobsTable.getSelectedRow();
        int col = jobsTable.getColumn("Job").getModelIndex();
        String paperCode = jobsTable.getValueAt(row, col).toString();
        col = jobsTable.getColumn("Status").getModelIndex();
        String jobStatus = jobsTable.getValueAt(row, col).toString();
        int opt = JOptionPane.showConfirmDialog(
                this,
                "Realy delete job " + paperCode + "?",
                "FileDeck warning!",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                jobsManager.deleteJob(paperCode, jobStatus);
                fillJobsTable();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                        "Error when trying to delete job " + paperCode + " to Dev",
                        "FileDeck operation error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_deleteJobButtonActionPerformed

    private void copyToEvoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyToEvoButtonActionPerformed
        int row = jobsTable.getSelectedRow();
        int col = jobsTable.getColumn("Job").getModelIndex();
        String paperCode = jobsTable.getValueAt(row, col).toString();
        try {
            jobsManager.copyToEvo(paperCode);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_copyToEvoButtonActionPerformed

    private void releaseVersionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseVersionButtonActionPerformed
        int row = jobsTable.getSelectedRow();
        int col = jobsTable.getColumn("Job").getModelIndex();
        String paperCode = jobsTable.getValueAt(row, col).toString();
        try {
            jobsManager.releaseVersion(paperCode);
            fillJobsTable();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    "Error when trying to release job " + paperCode,
                    "FileDeck operation error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_releaseVersionButtonActionPerformed

    private void newJobMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newJobMenuItemActionPerformed
        String user = userComboBox.getSelectedItem().toString();
        if (user.equals("all")) {
            JOptionPane.showMessageDialog(null,
                    "Select some user to add new job",
                    "FileDeck information",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            CreateJobDialog dialog = new CreateJobDialog(this, user, config);
            dialog.setVisible(true);
            fillJobsTable();
        }
    }//GEN-LAST:event_newJobMenuItemActionPerformed

    private void newUserMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserMenuItemActionPerformed
        String user = JOptionPane.showInputDialog("Type new user name");
        if (user != null) {
            try {
                DataTool dataTool = new DataTool(config.getDataSource());
                dataTool.addUser(user);
                fillUsersCombo(dataTool);
                userComboBox.setSelectedItem(user);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                        "Error when trying to add user " + user,
                        "FileDeck operation error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_newUserMenuItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JScrollPane contentScrollPane;
    private javax.swing.JButton copyToEvoButton;
    private javax.swing.JButton deleteJobButton;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JComboBox<String> jobStatusComboBox;
    private javax.swing.JLabel jobStatusLabel;
    private javax.swing.JTable jobsTable;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton moveToDevButton;
    private javax.swing.JButton moveToQaButton;
    private javax.swing.JMenuItem newJobMenuItem;
    private javax.swing.JMenuItem newUserMenuItem;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton releaseVersionButton;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JComboBox<String> userComboBox;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
}
