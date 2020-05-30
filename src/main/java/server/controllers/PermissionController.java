package server.controllers;

import common.models.Permissions;
import common.models.User;
import common.router.*;
import common.router.response.BadRequest;
import common.router.Response;
import common.router.response.Ok;
import common.router.response.UnsupportedType;
import common.sql.CollectionFactory;

import java.util.List;

/**
 * This class acts as the controller with all the Actions related to the permissions request path.
 *
 * @author Jamie Martin
 * @author Hieu Nghia Huynh
 * @author Perdana Bailey
 */
public class PermissionController {

    /**
     * This Action is the get all Action for the permissions.
     */
    public static class Get extends Action {
        // Generic Get action constructor.
        public Get() { }

        // Override the execute to run the get function of the permissions collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Get list of all permissions.
            List<Permissions> permissionsList = CollectionFactory.getInstance(Permissions.class).get(x -> true);

            // Return a success IActionResult with the list of permissions.
            return new Ok(permissionsList);
        }
    }

    /**
     * This Action is the GetByUsername Action for the permissions.
     */
    public static class GetByUsername extends Action {
        // Generic GetById action constructor.
        public GetByUsername() { }

        // Override the execute to run the get function of the permissions collection.
        @Override
        public Response execute(Request req) throws Exception {
            String username = req.params.get("username");
            
            // Ensure id field is not null.
            if (username == null) {
                return new BadRequest("Must specify a username.");
            }

            List<User> userList = CollectionFactory.getInstance(User.class).get(
                aUser -> username.equals(String.valueOf(aUser.username)));
            if (userList.isEmpty()) return new BadRequest("User doesn't exist.");
            // Get list of permissions with the ID as specified. This should only return 1 permission.
            List<Permissions> permissionsList = CollectionFactory.getInstance(Permissions.class).get(
                permissions -> username.equals(String.valueOf(permissions.username))
            );

            // Return a success IActionResult with the list of permissions.
            return new Ok(permissionsList);
        }

    }

    /**
     * This Action is the Update Action for the permissions.
     */
    public static class Update extends Action {
        // Generic Update action constructor.
        public Update() { }

        // Override the execute to run the update function of the permissions collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Return an error on incorrect body type.
            if (!(req.body instanceof Permissions)) return new UnsupportedType(Permissions.class);

            if(((Permissions) req.body).username.equals(req.session.username) )
            {
                if (!((Permissions) req.body).canEditUser) return new BadRequest("Can't remove your own Edit User permission.");
            }

            // Attempt to update the permission in the database then return a success IActionResult.
            CollectionFactory.getInstance(Permissions.class).update((Permissions) req.body);
            return new Ok();
        }
    }
}
