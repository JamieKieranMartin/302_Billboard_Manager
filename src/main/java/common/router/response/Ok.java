package common.router.response;

import common.router.Response;

/**
 * An IActionResult for a successful result.
 *
 * @author Jamie Martin
 */
public class Ok extends Response {

    /**
     * Generic Constructor which just gives the IActionResult a success parameter
     */
    public Ok() { super(Status.SUCCESS); }

    /**
     * A constructor with an object in the body that gets returned to the client.
     *
     * @param body The object that gets return to the client.
     */
    public Ok(Object body) {
        super(Status.SUCCESS, body);
    }
}
