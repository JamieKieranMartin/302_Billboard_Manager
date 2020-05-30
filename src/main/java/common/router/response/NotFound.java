package common.router.response;

import common.router.Response;

/**
 * An IActionResult for a not found result.
 *
 * @author Jamie Martin
 */
public class NotFound extends Response {

    /**
     * A constructor with the message in the body that gets returned to the client.
     *
     * @param message The body of the response.
     */
    public NotFound(String message) {
        super(Status.NOT_FOUND, message);
    }
}
