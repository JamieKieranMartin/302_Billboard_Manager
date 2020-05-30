package client.services;

import common.utils.session.Session;

/**
 * This class is responsible for the backend sessions service for the client/viewer.
 *
 * @author Jamie Martin
 */
public class SessionService {
    private Session session;

    /**
     * Initialise new session service.
     */
    protected SessionService() {}

    /**
     * Static singleton holder for session service
     */
    private static class SessionServiceHolder {
        private final static SessionService INSTANCE = new SessionService();
    }

    /**
     * Get the session service instance.
     * @return The session service instance from the singleton.
     */
    public static Session getInstance() { return SessionServiceHolder.INSTANCE.session; }

    /**
     * Set the session service instance.
     *
     * @param session the session service for the instance.
     */
    public static void setInstance(Session session) { SessionServiceHolder.INSTANCE.session = session; }
}
