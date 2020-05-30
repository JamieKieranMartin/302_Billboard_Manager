package common.models;

import common.sql.SQLITE;
import common.utils.RandomFactory;

import java.io.Serializable;

/**
 * This class consists of the user object and its associated methods.
 *
 * @author Perdana Bailey
 * @author Hieu Nghia Huynh
 * @author Jamie Martin
 */
@SQLITE
public class User extends Object implements Serializable{
    /**
     * The users ID.
     */
    @SQLITE(type="INTEGER PRIMARY KEY AUTOINCREMENT")
    public int id;

    /**
     * The users username.
     */
    @SQLITE(type="VARCHAR(255) NOT NULL UNIQUE")
    public String username;

    /**
     * The users password.
     *
     * <b>This value is only stored as a hash and temporarily to avoid a data breach.</b>
     */
    @SQLITE(type="VARCHAR(255)")
    public String password;

    /**
     * The users password salt.
     *
     * <b>This value is only stored temporarily to avoid a data breach.</b>
     */
    @SQLITE(type="VARCHAR(255)")
    public String salt;

    /**
     * An empty constructor just for creating the object.
     */
    public User() {

    }

    /**
     * User object constructor.
     *
     * @param username Users username.
     * @param password Users password hash.
     * @param salt Users password salt.
     */
    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    /**
     * User object constructor
     *
     * @param id Users id.
     * @param username Users username.
     * @param password Users password.
     * @param salt Users salt.
     */
    public User(int id, String username, String password, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    /**
     * Generates a User object with random variables.
     *
     * @return A randomised User object.
     */
    public static User Random() {
        return new User(
            RandomFactory.String(),
            RandomFactory.String(),
            RandomFactory.String()
        );
    }
}
