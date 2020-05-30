package server.controllers;

import common.models.Billboard;
import common.utils.scheduling.DayOfWeek;
import common.models.Schedule;
import common.router.*;
import common.router.response.BadRequest;
import common.router.Response;
import common.router.response.Ok;
import common.router.response.UnsupportedType;
import common.sql.CollectionFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class acts as the controller with all the Actions related to the schedule request path.
 *
 * @author Jamie Martin
 * @author Hieu Nghia Huynh
 * @author Perdana Bailey
 */
public class ScheduleController {

    /**
     * This Action is the get all Action for the schedule.
     */
    public static class Get extends Action {
        // Generic Get action constructor.
        public Get() {
        }

        // Override the execute to run the get function of the schedule collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Get list of all schedules.
            List<Schedule> scheduleList = CollectionFactory.getInstance(Schedule.class).get(x -> true);

            // Return a success IActionResult with the list of schedules.
            return new Ok(scheduleList);
        }
    }

    /**
     * This Action is the GetByID Action for the schedules.
     */
    public static class GetById extends Action {
        // Generic GetById action constructor.
        public GetById() {
        }

        // Override the execute to run the get function of the schedule collection.
        @Override
        public Response execute(Request req) throws Exception {
            String id = req.params.get("id");

            // Ensure id field is not null.
            if (id == null) {
                return new BadRequest("Must specify a schedule ID.");
            }

            // Get list of schedules with the ID as specified. This should only return 1 schedule.
            List<Schedule> scheduleList = CollectionFactory.getInstance(Schedule.class).get(
                schedule -> id.equals(String.valueOf(schedule.id))
            );

            // Return a success IActionResult with the list of schedules.
            return new Ok(scheduleList);
        }
    }

    /**
     * This Action is the Insert Action for the schedules.
     */
    public static class Insert extends Action {
        // Generic Insert action constructor.
        public Insert() {
        }

        // Override the execute to run the insert function of the schedule collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Ensure the body is of type schedule.
            if (req.body instanceof Schedule) {
                // Check if schedule exists.
                Schedule s = (Schedule) req.body;

                // Ensure the schedule data is of the correct type.
                if (s.dayOfWeek > 7 || s.dayOfWeek < 0)
                    return new BadRequest("Start must be between 0-7 as in 0 for Every day or Sun - Sat");
                if (s.interval != 0 && s.duration > s.interval) return new BadRequest("Duration cannot be longer than the interval");
                if (s.duration > 1440 || s.duration < 1) return new BadRequest("Duration must be between 1 - 1440 inclusive");
                if (s.interval > 60 || s.interval < 0) return new BadRequest("Interval must be between 0 - 60 inclusive");

                // Make sure the billboard exists.
                String sName = s.billboardName;
                List<Billboard> billboardList = CollectionFactory.getInstance(Billboard.class).get(
                    billboard -> sName.equals(String.valueOf(billboard.name)));
                if (billboardList.isEmpty()) return new BadRequest("Billboard doesn't exists.");

                // Attempt to insert the schedule into the database then return a success IActionResult.
                CollectionFactory.getInstance(Schedule.class).insert((Schedule) req.body);
                Billboard bb = billboardList.get(0);
                bb.locked = true;
                CollectionFactory.getInstance(Billboard.class).update(bb);
                return new Ok();
            }

            // Return an error on incorrect body type.
            return new UnsupportedType(Schedule.class);
        }
    }

    /**
     * This Action is the Delete Action for the schedules.
     */
    public static class Delete extends Action {
        // Generic Delete action constructor.
        public Delete() {
        }

        // Override the execute to run the delete function of the schedule collection.
        @Override
        public Response execute(Request req) throws Exception {
            // Ensure the body is of type schedule.
            if (req.body instanceof Schedule) {
                Schedule schedule = (Schedule) req.body;
                int id = schedule.id;

                List<Schedule> scheduleList = CollectionFactory.getInstance(Schedule.class).get(s -> id == s.id);

                if (scheduleList.isEmpty()) return new BadRequest("Schedule doesn't exist.");

                // Attempt to delete the schedule in the database then return a success IActionResult.
                CollectionFactory.getInstance(Schedule.class).delete((Schedule) req.body);

                // if this is the only schedule assigned to that billboard, remove lock
                if (scheduleList.size() == 1) {
                    List<Billboard> billboardList = CollectionFactory.getInstance(Billboard.class).get(billboard -> billboard.name.equals(schedule.billboardName));

                    if (!billboardList.isEmpty()) {
                        Billboard billboard = billboardList.get(0);

                        billboard.locked = false;

                        CollectionFactory.getInstance(Billboard.class).update(billboard);
                    }
                }

                return new Ok();
            }

            // Return an error on incorrect body type.
            return new UnsupportedType(Schedule.class);
        }
    }

    /**
     * This Action is the GetCurrent Action for the schedules.
     */
    public static class GetCurrent extends Action {
        // Generic GetById action constructor.
        public GetCurrent() {
        }

        // Override the execute to run the get function of the schedule collection
        @Override
        public Response execute(Request req) throws Exception {
            // Get list of all schedules.
            String day = Instant.now().atZone(ZoneId.systemDefault()).getDayOfWeek().name();
            // Get the list of schedules which the day is the same.
            DayOfWeek today = DayOfWeek.valueOf(day);
            List<Schedule> todaySchedule = CollectionFactory.getInstance(Schedule.class).get(s -> s.dayOfWeek == 0 || today.ordinal() == s.dayOfWeek);

            // Get the list of schedules which should be shown now.
            int totalMinutes = Instant.now().atZone(ZoneOffset.systemDefault()).getHour() * 60 + Instant.now().atZone(ZoneOffset.UTC).getMinute();
            List<Schedule> scheduleList = new ArrayList<>();
            for (Schedule schedule : todaySchedule) {
                int scheduleInterval = schedule.dayOfWeek == 0 ? (24 * 60) : schedule.interval;
                int check = (totalMinutes - schedule.start) % scheduleInterval;

                if (check < schedule.duration && check >= 0) {
                    scheduleList.add(schedule);
                }
            }

            // If no billboard is scheduled, return an OK.
            if (scheduleList.isEmpty()) return new Ok();

            // Get the newest inserted billboard.
            scheduleList.sort(Comparator.comparing(s -> s.createTime));

            // Get the newest billboard and return it.
            Schedule resultSchedule = scheduleList.get(scheduleList.size() - 1);
            List<Billboard> billboardList = CollectionFactory.getInstance(Billboard.class).get(
                billboard -> resultSchedule.billboardName.equals(billboard.name)
            );

            return new Ok(billboardList.stream().findFirst().get());
        }
    }
}
