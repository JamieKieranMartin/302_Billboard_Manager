package common.router.response;

import common.router.Response;

/**
 * An IActionResult for a bad request result.
 *
 * @author Jamie Martin
 */
public class BadRequest extends Response {

    /**
     * A constructor with the message in the body that gets returned to the client.
     *
     * @param message The body of the response.
     */
    public BadRequest(String message) {
        super(Status.BAD_REQUEST, message);
    }
}
