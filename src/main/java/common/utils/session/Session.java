package common.utils.session;

import common.models.Permissions;
import common.utils.RandomFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class consists of the Session object.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 */
public class Session implements Serializable {
    /**
     * The user session token as a string.
     */
    public String token;

    /**
     * The user session id as an integer.
     */
    public int userId;

    /**
     * The user session username as a string.
     */
    public String username;

    /**
     * The user session permissions as a permission.
     */
    public Permissions permissions;

    /**
     * The user session expiry as a date time.
     */
    public LocalDateTime expireTime;

    /**
     * A constructor that creates the user session object.
     *
     * @param userId The users ID tied to the session.
     * @param username The users username tied to the session.
     * @param permissions The users permissions tied to the session.
     */
    public Session(int userId, String username, Permissions permissions) {
        this.token = RandomFactory.token();
        this.userId = userId;
        this.username = username;
        this.permissions = permissions;
        this.expireTime = LocalDateTime.now().plusHours(24);
    }
}
