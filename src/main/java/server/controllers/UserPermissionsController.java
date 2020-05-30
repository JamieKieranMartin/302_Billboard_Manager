package server.controllers;

import common.models.Permissions;
import common.models.User;
import common.models.UserPermissions;
import common.router.*;
import common.router.response.BadRequest;
import common.router.Response;
import common.router.response.Ok;
import common.router.response.UnsupportedType;
import common.utils.session.HashingFactory;
import common.router.Action;
import common.sql.CollectionFactory;

import java.util.List;

import static common.utils.session.HashingFactory.*;

/**
 * This class acts as the controller with all the Actions related to the user permissions.
 *
 * @author Jamie Martin
 * @author Hieu Nghia Huynh
 * @author Perdana Bailey
 */
public class UserPermissionsController {

    /**
     * This Action is the Insert Action for the users and permissions.
     */
    public static class Insert extends Action {
        // Generic Insert action constructor
        public Insert() { }

        // Override the execute to run the insert function of the user and permissions collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Ensure the body is of type userpermissions.
            if (!(req.body instanceof UserPermissions)) return new UnsupportedType(UserPermissions.class);

            // Ensure its not null.
            UserPermissions userPermissions = (UserPermissions)req.body;
            if (userPermissions.user == null || userPermissions.permissions == null) return new UnsupportedType(UserPermissions.class);
            if(userPermissions.user.username.length() <1) {return new BadRequest("Username can not be empty.");}
            // Fetch the user and return if exists.
            User user = userPermissions.user;
            List<User> userList = CollectionFactory.getInstance(User.class).get(
                userName -> user.username.equals(String.valueOf(userName.username)));
            if (!userList.isEmpty()) return new BadRequest("User already exists.");


            Permissions permissions = userPermissions.permissions;
            // Hash the password supplied and set the respective user objects for database insertion.
            byte[] salt = HashingFactory.getSalt();
            byte[] password = hashAndSaltPassword(user.password, salt);
            user.salt = encodeHex(salt);
            user.password = encodeHex(password);

            // Attempt to insert the user into the database then insert the permissions, return a success IActionResult.
            CollectionFactory.getInstance(User.class).insert(user);
            permissions.username = user.username;
            CollectionFactory.getInstance(Permissions.class).insert(permissions);

            return new Ok();
        }
    }

}
