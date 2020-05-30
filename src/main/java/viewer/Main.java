package viewer;

import common.models.Billboard;
import common.router.Response;
import common.router.response.Status;
import common.utils.ClientSocketFactory;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * This class consists of the Billboard Viewer handler.
 * All methods that manage and create the GUI are present in this file.
 *
 * @author Trevor Waturuocha
 * @author Jamie Martin
 */
public class Main {
    /**
     * Create the Billboard Viewer GUI and show it.
     *
     * @param billboard The billboard to inject for the View Selected on Control Panel
     * @throws Exception
     */
    public static void createAndShowGUI(Billboard billboard) throws Exception {
        // If billboard is selected, add it to viewer
        if (billboard != null) {
            new Frame(new Panel(billboard), false);
        }

        // Otherwise display the most recently selected billboard
        else {
            new Thread(new Runnable() {
                Frame frame = new Frame(new Panel(Main.getCurrent()), true);

                @Override
                public void run() {
                    while(true) {
                        try {

                            Thread.sleep(14500);
                            Frame temp = new Frame(new Panel(Main.getCurrent()), true);
                            Thread.sleep(500);

                            frame.dispose();

                            frame = temp;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * Retrieves the most recently selected billboard
     *
     * @return The currently scheduled billboard from the server
     */
    public static Billboard getCurrent() {
        Billboard billboard = new Billboard();

        Response res = new ClientSocketFactory("/schedule/get/current", null, null).setMessageOnError(false).Connect();

        // Check if there is an active billboard
        if (res != null && res.status == Status.SUCCESS && res.body instanceof Billboard) {
            billboard = (Billboard)res.body;
        } else {
            billboard.message = "No billboard scheduled";
            billboard.information = new SimpleDateFormat("K:mm a z").format(Date.from(Instant.now()));
        }

        return billboard;
    }

    /**
     * Main class to run Viewer GUI Application
     */
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
