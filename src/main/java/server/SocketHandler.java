package server;

import java.io.*;
import java.net.Socket;
import java.util.List;

import common.router.Request;
import common.router.response.BadRequest;
import common.router.Response;
import common.router.Action;
import server.services.RouterService;

/**
 * This class handles the how the server responds to the clients request.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 */
public class SocketHandler implements Runnable {

    // Used for getting the relevant streams and closing the socket when finished.
    private Socket client;
    /**
     * The SocketHandler Constructor.
     *
     * @param client This is the socket connection from the client.
     */
    public SocketHandler(Socket client) {
        this.client = client;
    }

    /**
     * This is the function that runs on the server as the clients thread.
     */
    public void run() {
        // Initialise the Object Input stream
        ObjectInputStream ois = null;

        // Attempt to populate the Object Input Stream with the InputStream from the client
        try {
            ois = new ObjectInputStream(this.client.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Attempt to read the object input and reply with the correct information
        try {
            Object o = ois.readObject();
            // cast the object to a request
            Request req = (Request) o;
            System.out.println("On path: " + req.path);
            // assign the IP of the client to the request
            req.ip = client.getLocalAddress().toString();

            // use the router to try and find a response
            List<Class<? extends Action>> actions = RouterService.getInstance().route(req.path);
            // execute the request for the result
            Response res = RouteHandler.execute(req, actions);

            // send the response
            replyClient(res);

        } catch (Exception e) {
            // Print an error if reading the objects fail
            e.printStackTrace();
            replyClient(new BadRequest("Invalid request."));
        }

        // Close the connection as it is no longer needed
        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is a helper function to make replying to client easier.
     *
     * @param resp This is the response that will be sent to the client.
     */
    private void replyClient(Response resp) {

        // Attempt to send the response
        try {
            // Open an object output stream based on the output stream of the client socket and write the response then flush it
            ObjectOutputStream socketOut = new ObjectOutputStream(this.client.getOutputStream());
            socketOut.writeObject(resp);
            socketOut.flush();
            System.out.println("Responded to " + this.client.getInetAddress());
        } catch (Exception e) {
            System.out.println("Failed to respond to " + this.client.getInetAddress());
        }
    }
}
