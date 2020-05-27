package server.controllers;

import common.models.Permissions;
import common.models.Session;
import common.models.User;
import common.router.*;
import common.utils.RandomFactory;
import server.router.*;
import server.services.TokenService;
import server.sql.CollectionFactory;

import java.util.List;
import java.util.Optional;

import static common.utils.HashingFactory.encodeHex;
import static common.utils.HashingFactory.hashAndSaltPassword;

/**
 * This class acts as the controller with all the Actions related to the user request path.
 *
 * @author Jamie Martin
 * @author Kevin Huynh
 * @author Perdana Bailey
 */
public class UserController {

    /**
     * This class extends action for logging in users. It "logs" the user in and generates a token.
     */
    public static class Login extends Action {
        public Login() {
        }

        /**
         * Override the default execute function with the login of the user.
         *
         * @param req: The user request.
         * @return IActionResult: This object is for the router that returns a token or a Unauthenticated.
         * @throws Exception: Pass through the server error from the checkUserExists or tryLogin function.
         */
        @Override
        public IActionResult execute(Request req) throws Exception {
            String username = req.params.get("username");
            String password = req.params.get("password");

            // Ensure the fields are not null.
            if (username == null) {
                return new BadRequest("Parameter required: username.");
            } else if (password == null) {
                return new BadRequest("Parameter required: password.");
            }

            // Ensure the user exists.
            Optional<User> user = TokenService.getInstance().checkUserExists(username);
            Optional<Permissions> permissions = TokenService.getInstance().checkPermissionsExist(username);
            if (user.isPresent() && permissions.isPresent()) {
                // Attempt to log the user in and request for the token.
                Session ses = TokenService.getInstance().tryLogin(user.get(), permissions.get(), password);
                // Return a success IActionResult with the token.
                if (ses != null) return new Ok(ses);
            }

            // If the token is null that means the password is incorrect.
            // If the user doesn't exist tell the client it's an invalid username.
            return new BadRequest("Incorrect details.");
        }
    }

    /**
     * This class extends action for logging out the user. It "logs" the user out and removes them from the session.
     */
    public static class Logout extends Action {
        public Logout() {
        }

        /**
         * Override the default execute function with the logging out of the user.
         *
         * @param req: The user request.
         * @return IActionResult: This object is for the router that ensures a successful logout.
         * @throws Exception: Pass through the server error from the tryLogout function.
         */
        @Override
        public IActionResult execute(Request req) throws Exception {
            // Ensure the token is not null.
            if (req.token == null) return new Unauthorised("Must provide token to logout.");

            // Attempt to log the user out and return a success empty IActionResult.
            TokenService.getInstance().tryLogout(req.token);
            return new Ok();
        }
    }

    /**
     * This Action is the UpdatePassword Action the users,
     */
    public static class UpdatePassword extends Action {
        // Generic UpdatePassword action constructor.
        public UpdatePassword() {
        }

        // Override the execute to run the update function of the user collection.
        @Override
        public IActionResult execute(Request req) throws Exception {
            // Ensure the body is of type user.
            if (req.params.get("username").isEmpty() || req.params.get("password").isEmpty())
                return new BadRequest("No username or password");

            String username = req.params.get("username");
            List<User> userList = CollectionFactory.getInstance(User.class).get(user -> username.equals(String.valueOf(user.username)));

            if (userList.isEmpty()) return new BadRequest("User doesn't exist");
            User temp = userList.get(0);

            // Hash the password supplied and set the respective user objects for database insertion.
            byte[] salt = RandomFactory.String().getBytes();
            byte[] newPass = hashAndSaltPassword(req.params.get("password"), salt);
            temp.salt = encodeHex(salt);
            temp.password = encodeHex(newPass);
            CollectionFactory.getInstance(User.class).update(temp);
            return new Ok();
        }
    }

    /**
     * This Action is the Delete Action for the users.
     */
    public static class Delete extends Action {
        // Generic Delete action constructor.
        public Delete() {
        }

        // Override the execute to run the delete function of the user collection.
        @Override
        public IActionResult execute(Request req) throws Exception {
            // Ensure the body is of type user.
            if (req.params.get("username") == null) return new UnsupportedType(String.class);

            // Get the correct user
            String Username = req.params.get("username");
            List<User> deleteUser = CollectionFactory.getInstance(User.class).get(users -> Username.equals(String.valueOf(users.username)));
            if (deleteUser.isEmpty()) return new BadRequest("User not existed");
            User temp = deleteUser.get(0);

            // Get the correct permission
            List<Permissions> deletePerm = CollectionFactory.getInstance(Permissions.class).get(perm -> Username.equals(String.valueOf(perm.username)));
            if (deletePerm.isEmpty()) return new BadRequest("Permission not existed");
            Permissions perm = deletePerm.get(0);

            // Attempt to delete the user and permission in the database then return a success IActionResult.
            CollectionFactory.getInstance(Permissions.class).delete(perm);
            CollectionFactory.getInstance(User.class).delete(temp);
            return new Ok();
        }
    }
}
