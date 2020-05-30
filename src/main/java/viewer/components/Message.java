package viewer.components;

import common.models.Billboard;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.IOException;

/**
 * This class consists of the message methods for the Billboard Viewer GUI.
 * It sets up all message related contents to be displayed in the Viewer.
 *
 * @author Trevor Waturuocha
 */

public class Message extends JLabel {
    /**
     * Message class constructor. Takes billboard object and container to draw in as parameters.
     *
     * @param billboard The billboard being viewed.
     */
    public Message(Billboard billboard) {
        setText(billboard.message);
        CalcMsgWidth(); // Formatting message for billboard width
        // Check if message string has a message colour attribute to add colour
        if(billboard.messageColor != null){
            setForeground(Color.decode(billboard.messageColor));
        }
        setAlignmentX(Component.CENTER_ALIGNMENT); // Horizontally centering message text
    }

    /**
     * Method to calculate the largest possible message to fit the screen in one line.
     */
    public void CalcMsgWidth(){
        Font labelFont = getFont();
        String labelText = getText();
        int stringWidth = getFontMetrics(labelFont).stringWidth(labelText);
        int labelWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int maxHeight = Toolkit.getDefaultToolkit().getScreenSize().height / 3;

        // Calculating largest width
        double largestWidth = (double)labelWidth / (double)stringWidth;
        int finalSize = (int)(labelFont.getSize() * largestWidth);

        // Set the final plain font size to use for the message label
        setFont(new Font(labelFont.getName(), Font.PLAIN, finalSize));

        // Resize font if it is larger than max height
        if (finalSize > maxHeight){
            setFont(new Font(labelFont.getName(), Font.PLAIN, maxHeight));
        }
    }
}
