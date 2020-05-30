package common.router.response;

import common.router.Response;

/**
 * An IActionResult for an internal server error result.
 *
 * @author Jamie Martin
 */
public class InternalError extends Response {

    /**
     * A constructor with the message in the body that gets returned to the client.
     *
     * @param message The body of the response.
     */
    public InternalError(String message) { super(Status.INTERNAL_SERVER_ERROR, message); }
}
