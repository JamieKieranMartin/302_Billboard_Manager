package viewer;

import common.models.Billboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * This class consists of the main viewer frame.
 *
 * @author Trevor Waturuocha
 * @author Jamie Martin
 */
public class Frame extends JFrame {

    /**
     * Frame constructor used to add panel contents to the Viewer JFrame as well as event listeners.
     *
     * @param panel The JPanel to be rendered
     * @param hardExit Whether the Frame should hard exit (System.exit) or just dispose.
     */
    public Frame(JPanel panel, boolean hardExit) {
        setTitle("Billboard Viewer");

        // Get the screen dimensions
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Setting the frame dimensions
        final int screen_Width = dim.width;
        final int screen_Height = dim.height;
        setSize(screen_Width, screen_Height);

        // Adding key listener
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (hardExit) {
                        System.exit(0);
                    } else {
                        dispose();
                    }
                }
            }
        });

        // Adding mouse listener
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (hardExit) {
                    System.exit(0);
                } else {
                    dispose();
                }
            }
        });

        // Setting frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setLayout(new BorderLayout());
        add(panel);
        setVisible(true);
    }
}
