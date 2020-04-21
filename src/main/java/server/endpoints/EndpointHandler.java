package server.endpoints;

import server.database.DataService;
import server.endpoints.billboard.BillboardHandler;
import server.middleware.MiddlewareHandler;

/**
 * This class handles the multiple endpoints for the server.
 *
 * @author Perdana Bailey
 */
public class EndpointHandler {
    private DataService db;
    public BillboardHandler billboard;
    public MiddlewareHandler middlewareHandler;

    /**
     * Generates a Endpoint Handler Instance.
     *
     * @param db: This is the database connection the endpoints will use.
     */
    public EndpointHandler(DataService db, MiddlewareHandler middlewareHandler) {
        this.db = db;
        this.middlewareHandler = middlewareHandler;
        this.billboard = new BillboardHandler(this.db, this.middlewareHandler);
    }
}
