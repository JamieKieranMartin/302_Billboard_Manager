package client.components.table;

import common.utils.Picture;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * This class makes the Java Swing picture for the client.
 *
 * @author Jamie Martin
 */
public class PictureEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    Picture currentPicture;
    JButton button;
    JFileChooser fileChooser;
    JDialog dialog;
    protected static final String EDIT = "edit";

    /**
     * Constructor for the PictureEditor that generates the picture editor dialog as an object to be used in frames.
     */
    public PictureEditor() {
        // prep the editor, a button.
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        // Prep the file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "BMP", "JPEG", "PNG"));
        dialog = new JDialog((Window) null);
    }

    /**
     * Handles events from the editor button and from the dialog's OK button.
     *
     * @param e Pass through the action event to manage the respective action.
     */
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {

            int returnVal = fileChooser.showOpenDialog(dialog);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    currentPicture.data = Base64.getEncoder().encodeToString(fileContent);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Failed to load the file: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }

            // prompts rerender
            fireEditingStopped();
        }
    }

    /**
     * Required function from AbstractCellEditor that PictureEditor extends from.
     *
     * @return Object that represents the current selected picture.
     */
    public Object getCellEditorValue() {
        return currentPicture;
    }

    /**
     * The function runs when a picture is selected. Used to update the current picture to the
     * selected picture for the PictureRenderer to display.
     *
     * @param table The table where the editor button resides.
     * @param value The selected picture.
     * @param isSelected Determines if the cell has been selected.
     * @param row The row the picture was selected from.
     * @param column The column the picture was selected from.
     * @return The button for the cell after updating the new picture.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentPicture = (Picture) value;
        return button;
    }
}
