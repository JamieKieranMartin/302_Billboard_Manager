package common.router;

import common.router.response.Status;

import java.io.Serializable;

/**
 * This class is the Response object.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 */
public class Response implements Serializable {
    /**
     * The variables of the object billboard.
     */
    public Status status;
    public String message;
    public Boolean error;
    public Object body;

    /**
     * Constructor for the Response object.
     *
     * @param status An enum that determines if the request was successful.
     */
    public Response(Status status) {
        this.status = status;
        isError(status);
    }

    /**
     * Constructor for the Response object.
     *
     * @param status An enum that determines if the request was successful.
     * @param message The message given.
     */
    public Response(Status status, String message) {
        this.status = status;
        isError(status);
        this.message = message;
    }

    /**
     * Constructor for the Response object.
     *
     * @param status An enum that determines if the request was successful.
     * @param body The body Object of the return.
     */
    public Response(Status status, Object body) {
        this.status = status;
        isError(status);
        this.body = body;
    }

    /**
     * Constructor for the Response object.
     *
     * @param status An enum that determines if the request was successful.
     * @param message The message given.
     * @param body This acts as the result of the request.
     */
    public Response(Status status, String message, Object body) {
        this.status = status;
        isError(status);
        this.message = message;
        this.body = body;
    }

    /**
     * Check if the IAction is an error or not.
     *
     * @param status The status of the result.
     */
    public void isError(Status status) {
        switch (status) {
            case SUCCESS:
                this.error = false;
                break;
            default:
                this.error = true;
        }
    }
}
