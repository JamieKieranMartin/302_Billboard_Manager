package common.router.response;

import common.router.Response;

/**
 * An IActionResult for an unsupported type result.
 *
 * @author Jamie Martin
 */
public class UnsupportedType extends Response {
    /**
     * A constructor with a class based response for the client.
     *
     * @param classType The class type for the body of the response.
     */
    public <T> UnsupportedType(Class<T> classType) { super(Status.UNSUPPORTED_TYPE, "Unsupported Type. Must be" + classType.getSimpleName()); }
}
