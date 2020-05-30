package client.panels;

import client.services.SessionService;
import common.utils.session.Session;

import javax.swing.*;

/**
 * This class handles the different types of panels available for the client.
 *
 * @author Jamie Martin
 */
public class PanelHandler extends JTabbedPane {

    /**
     * The Panel constructor that generates the Panel GUI.
     */
    public PanelHandler() {
        Session session = SessionService.getInstance();

        add("Billboards", new BillboardPanel());

        // only render if the current session has canScheduleBillboard
        if (session.permissions.canScheduleBillboard) {
            add("Schedule", new SchedulePanel());
        }

        // only render if the current session has canEditUser
        if (session.permissions.canEditUser) {
            add("Users", new UserPanel());
        }
    }
}
