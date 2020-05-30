package client.components;

import javax.swing.*;
import java.awt.*;

/**
 * This class renders the menu for the client.
 *
 * @author Jamie Martin
 */
public class Menu extends JMenuBar {
    Color bgColor = Color.WHITE;

    /**
     * The logout button as a menu item object.
     */
    private JMenuItem logout = new JMenuItem("Logout"){
        @Override
        public Dimension getMaximumSize() {
            setBackground(bgColor);
            Dimension d1 = super.getPreferredSize();
            Dimension d2 = super.getMaximumSize();
            d2.width = d1.width;
            return d2;
        }
    };

    /**
     * The update password button.
     */
    private JMenuItem updatePassword = new JMenuItem("Update Password") {
        @Override
        public Dimension getMaximumSize() {
            setBackground(bgColor);
            Dimension d1 = super.getPreferredSize();
            Dimension d2 = super.getMaximumSize();
            d2.width = d1.width;
            return d2;
        }
    };

    /**
     * The constructor that creates a menu composed of an update button and a logout button.
     */
    public Menu() {
        add(Box.createHorizontalGlue());
        add(updatePassword);
        add(logout);
    }

    /**
     * Getter method for the logout button.
     *
     * @return The logout button as a JMenuItem.
     */
    public JMenuItem getLogout() {
        return logout;
    }

    /**
     * Getter method for the update password button.
     *
     * @return The update password button as a JMenuItem.
     */
    public JMenuItem getUpdatePassword() { return updatePassword; }

    /**
     * Called by the JMenuBar to adjust it's colour.
     *
     * @param graphics The graphic being being painted.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
