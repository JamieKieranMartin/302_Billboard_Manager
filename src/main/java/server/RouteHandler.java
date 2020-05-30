package server;

import common.models.Permissions;
import common.router.*;
import common.router.response.*;
import common.router.Action;
import common.utils.session.Session;
import common.router.response.InternalError;
import server.services.TokenService;
import common.sql.CollectionFactory;

import java.util.List;
import java.util.Optional;

public class RouteHandler {

    /**
     * Routes a given request and returns the result.
     * All exceptions inside the actions are handled here and returned to the client.
     *
     * @param r The Request class.
     * @return IActionResult The result from performing the given actions.
     */
    public static Response execute(Request r, List<Class<? extends Action>> actions) throws Exception {
        // If there's no actions return NotFound.
        if (actions == null) return new NotFound("No path requests were specified.");

        // Ensure the client can't inject permissions or session information
        r.permissions = null;
        r.session = null;

        if (r.token != null) {
            Optional<Session> session = TokenService.getInstance().getSessionByToken(r.token);
            if (session.isEmpty()) return new BadRequest("Invalid token, session not found.");

            Optional<Permissions> perms = CollectionFactory.getInstance(Permissions.class).get(p -> p.username.equals(session.get().username)).stream().findFirst();
            if (perms.isEmpty()) return new BadRequest("No permissions found for your user. Contact an administrator.");

            r.session = session.get();
            r.permissions = perms.get();
        }

        // Initialise the return object
        Response result = null;

        // Loop through actions
        for (var action : actions) {
            // if an action is null then we can't perform anything, return NotFound.
            if (action == null) return new NotFound("A path request was null.");

            // Try and execute the action
            try {
                // Execute the action
                result = action.getDeclaredConstructor().newInstance().execute(r);

                // When Actions are chained, if they don't return a success the loop stops and returns the given result.
                if (result.status != Status.SUCCESS) {
                    break;
                }
            } catch (Exception e) {
                // If an internal error occurs display it on the server console with contextual information and a stacktrace.
                System.out.println("User from " + r.ip + " has requested " + r.path + " which failed internally.");
                System.out.println("User requested with the parameters: " + r.params + ".");
                System.out.println("User requested with the token: " + r.token + ".");
                System.out.println("User requested with the body: " + r.body + ".");
                //System.out.println("User requested with server injected permissions: " + r.permissions + ".");
                //System.out.println("User requested with server injected session data: " + r.session + ".");
                System.out.println("The request error stacktrace will now print: ");
                e.printStackTrace();

                // Return Internal Error with generic message for the client.
                return new InternalError("Internal System Error Occurred. Contact system administrator, if issue persists.");
            }
        }

        // Return result when the actions are completed, it will only ever return the result of the final action.
        return result;
    }
}
