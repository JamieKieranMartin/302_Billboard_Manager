package client.components.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * This class renders the Java Swing picture editor for the client.
 *
 * @author Jamie Martin
 */
public class PictureRenderer extends JLabel implements TableCellRenderer {


    /**
     * The PictureRenderer constructor, displays the selected picture in cell the location by default as centered and edit picture.
     */
    public PictureRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setText("Edit Picture");
    }

    /**
     * The function runs when a picture is selected. Used to update the current picture to the
     * selected picture for the PictureRenderer to display.
     *
     * @param table The table where the editor button resides.
     * @param image The picture object being rendered.
     * @param isSelected Determines if the cell has been selected.
     * @param row The row the picture was selected from.
     * @param column The column the picture was selected from.
     * @return The button for the cell after updating the new picture.
     */
    public Component getTableCellRendererComponent(JTable table, Object image, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
