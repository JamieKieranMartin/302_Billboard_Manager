package common.models;

import common.utils.RandomFactory;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class consists of the user object and its associated methods.
 *
 * @author Perdana Bailey
 * @author Kevin Huynh
 * @author Jamie Martin
 */
public class User extends Object implements Serializable {
    /**
     * The variables of the object User
     */
    public int id;
    public String username;
    public String password;
    public String salt;

    /**
     * An empty constructor just for creating the object.
     */
    public User() {

    }

    /**
     * User object constructor
     *
     * @param username: users username.
     * @param password: users password hash.
     * @param salt:     users password salt.
     */
    public User(String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    /**
     * User object constructor
     *
     * @param id: users id.
     * @param username: users username.
     * @param password: users password.
     * @param salt: users salt.
     */
    public User(int id, String username, String password, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    /**
     * Change the password of the user
     *
     * @param newPass, the new password hash.
     */
    public void changePassword(String newPass) {
        this.password = newPass;
    }

    /**
     * Try login with the password for this user.
     *
     * @param pass, the password input
     */
    public boolean tryLogin(String pass) {
        return this.password.equals(pass);
    }
    
    /**
     * Generates a User object with random variables
     * @return a randomised User object
     */
    public static User Random() {
        return new User(
            RandomFactory.String(),
            RandomFactory.String(),
            RandomFactory.String()
        );
    }
}
