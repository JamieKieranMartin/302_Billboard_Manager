package client.frames;

import client.Main;
import client.services.SessionService;
import common.utils.session.Session;
import common.router.Response;
import common.router.response.Status;
import common.utils.ClientSocketFactory;
import common.utils.session.HashingFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * This class renders the Java Swing login client frame for the client.
 *
 * @author Jamie Martin
 */
public class Login extends JFrame implements ActionListener {

    // initialise components
    Container container = getContentPane();
    JLabel usernameLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField username = new JTextField(15);
    JPasswordField password = new JPasswordField(15);
    JButton login = new JButton("LOGIN");

    /**
     * The constructor that creates the login frame.
     */
    public Login() {
        setTitle("Control Panel: Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayoutManager();
        addComponentsToContainer();
        setLocationAndSize();
        // Set the listener for the login button and make it the default button for enter press
        login.addActionListener(this);
        getRootPane().setDefaultButton(login);
        setResizable(false);
        setVisible(true);
    }
    /**
     * Sets the layout manager of the frame to a GridBagLayout.
     */
    public void setLayoutManager() {
        container.setLayout(new GridBagLayout());
    }

    /**
     * Organises the components in the frame to the GridBagLayout.
     */
    public void addComponentsToContainer() {
        GridBagConstraints c = new GridBagConstraints();
        Insets i = new Insets(5, 5, 5, 5);
        c.insets = i;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        container.add(usernameLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        container.add(username, c);
        c.gridx = 0;
        c.gridy = 1;
        container.add(passwordLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        container.add(password, c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        container.add(login, c);
    }

    /**
     * Sets the size and location of the frame and it's components to the center.
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

        usernameLabel.setSize(100, 30);
        passwordLabel.setSize(100, 30);
        username.setSize(150, 30);
        password.setSize(150, 30);
        login.setSize(100, 30);
    }

    /**
     * Action for selecting the login button.
     *
     * @param e Pass through the action event to manage the respective action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        HashMap<String, String> params = new HashMap<>();

        // get the values from the components
        params.put("username", username.getText());
        params.put("password", HashingFactory.hashPassword(String.valueOf(password.getPassword())));

        // await a response from the server
        Response res = new ClientSocketFactory("/login", null, params, null).Connect();

        if (res != null && res.status == Status.SUCCESS && res.body instanceof Session) {
            // set the session and render client main menu
            SessionService.setInstance((Session) res.body);
            Main.createAndShowClient();
            dispose();
        }
    }
}
