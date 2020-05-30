package client.services;

import common.models.*;
import common.router.Response;
import common.router.response.Status;
import common.utils.ClientSocketFactory;
import common.utils.session.HashingFactory;
import common.utils.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is responsible for the backend permissions service for the client/viewer.
 *
 * @author Jamie Martin
 */
public class PermissionsService extends DataService<Permissions> {
    private List<Permissions> permissions;

    /**
     * Initialise new permissions service.
     */
    protected PermissionsService() {
        this.permissions = new ArrayList<>();
    }

    /**
     * Static singleton holder for permissions service.
     */
    private static class PermissionsServiceHolder {
        private final static PermissionsService INSTANCE = new PermissionsService();
    }

    /**
     * Get the permissions service instance.
     *
     * @return The permissions service instance from the singleton.
     */
    public static PermissionsService getInstance() { return PermissionsServiceHolder.INSTANCE; }

    /**
     * Refreshes the permissions list.
     *
     * @return The new list of permissions from the server.
     */
    public List<Permissions> refresh() {
        Session session = SessionService.getInstance();

        if (session != null) {
            Response result = new ClientSocketFactory("/permission/get", session.token, null).Connect();

            if (result != null && result.status == Status.SUCCESS && result.body instanceof List) {
                PermissionsServiceHolder.INSTANCE.permissions = (List<Permissions>) result.body;
            }
        }

        return PermissionsServiceHolder.INSTANCE.permissions;
    }

    /**
     * Attempts to insert the given permissions on the server.
     *
     * @param userPerm The user permission that is being sent to the server.
     * @return The new list of permissions from the server.
     */
    public List<Permissions> insert(UserPermissions userPerm) {
        Session session = SessionService.getInstance();
        Response res = new ClientSocketFactory("/userpermissions/insert", session.token, null, userPerm).Connect();
        return refresh();
    }

    /**
     * Attempts to update the given permissions on the server.
     *
     * @param userPerm The user permission that is being updated on the server.
     * @return A boolean whether the user permission was updated or not.
     */
    public Boolean update(Permissions userPerm) {
        Session session = SessionService.getInstance();
        Response res = new ClientSocketFactory("/permission/update", session.token, null, userPerm).Connect();
        return !res.error;
    }

    /**
     * Attempts to delete the given user on the server.
     *
     * @param user The user that is being deleted from the server.
     * @return The new list of permissions from the server.
     */
    public List<Permissions> delete(User user) {
        Session session = SessionService.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("username", user.username);

        Response res = new ClientSocketFactory("/user/delete", session.token, params).Connect();
        return refresh();
    }

    /**
     * Attempts to update the password of the given user id on the server.
     *
     * @param username The username as a string of the user that's password is being updated.
     * @param password The password as a string of the user that's password is being updated.
     * @return The new list of permissions from the server.
     */
    public List<Permissions> updatePassword(String username, String password){
        Session session = SessionService.getInstance();
        
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", HashingFactory.hashPassword(password));

        Response res = new ClientSocketFactory("/user/update/password", session.token, params, null).Connect();
        return refresh();
    }
}
