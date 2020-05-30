package client.components.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * This class renders the background colour in the table cell for the client.
 *
 * @author Jamie Martin
 */
public class ColourRenderer extends JLabel implements TableCellRenderer {

    /**
     * The ColourRenderer constructor, displays the selected colour in cell the location by default as transparent.
     */
    public ColourRenderer() {
        setOpaque(true);
    }

    /**
     * Method called by the Table to render the component.
     *
     * @param table The table that renders the colour in a cell.
     * @param colour The selected colour to render.
     * @param isSelected Determines if the cell has been selected
     * @param hasFocus Determines if the cell is hovered over.
     * @param row The row the colour is displayed from.
     * @param column The column the colour was displayed from.
     * @return This component with the new background colour.
     */
    public Component getTableCellRendererComponent(JTable table, Object colour, boolean isSelected, boolean hasFocus, int row, int column) {
        if (colour != null) setBackground((Color)colour);

        return this;
    }
}
