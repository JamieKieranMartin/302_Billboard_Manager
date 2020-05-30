package client.panels;

import client.components.table.ColourEditor;
import client.components.table.ColourRenderer;
import client.components.table.PictureEditor;
import client.components.table.PictureRenderer;
import client.components.table.ObjectTableModel;
import client.components.table.IObjectTableModel;
import client.services.PermissionsService;
import client.services.SessionService;
import common.models.*;
import common.swing.Notification;
import common.utils.session.HashingFactory;
import common.utils.session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * This class renders the Java Swing user panel for the client.
 *
 * @author Jamie Martin
 */
public class UserPanel extends JPanel implements ActionListener {

    // initialise components
    IObjectTableModel<Permissions> tableModel;
    JTable table;
    Container buttonContainer = new Container();
    JButton editButton = new JButton("Edit Password"),
        createButton = new JButton("Create User"),
        refreshButton = new JButton("Refresh"),
        deleteButton = new JButton("Delete Selected");
    String selected;

    /**
     * The User Panel constructor that generates the UserPanel GUI.
     */
    public UserPanel() {
        // add the action listeners
        createButton.addActionListener(this);
        editButton.addActionListener(this);
        refreshButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // default enable/disabled for buttons
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // initialise the table
        tableModel = new ObjectTableModel<>(Permissions.class, PermissionsService.getInstance());
        tableModel.setObjectRows(PermissionsService.getInstance().refresh());
        table = new JTable(tableModel);

        setupSelection();
        JScrollPane pane = new JScrollPane(table);

        // add the buttons to the container
        buttonContainer.setLayout(new FlowLayout());
        buttonContainer.add(createButton);
        buttonContainer.add(editButton);
        buttonContainer.add(deleteButton);
        buttonContainer.add(refreshButton);

        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Sets up the selection logic for the table.
     */
    public void setupSelection() {
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            selected = (String)table.getModel().getValueAt(table.getSelectedRow(), 1);

            if (selected != null) {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

    }

    /**
     * Adding listener events for the user panel buttons.
     *
     * @param e Pass through the action event to manage the respective action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if create button is pressed
        if(e.getSource() == createButton){
            try {
                // initialise create user components
                JTextField username = new JTextField();
                JPasswordField password = new JPasswordField();
                JCheckBox canCreateBillboard = new JCheckBox();
                JCheckBox canEditBillboard = new JCheckBox();
                JCheckBox canScheduleBillboard = new JCheckBox();
                JCheckBox canEditUser = new JCheckBox();

                // build component container
                final JComponent[] components = new JComponent[] {
                    new JLabel("Username"),
                    username,
                    new JLabel("Password"),
                    password,
                    new JLabel("Can Create Billboards"),
                    canCreateBillboard,
                    new JLabel("Can Edit Billboards"),
                    canEditBillboard,
                    new JLabel("Can Schedule Billboards"),
                    canScheduleBillboard,
                    new JLabel("Can Edit Users"),
                    canEditUser
                };

                // get the result from the inputs
                int result = JOptionPane.showConfirmDialog(this, components, "Create new User", JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    if (username.getText().isEmpty()) {
                        Notification.display("Username cannot be null. Please try again");
                    } else if (String.valueOf(password.getPassword()).isEmpty()) {
                        Notification.display("Password cannot be null. Please try again");
                    } else {
                        // convert values to User + Permission object
                        User user = new User();
                        user.username = username.getText();
                        user.password = HashingFactory.hashPassword(String.valueOf(password.getPassword()));

                        Permissions permissions = new Permissions(username.getText(), canCreateBillboard.isSelected(), canEditBillboard.isSelected(), canScheduleBillboard.isSelected(), canEditUser.isSelected());

                        // send insert to server and fire updates to table model
                        tableModel.setObjectRows(PermissionsService.getInstance().insert(new UserPermissions(user, permissions)));
                        tableModel.fireTableDataChanged();
                    }
                }
            } catch (Exception ex) {
                Notification.display(ex.getMessage());
            }
        }
        // Check if edit button is pressed
        if(e.getSource() == editButton){
            // get new password
            String result = (String)JOptionPane.showInputDialog(
                this,
                "Input a new password for user: " + selected,
                "Edit Password",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
            );

            if (result != null) {
                if (result.isEmpty()) {
                    Notification.display("Password cannot be blank");
                } else {
                    int id = tableModel.getObjectRows().stream().filter(x -> x.username.equals(selected)).findFirst().get().id;

                    try {
                        // try update the password
                        PermissionsService.getInstance().updatePassword(selected, result);
                    } catch (Exception exception) {
                        Notification.display(exception.getMessage());
                    }
                }
            }
        }
        // Check if refresh button is pressed
        if (e.getSource() == refreshButton) {
            tableModel.setObjectRows(PermissionsService.getInstance().refresh());
            tableModel.fireTableDataChanged();
        }
        // Check if delete button is pressed
        if (e.getSource() == deleteButton) {
            Session session = SessionService.getInstance();
            var permissionList = tableModel.getObjectRows();

            // handle edge cases
            if (session.username.equals(selected)) {
                Notification.display("Cannot delete yourself!");
            } else if (permissionList.size() == 1) {
                Notification.display("Cannot delete the only user!");
            } else {
                Permissions permissions = permissionList.stream().filter(x -> x.username.equals(selected)).findFirst().get();

                User u = new User();
                u.id = permissions.id;
                u.username = permissions.username;
                // send a delete to the controller service for the selected user
                tableModel.setObjectRows(PermissionsService.getInstance().delete(u));
                tableModel.fireTableDataChanged();
            }
        }
    }
}
