package client.frames;

import client.Main;
import client.components.Menu;
import client.panels.PanelHandler;
import client.services.PermissionsService;
import client.services.SessionService;
import common.swing.Notification;
import common.utils.session.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class renders the Java Swing main client frame for the Client.
 *
 * @author Jamie Martin
 */
public class Client extends JFrame {

    Menu menu = new Menu();

    /**
     * The constructor that creates the main client frame.
     */
    public Client() {
        setTitle("Control Panel");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationAndSize();

        // add listener functionality for the logout button
        menu.getLogout().addActionListener(e -> {
            SessionService.setInstance(null);
            client.Main.createAndShowLogin();
            dispose();
        });

        // add update password functionality for the logout button
        menu.getUpdatePassword().addActionListener(e -> {
            Session session = SessionService.getInstance();

            // get the new password
            String result = (String)JOptionPane.showInputDialog(
                this,
                "Input a new password for user: " + session.username,
                "Edit Password",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                ""
            );

            if (result != null) {
                if (result.isEmpty()) {
                    Notification.display("Password cannot be blank");
                } else {
                    try {
                        // try update on server
                        PermissionsService.getInstance().updatePassword(session.username, result);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        setJMenuBar(menu);

        // add the panel handler
        add(new PanelHandler(), BorderLayout.CENTER);

        setVisible(true);

    }

    /**
     * Sets the location and size for the frame to be centered.
     */
    public void setLocationAndSize() {
        // Get the screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // Display the window.
        // Get the screen size
        setSize(width / 2, height / 2);
        // Get the frame size for centering
        int x = (width - getWidth()) / 2;
        int y = (height - getHeight()) / 2;
        // Set the new frame location and show GUI
        setLocation(x, y);
    }
}
