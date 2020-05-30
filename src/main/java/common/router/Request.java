package common.router;

import common.models.Permissions;
import common.utils.session.Session;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class is the Request object.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 */
public class Request implements Serializable {

    /**
     * The variables of the object Request.
     */
    public String path;
    public String token;
    public String ip;
    public Session session;
    public Permissions permissions;
    public HashMap<String, String> params;
    public Object body;

    /**
     * An empty constructor just for creating the object.
     */
    public Request() {

    }

    /**
     * Constructor for the Request object.
     *
     * @param path Determines the route the clients request will take.
     * @param token A token to authenticate the clients request.
     * @param params This acts as the parameters of the request.
     * @param body This is the body, similar to a HTTP request.
     */
    public Request(String path, String token, HashMap<String, String> params, Object body) {
        this.path = path;
        this.token = token;
        this.params = params;
        this.body = body;
    }
}
