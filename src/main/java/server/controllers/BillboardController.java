package server.controllers;

import common.models.Billboard;
import common.models.Schedule;
import common.router.*;
import common.router.response.BadRequest;
import common.router.Response;
import common.router.response.Ok;
import common.router.response.UnsupportedType;
import common.sql.CollectionFactory;

import java.util.List;

/**
 * This class acts as the controller with all the Actions related to the billboard request path.
 *
 * @author Jamie Martin
 * @author Hieu Nghia Huynh
 * @author Perdana Bailey
 */
public class BillboardController {

    /**
     * This Action is the get all Action for the billboards.
     */
    public static class Get extends Action {
        public Get() {
        }

        // Override the execute to run the get function of the billboard collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Get list of all billboards.
            List<Billboard> billboardList = CollectionFactory.getInstance(Billboard.class).get(billboard -> true);

            // Return a success IActionResult with the list of billboards.
            return new Ok(billboardList);
        }
    }

    /**
     * This Action is the GetById Action for the billboards.
     */
    public static class GetByName extends Action {
        public GetByName() {
        }

        // Override the execute to run the get function of the billboard collection.
        @Override
        public Response execute(Request req) throws Exception {
            String name = req.params.get("name");

            // Ensure ID field is not null.
            if (name == null) {
                return new BadRequest("Must specify a billboard name.");
            }

            // Get list of billboards with the ID as specified. This should only return 1 billboard.
            List<Billboard> billboardList = CollectionFactory.getInstance(Billboard.class).get(
                billboard -> name.equals(String.valueOf(billboard.name))
            );

            // Return a success IActionResult with the list of billboards.
            return new Ok(billboardList);
        }
    }

    /**
     * This Action is the Insert Action for the billboards.
     */
    public static class Insert extends Action {
        public Insert() {
        }

        // Override the execute to run the insert function of the billboard collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Return an error on incorrect body type.
            if (!(req.body instanceof Billboard)) return new UnsupportedType(Billboard.class);

            Billboard b = (Billboard) req.body;
            if (b.name == null) return new BadRequest("Billboard name not nullable");
            if (b.name.length() <1) return new BadRequest("Billboard name must not be empty.");
            List<Billboard> billboardList = CollectionFactory.getInstance(Billboard.class).get(
                billboard -> b.name.equals(String.valueOf(billboard.name)));

            if (!billboardList.isEmpty()) return new BadRequest("Billboard name already exists.");

            // Set default values
            b.messageColor = b.messageColor == null ? "#000000" : b.messageColor;
            b.informationColor = b.informationColor == null ? "#000000" : b.informationColor;
            b.backgroundColor = b.backgroundColor == null ? "#ffffff" : b.backgroundColor;

            b.userId = req.session.userId;

            // Attempt to insert the billboard into the database then return a success IActionResult.
            CollectionFactory.getInstance(Billboard.class).insert((Billboard) req.body);
            return new Ok();
        }
    }

    /**
     * This Action is the Update Action for the billboards.
     */
    public static class Update extends Action {
        public Update() {
        }

        // Override the execute to run the update function of the billboard collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Return an error on incorrect body type.
            if (!(req.body instanceof Billboard)) return new UnsupportedType(Billboard.class);

            String bName = ((Billboard) req.body).name;
            List<Billboard> billboardList = CollectionFactory.getInstance(Billboard.class).get(
                billboard -> bName.equals(String.valueOf(billboard.name)));
            if (!billboardList.isEmpty()) {
                Billboard temp = billboardList.get(0);
                if (temp.id != ((Billboard) req.body).id) {
                    return new BadRequest("Billboard name already exists.");
                }
            }

            // Attempt to update the billboard in the database then return a success IActionResult.
            CollectionFactory.getInstance(Billboard.class).update((Billboard) req.body);
            return new Ok();
        }
    }

    /**
     * This Action is the Delete Action for the billboards.
     */
    public static class Delete extends Action {
        public Delete() {
        }

        // Override the execute to run the delete function of the billboard collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Return an error on incorrect params type.
            if (req.params == null) return new BadRequest("Parameter required: bName");

            String bName = req.params.get("bName");

            if (bName == null) return new UnsupportedType(String.class);
            if (bName.length() < 1) return new BadRequest("Billboard name must not be empty.");

            List<Billboard> bbList = CollectionFactory.getInstance(Billboard.class).get(b -> b.name.equals(bName));

            if (bbList.isEmpty()) return new BadRequest("Billboard doesn't exist");

            Billboard toDelete = bbList.get(0);

            if (toDelete.locked)
            {
                List<Schedule> scheduleList = CollectionFactory.getInstance(Schedule.class).get(schedule -> toDelete.name.equals(schedule.billboardName));

                for (var schedule: scheduleList) {
                    CollectionFactory.getInstance(Schedule.class).delete(schedule);
                }
            }

            // Attempt to delete the billboard in the database then return a success IActionResult
            CollectionFactory.getInstance(Billboard.class).delete(toDelete);
            return new Ok();
        }
    }
}

