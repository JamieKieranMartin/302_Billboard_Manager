package common.swing;

import javax.swing.*;

/**
 * This class consists of the notification engine for Java Swing to handle errors.
 *
 * @author Jamie Martin
 */
public class Notification {
    /**
     * A helper function to display the notification for the java swing.
     *
     * @param message The message that wants to be displayed.
     */
    public static void display(String message) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JDialog dialog = new JDialog();
                dialog.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(dialog, message);
            }
        });
    }
}
