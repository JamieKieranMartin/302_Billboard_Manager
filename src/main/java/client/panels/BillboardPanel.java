package client.panels;

import client.components.table.ColourEditor;
import client.components.table.ColourRenderer;
import client.components.table.PictureEditor;
import client.components.table.PictureRenderer;
import client.components.table.ObjectTableModel;
import client.components.table.IObjectTableModel;
import client.services.BillboardService;
import client.services.SessionService;
import common.utils.XML;
import viewer.Main;
import common.models.Billboard;
import common.utils.Picture;
import common.utils.session.Session;
import common.swing.Notification;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Optional;

/**
 * This class renders the Java Swing billboard panel for the client.
 *
 * @author Jamie Martin
 */
public class BillboardPanel extends JPanel implements ActionListener {

    // initialise components
    IObjectTableModel<Billboard> tableModel;
    JTable table;
    Container buttonContainer = new Container();
    JButton viewButton = new JButton("View Selected"),
        createButton = new JButton("Create Billboard"),
        refreshButton = new JButton("Refresh"),
        deleteButton = new JButton("Delete Selected"),
        importButton = new JButton("Import into Selected"),
        exportButton = new JButton("Export Selected");
    String selected;

    /**
     * The Billboard Panel constructor that generates the BillboardPanel GUI.
     */
    public BillboardPanel() {
        Session session = SessionService.getInstance();
        // add action listeners for buttons
        createButton.addActionListener(this);
        viewButton.addActionListener(this);
        refreshButton.addActionListener(this);
        deleteButton.addActionListener(this);
        importButton.addActionListener(this);
        exportButton.addActionListener(this);

        // selectively enable disabled buttons
        if (!session.permissions.canCreateBillboard) {
            createButton.setEnabled(false);
        }
        viewButton.setEnabled(false);
        deleteButton.setEnabled(false);
        importButton.setEnabled(false);
        exportButton.setEnabled(false);

        // initialise the table model with billboard data
        tableModel = new ObjectTableModel(Billboard.class, BillboardService.getInstance());
        tableModel.setObjectRows(BillboardService.getInstance().refresh());
        table = new JTable(tableModel);

        setupSelection();
        setupRenderersAndEditors();

        JScrollPane pane = new JScrollPane(table);

        // add the buttons to the container
        buttonContainer.setLayout(new FlowLayout());
        buttonContainer.add(createButton);
        buttonContainer.add(viewButton);
        buttonContainer.add(deleteButton);
        buttonContainer.add(importButton);
        buttonContainer.add(exportButton);
        buttonContainer.add(refreshButton);

        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Manages the logic when a user selects cells on the table.
     */
    public void setupSelection() {
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            selected = (String)table.getModel().getValueAt(table.getSelectedRow(), 1);
            Session session = SessionService.getInstance();

            Optional<Billboard> selectedValue = tableModel.getObjectRows().stream().filter(x -> x.name.equals(selected)).findFirst();

            if (selected != null) {
                viewButton.setEnabled(true);
            } else {
                viewButton.setEnabled(false);
            }

            if (selectedValue.isPresent() && (session.permissions.canEditBillboard || session.userId == selectedValue.get().userId)) {
                if (selected != null) {
                    deleteButton.setEnabled(true);
                    importButton.setEnabled(true);
                    exportButton.setEnabled(true);
                } else {
                    deleteButton.setEnabled(false);
                    importButton.setEnabled(false);
                    exportButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * Set up the default renderers and editors for particular classes.
     */
    public void setupRenderersAndEditors() {
        table.setDefaultRenderer(Color.class, new ColourRenderer());
        table.setDefaultEditor(Color.class, new ColourEditor());
        table.setDefaultEditor(Picture.class, new PictureEditor());
        table.setDefaultRenderer(Picture.class, new PictureRenderer());
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
            String result = (String)JOptionPane.showInputDialog(
                this,
                "Input a new billboard name",
                "Create Billboard",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
            );

            if (result != null) {
                Billboard b = new Billboard();
                b.name = result;

                tableModel.setObjectRows(BillboardService.getInstance().insert(b));
                tableModel.fireTableDataChanged();
            }
        }
        // Check if view button is pressed
        if(e.getSource() == viewButton){
            SwingUtilities.invokeLater(() -> {
                try {
                   Main.createAndShowGUI(tableModel.getObjectRows().stream().filter(x -> x.name.equals(selected)).findFirst().get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        // check if the refresh button is pressed
        if (e.getSource() == refreshButton) {
            // refresh the billboard list and update the table
            tableModel.setObjectRows(BillboardService.getInstance().refresh());
            tableModel.fireTableDataChanged();
        }
        // check if the delete button is pressed
        if (e.getSource() == deleteButton) {
            // delete the currently selected billboard and update the table
            tableModel.setObjectRows(BillboardService.getInstance().delete(tableModel.getObjectRows().stream().filter(x -> x.name.equals(selected)).findFirst().get()));
            tableModel.fireTableDataChanged();
        }
        // check if the import button is pressed
        if (e.getSource() == importButton) {
            try {
                // open file picker for xml
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
                int returnVal = fileChooser.showOpenDialog(new JDialog((Window) null));

                // get file
                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    String xml = XML.readFile(fileChooser.getSelectedFile().getPath());

                    // update selected billboard with variables
                    Optional<Billboard> selectedBillboard = tableModel.getObjectRows().stream().filter(x -> x.name.equals(selected)).findFirst();

                    if (selectedBillboard.isPresent()) {
                        // convert the xml string to a billboard
                        Billboard billboard = XML.fromXML(xml, selectedBillboard.get());

                        // only if successful does it re-fire the table
                        Boolean success = BillboardService.getInstance().update(billboard);

                        if (success) {
                            tableModel.setObjectRows(BillboardService.getInstance().refresh());
                            tableModel.fireTableDataChanged();
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to load the file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        if (e.getSource() == exportButton) {
            try {
                // try get the selected billboard
                Optional<Billboard> selectedBillboard = tableModel.getObjectRows().stream().filter(x -> x.name.equals(selected)).findFirst();

                if (selectedBillboard.isPresent()) {
                    // spawn the file save dialog
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
                    int returnVal = fileChooser.showSaveDialog(new JDialog((Window) null));

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        // get the billboard
                        Billboard billboard = selectedBillboard.get();

                        // convert the billboard to an xml string
                        String xml = XML.toXML(billboard);
                        // convert the string to a document
                        Document document = XML.toDocument(xml);

                        // save the file to the selected file
                        File file = fileChooser.getSelectedFile();
                        XML.saveDocument(document, file);

                        Notification.display("Successfully exported " + billboard.name + " to " + fileChooser.getName(fileChooser.getSelectedFile()));
                    }
                }
            } catch (Exception ex) {
                Notification.display("Error exporting: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
