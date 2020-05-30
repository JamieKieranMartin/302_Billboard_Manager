package server.middleware;

import common.models.Billboard;
import common.models.User;
import common.router.*;
import common.router.response.*;
import common.sql.CollectionFactory;

import java.util.Optional;

/**
 * This class handles permission checks to ensure the server fulfill the request if the user has the correct permissions.
 *
 * @author Jamie Martin
 * @author Perdana Bailey
 * @author Hieu Nghia Huynh
 */
public class Permission {

    /**
     * This is an Action class that ensures the user can create billboards.
     */
    public static class canCreateBillboard extends Action {
        // Generic canCreateBillboard Constructor.
        public canCreateBillboard() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (!req.permissions.canCreateBillboard) return new Unauthorised("Cannot Create Billboards.");

            return new Ok();
        }
    }

    /**
     * This is an Action class that ensures the user can edit billboards.
     */
    public static class canEditBillboard extends Action {
        public canEditBillboard() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (!req.permissions.canEditBillboard) {
                if (!(req.body instanceof Billboard)) return new UnsupportedType(Billboard.class);
                if (((Billboard) req.body).locked) return new BadRequest("Can't change a scheduled billboard.");
                Billboard billboard = (Billboard) req.body;

                if (billboard.userId != req.session.userId)
                    return new Unauthorised("Not authorised to edit billboards.");
            }

            return new Ok();
        }
    }

    /**
     * This is an Action class that ensures the user can edit billboards.
     */
    public static class canDeleteBillboard extends Action {
        public canDeleteBillboard() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (!req.permissions.canEditBillboard) {
                Optional<Billboard> billboard = CollectionFactory.getInstance(Billboard.class).get(
                    b -> req.params.get("bName").equals(b.name)).stream().findFirst();
                if (billboard.get().locked) return new BadRequest("Can't change a scheduled billboard.");

                if (billboard.isEmpty()) return new BadRequest("Billboard does not exist.");

                if (billboard.get().userId != req.session.userId)
                    return new Unauthorised("Not authorised to edit billboards.");
            }

            return new Ok();
        }
    }

    /**
     * This is an Action class that ensures the user has self permissions on billboards.
     */
    public static class canChangePassword extends Action {
        public canChangePassword() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (req.permissions.canEditUser) return new Ok();
            else {
                if (!req.session.username.equals(req.params.get("username")))
                    return new Unauthorised("Not authorised to edit password.");
            }
            return new Ok();
        }
    }

    /**
     * This is an Action class that ensures the user can create users.
     */
    public static class canEditUser extends Action {
        public canEditUser() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (!req.permissions.canEditUser) return new Unauthorised("Not authorised to edit user. ");
            return new Ok();
        }
    }

    /**
     * This is an Action class that ensures the user can create users.
     */
    public static class canViewPermission extends Action {
        public canViewPermission() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (req.permissions.canEditUser) return new Ok();
            else {
                if (req.params == null) return new BadRequest("Parameters required");

                String username = req.params.get("username");

                if (username == null || username.length() < 1) return new UnsupportedType(String.class);

                Optional<User> user = CollectionFactory.getInstance(User.class).get(u -> (username).equals(u.username)).stream().findFirst();
                
                if (user.isEmpty()) return new BadRequest("User does not exist.");
                
                if (user.get().username.equals(req.session.username)) return new Ok();
            }
            return new Unauthorised(" Not authorised to view  other user's permissions.");
        }
    }

    /**
     * This is an Action class that ensures the user can create users.
     */
    public static class canDeleteUser extends Action {
        public canDeleteUser() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (!req.permissions.canEditUser) return new Unauthorised("Not authorised to delete user. ");
            else {
                if (req.params == null) {return new UnsupportedType(String.class);}
                if (req.params.get("username") == null) {return new BadRequest("Username must not be empty.");}
                Optional<User> user = CollectionFactory.getInstance(User.class).get(u -> req.params.get("username").equals(u.username)).stream().findFirst();
                if (user.isEmpty()) return new BadRequest("User does not exist. ");

                if (user.get().username.equals(req.session.username))
                    return new Unauthorised("Not authorised to delete yourself.");
            }
            return new Ok();
        }
    }

    /**
     * This is an Action class that ensures the user can schedule billboards.
     */
    public static class canScheduleBillboard extends Action {
        public canScheduleBillboard() {
        }

        /**
         * Override the default execute function with permission check.
         *
         * @param req The user request.
         * @return IActionResult This object is for the router that returns whether they have the specified permission.
         * @throws Exception Pass through server error.
         */
        @Override
        public Response execute(Request req) throws Exception {
            if (!req.permissions.canScheduleBillboard)
                return new Unauthorised("Not authorised to schedule billboards. ");

            return new Ok();
        }
    }
}
