package common.router;

import common.router.response.NotFound;

/**
 * The Action Class, this is used in the Router to ensure given executions contain the necessary methods to run.
 *
 * @author Jamie Martin
 * @author Perdana Bailey
 */
public abstract class Action {

    /**
     * The executor of the Action. This is overridden when making new Action.
     *
     * @param r The Request class.
     * @return The IActionResult from performing the given actions, default NotFound.
     */
    public Response execute(Request r) throws Exception {
        return new NotFound("Path not implemented yet.");
    }
}
