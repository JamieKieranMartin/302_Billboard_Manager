package server;

import common.models.*;
import common.router.Request;
import common.utils.session.HashingFactory;
import common.utils.Props;
import server.controllers.*;
import server.middleware.*;
import server.services.RouterService;
import common.sql.CollectionFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * This class is the main class, used as the entry point for the server application.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 */
public class Main {

    /**
     * This is the main function.
     *
     * @param args Command line arguments.
     * @throws Exception Automatically critical fail if an error is thrown.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Billboard Server");

        System.out.println("Attempting to connect to database...");
        initDatabase();

        System.out.println("Configuring router...");
        initRouter();

        // Fetch the port from the props file
        Properties props = Props.getProps("./network.props");
        String port = props.getProperty("server.port");
        if (port == null) {
            throw new Exception("Server Port was not specified in the network.props file!");
        }

        // Open the socket
        System.out.println("Opening Server on port " + port + "...");
        int portNum = Integer.parseInt(port);
        ServerSocket ss = new ServerSocket(portNum);
        System.out.println("Sever available at " + ss.getLocalSocketAddress());

        // Loop through constantly looking for connections
        while (true) {
            // When a connection is found accept it and create a thread for it
            Socket s = ss.accept();
            System.out.println("A request attempt has been made from " + s.getInetAddress());
            new Thread(new SocketHandler(s)).start();
        }
    }

    /**
     * This function initialises the database for the application.
     * @throws Exception Pass through the server error.
     */
    public static void initDatabase() throws Exception {
        // Connect and populate the database
        CollectionFactory.getInstance(Billboard.class);
        CollectionFactory.getInstance(User.class);
        CollectionFactory.getInstance(Schedule.class);
        CollectionFactory.getInstance(Permissions.class);

        // Insert admin user( u:admin-p:admin)
        int adminExists = CollectionFactory.getInstance(User.class).get(user -> user.username.equals("admin")).size();

        if (adminExists == 0) {
            User u = new User("admin", HashingFactory.hashPassword("admin"), null);
            Permissions p = new Permissions(u.username, true, true, true, true);
            UserPermissions up = new UserPermissions(u, p);

            new UserPermissionsController.Insert().execute(new Request(null, null, null, up));
        }
    }

    /**
     * This function initialises the router for the application.
     */
    public static void initRouter() {
        // ADD THE ROUTER
        RouterService.getInstance().SET_AUTH(Authentication.Authenticate.class)
            .ADD("/login", UserController.Login.class)
            .ADD("/logout", UserController.Logout.class)
            // Add Billboard actions to router
            .ADD_AUTH("/billboard/get", BillboardController.Get.class)
            //.ADD_AUTH("/billboard/get/lock", BillboardController.GetByLock.class)
            .ADD_AUTH("/billboard/get/name", BillboardController.GetByName.class)
            .ADD_AUTH("/billboard/insert", Permission.canCreateBillboard.class, BillboardController.Insert.class)
            .ADD_AUTH("/billboard/update", Permission.canEditBillboard.class, BillboardController.Update.class)
            .ADD_AUTH("/billboard/delete", Permission.canDeleteBillboard.class, BillboardController.Delete.class)
            // Add UserPermissions actions to router
            .ADD_AUTH("/userpermissions/insert", Permission.canEditUser.class, UserPermissionsController.Insert.class)
            // Add User actions to router
            .ADD_AUTH("/user/update/password", Permission.canChangePassword.class, UserController.UpdatePassword.class)
            .ADD_AUTH("/user/delete", Permission.canDeleteUser.class, UserController.Delete.class)
            // Add Schedule actions to router
            .ADD_AUTH("/schedule/get", Permission.canScheduleBillboard.class, ScheduleController.Get.class)
            .ADD("/schedule/get/current", ScheduleController.GetCurrent.class)
            .ADD_AUTH("/schedule/insert", Permission.canScheduleBillboard.class, ScheduleController.Insert.class)
            .ADD_AUTH("/schedule/delete", Permission.canScheduleBillboard.class, ScheduleController.Delete.class)
            //Add Permission actions to router
            .ADD_AUTH("/permission/get", Permission.canEditUser.class, PermissionController.Get.class)
            .ADD_AUTH("/permission/get/username", Permission.canViewPermission.class, PermissionController.GetByUsername.class)
            .ADD_AUTH("/permission/update", Permission.canEditUser.class, PermissionController.Update.class);
    }
}
