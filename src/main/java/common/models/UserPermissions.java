package common.models;

import java.io.Serializable;

/**
 * This class consists of the user and permission combined object.
 *
 * @author Perdana Bailey
 * @author Hieu Nghia Huynh
 * @author Jamie Martin
 */
public class UserPermissions implements Serializable {

    /**
     * The users user object.
     */
    public User user;

    /**
     * The users permission object.
     */
    public Permissions permissions;

    /**
     * A constructor for the user permissions object as they are tied together.
     *
     * @param user The users user object.
     * @param permissions The users permissions object.
     */
    public UserPermissions(User user, Permissions permissions) {
        this.user = user;
        this.permissions = permissions;
    }
}
