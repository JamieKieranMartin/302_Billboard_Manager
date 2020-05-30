package common.router.response;

/**
 * This enum determines the type of response.
 *
 * @author Perdana Bailey
 */
public enum Status {
    /**
     * This is the status for a malformed request. Equivalent to HTTP-400.
     */
    BAD_REQUEST,

    /**
     * This is the status for an internal server error. Equivalent to HTTP-500.
     */
    INTERNAL_SERVER_ERROR,

    /**
     * This is the status for a not found error. Equivalent to HTTP-404.
     */
    NOT_FOUND,

    /**
     * This is the status for a success. Equivalent to HTTP-200.
     */
    SUCCESS,

    /**
     * This is the status for an unauthorized error. Equivalent to HTTP-401.
     */
    UNAUTHORIZED,

    /**
     * This is the status for an unsupported type error. Equivalent to HTTP-415.
     */
    UNSUPPORTED_TYPE,
}
