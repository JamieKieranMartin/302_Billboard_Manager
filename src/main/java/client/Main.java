package client;

import client.frames.*;
import common.models.Billboard;

/**
 * This class consists of the Billboard Viewer handler.
 * All methods that manage and create the GUI are present in this file.
 *
 * @author Jamie Martin
 * @author Trevor Waturuocha
 */
public class Main {
    /**
     * Create the Login Panel GUI and show it.
     */
    public static void createAndShowLogin() {
        new Login();
    }

    /**
     * Create the Control Panel GUI and show it.
     */
    public static void createAndShowClient() {
        new Client();
    }

    /**
     * Main class to run GUI Application and socket interface.
     *
     */
    public static void main(String[] args) throws Exception {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowLogin);
    }
}
