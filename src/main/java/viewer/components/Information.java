package viewer.components;

import common.models.Billboard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class consists of the information methods for the Billboard Viewer GUI.
 * It sets up all information related contents to be displayed in the Viewer.
 *
 * @author Trevor Waturuocha
 */
public class Information extends JLabel {

    /**
     * Information class constructor. Takes billboard object and container to draw in as parameters.
     *
     * @param billboard The billboard being viewed.
     * @param wFactor The width factor for the frame.
     * @param hFactor The height factor for the frame.
     */
    public Information(Billboard billboard, int wFactor, int hFactor) {
        setText(billboard.information);
        FormatInfo(wFactor, hFactor);

        // Check if information string has an information colour attribute to add colour
        if(billboard.informationColor != null){
            setForeground(Color.decode(billboard.informationColor));
        }
    }

    /**
     * Method to format the information text to the correct size.
     *
     * @param wFactor The width factor for the frame.
     * @param hFactor The height factor for the frame.
     */
    public void FormatInfo(int wFactor, int hFactor){
        // Setting information text dimensions
        int labelWidth = Toolkit.getDefaultToolkit().getScreenSize().width * 3 / wFactor;
        int labelHeight = Toolkit.getDefaultToolkit().getScreenSize().height / hFactor;
        setMinimumSize(new Dimension(labelWidth,labelHeight));
        setMaximumSize(new Dimension(labelWidth,labelHeight));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setHorizontalAlignment(CENTER);
        String labelText = getText();

        // Formatting information with font size, div centering and text wrapping using HTML string formatting
        setText("<html><div style=\"text-align:center; font-size:40;\">" + labelText + "</div></html>"); // Updating label with formatted information text
    }
}
