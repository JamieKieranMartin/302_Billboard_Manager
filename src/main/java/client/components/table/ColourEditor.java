package client.components.table;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class turns the cell in the table to a button where a dialog box for colour selection is displayed.
 *
 * @author Jamie Martin
 */
public class ColourEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    /**
     * The current selected colour on the ColourEditor.
     */
    Color currentColour;

    /**
     * The confirmation button for the ColourEditor.
     */
    JButton button;

    /**
     * The colour picker for the ColourEditor.
     */
    JColorChooser colourPicker;

    /**
     * The dialog box that encased the ColourEditor.
     */
    JDialog dialog;

    /**
     * The dialog box that encases the ColourEditor.
     */
    protected static final String EDIT = "edit";

    /**
     * The ColourEditor constructor, generates the ColourEditor dialog as an object to be used in frames.
     */
    public ColourEditor() {
        // Prep the editor, a button.
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        // Prep the colour picker.
        colourPicker = new JColorChooser();
        dialog = JColorChooser.createDialog(button,"Pick a Color",true, colourPicker, this, null);
    }

    /**
     * Handles events from the ColourEditor button (in the cell) or the dialog's OK button.
     *
     * @param e Pass through the action event to manage the respective action.
     */
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            // The user has clicked the cell, so spawn the colour picker.
            button.setBackground(currentColour);
            colourPicker.setColor(currentColour);
            dialog.setVisible(true);

            // prompts rerender
            fireEditingStopped();

        } else {
            // user pressed OK button
            currentColour = colourPicker.getColor();
        }
    }

    /**
     * Required function from AbstractCellEditor that ColourEditor extends from.
     *
     * @return Object that represents the current selected colour.
     */
    public Object getCellEditorValue() {
        return currentColour;
    }

    /**
     * The function runs when a colour is selected. Used to update the current colour to the
     * selected colour for the ColourRenderer to display.
     *
     * @param table The table where the editor button resides.
     * @param value The selected colour.
     * @param isSelected Determines if the cell has been selected.
     * @param row The row the colour was selected from.
     * @param column The column the colour was selected from.
     * @return The button for the cell after updating the new colour.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentColour = (Color) value;
        return button;
    }
}
