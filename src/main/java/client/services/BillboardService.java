package client.services;

import common.models.*;
import common.router.Response;
import common.router.response.Status;
import common.utils.ClientSocketFactory;
import common.utils.session.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is responsible for the backend billboard service for the client/viewer.
 *
 * @author Jamie Martin
 */
public class BillboardService extends DataService<Billboard> {
    public List<Billboard> billboards;

    /**
     * Initialise new billboard service.
     */
    protected BillboardService() {
        this.billboards = new ArrayList<>();
    }

    /**
     * Static singleton holder for a billboard service.
     */
    private static class BillboardServiceHolder {
        private final static BillboardService INSTANCE = new BillboardService();
    }

    /**
     * Get the billboard service instance.
     *
     * @return The billboard service instance from the singleton.
     */
    public static BillboardService getInstance() { return BillboardServiceHolder.INSTANCE; }

    /**
     * Refreshes the billboard list.
     *
     * @return The new list of billboards from the server.
     */
    public List<Billboard> refresh() {
        Session session = SessionService.getInstance();

        if (session != null) {
            Response result = new ClientSocketFactory("/billboard/get", session.token, null).Connect();

            if (result != null && result.status == Status.SUCCESS && result.body instanceof List) {
                BillboardServiceHolder.INSTANCE.billboards = (List<Billboard>) result.body;
            }
        }

        return BillboardServiceHolder.INSTANCE.billboards;
    }

    /**
     * Attempts to insert the given billboard on the server.
     *
     * @param billboard The billboard that is being sent to the server.
     * @return The new list of billboards from the server.
     */
    public List<Billboard> insert(Billboard billboard) {
        Session session = SessionService.getInstance();
        Response res = new ClientSocketFactory("/billboard/insert", session.token, null, billboard).Connect();
        return refresh();
    }

    /**
     * Attempts to update the given billboard on the server.
     *
     * @param billboard The billboard that is being updated on the server.
     * @return A boolean whether the billboard was updated or not.
     */
    public Boolean update(Billboard billboard) {
        Session session = SessionService.getInstance();
        Response res = new ClientSocketFactory("/billboard/update", session.token, null, billboard).Connect();
        return !res.error;
    }

    /**
     * Attempts to delete the given billboard on the server.
     *
     * @param billboard The billboard that is being deleted from the server.
     * @return The new list of billboards from the server.
     */
    public List<Billboard> delete(Billboard billboard) {
        Session session = SessionService.getInstance();

        HashMap<String, String> params = new HashMap<>();
        params.put("bName", billboard.name);

        Response res = new ClientSocketFactory("/billboard/delete", session.token, params).Connect();
        return refresh();
    }
}
