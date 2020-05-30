package common.router.response;

import common.router.Response;

/**
 * An IActionResult for an unauthorised result.
 *
 * @author Jamie Martin
 */
public class Unauthorised extends Response {

    /**
     * A constructor with the message in the body that gets returned to the client.
     *
     * @param message The body of the response.
     */
    public Unauthorised(String message) {
        super(Status.UNAUTHORIZED, message);
    }
}
