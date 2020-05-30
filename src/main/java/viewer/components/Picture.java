package viewer.components;

import common.models.Billboard;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.util.Base64;

/**
 * This class consists of the picture methods for the Billboard Viewer GUI.
 * It sets up all picture related contents to be displayed in the Viewer.
 *
 * @author Trevor Waturuocha
 */
public class Picture extends JLabel {
    /**
     * Picture class constructor. Takes billboard object and container to draw in as parameters.
     *
     * @param billboard The billboard being viewed.
     * @param wFactor The width factor of the frame.
     * @param hFactor The height factor of the frame.
     * @throws IOException
     */
    public Picture(Billboard billboard, int wFactor, int hFactor) throws IOException {
        byte[] base64 = Base64.getDecoder().decode(billboard.picture);
        BufferedImage pictureOutput = ImageIO.read(new ByteArrayInputStream(base64));

        // Converting image byte array to a buffered image to calculation new size
        int labelWidth = Toolkit.getDefaultToolkit().getScreenSize().width / wFactor;
        int labelHeight = Toolkit.getDefaultToolkit().getScreenSize().height * 2 / hFactor;

        setIcon(scaleImage(new ImageIcon(pictureOutput), labelWidth, labelHeight));

        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Method to calculate and resize an ImageIcon appropriately inside panel. Takes the ImageIcon and label boundaries as parameters.
     * @param icon The picture icon to be displayed
     * @param w The width of the component
     * @param h The height of the component
     * @return ImageIcon The scaled image
     */
    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        // Checking if scaling the width is needed
        if(nw > w || w > nw)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        // Checking if scaling the height is needed
        if(nh > h || h > nh)
        {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
}
