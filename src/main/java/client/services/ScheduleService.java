package client.services;

import common.utils.scheduling.Day;
import common.utils.scheduling.DayOfWeek;
import common.models.Schedule;
import common.utils.session.Session;
import common.router.Response;
import common.router.response.Status;
import common.utils.ClientSocketFactory;
import common.utils.scheduling.Time;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for the backend schedule service for the client/viewer.
 *
 * @author Jamie Martin
 */
public class ScheduleService {
    public List<Schedule> schedules;

    /**
     * Initialise new schedule service.
     */
    protected ScheduleService() {
        this.schedules = new ArrayList<>();
    }

    /**
     * Static singleton holder for schedule service.
     */
    private static class ScheduleServiceHolder {
        private final static ScheduleService INSTANCE = new ScheduleService();
    }

    /**
     * Get the schedule service instance.
     *
     * @return The schedule service instance from the singleton.
     */
    public static ScheduleService getInstance() {
        return ScheduleServiceHolder.INSTANCE;
    }

    /**
     * Refreshes the schedule list.
     *
     * @return The new list of schedules from the server.
     */
    public List<Schedule> refresh() {
        Session session = SessionService.getInstance();

        if (session != null) {
            Response result = new ClientSocketFactory("/schedule/get", session.token, null).Connect();

            if (result != null && result.status == Status.SUCCESS && result.body instanceof List) {
                ScheduleServiceHolder.INSTANCE.schedules = (List<Schedule>) result.body;
            }
        }

        return ScheduleServiceHolder.INSTANCE.schedules;
    }

    /**
     * Get the schedule view data.
     *
     * @return The list of days from the schedule for viewing.
     */
    public List<Day> getSchedule() {
        List<Day> schedulesList = new ArrayList<>();

        // iterates over days of the week
        for (var day : DayOfWeek.values()) {
            if (day != DayOfWeek.EVERY) {
                String[] minutesInDay = new String[1440];
                // get the schedules that exist for today
                List<Schedule> todaysList = schedules.stream().filter(s -> s.dayOfWeek == 0 || day.ordinal() == s.dayOfWeek).collect(Collectors.toList());
                // sort them by create time
                todaysList.sort(Comparator.comparing(s -> s.createTime));

                // iterate over each schedule
                for (var schedule : todaysList) {
                    // iterate over the minutes in a day
                    for (int i = schedule.start; i < minutesInDay.length; i = i + schedule.interval) {
                        int diff = schedule.duration;

                        if (i + diff >= 1440) {
                            diff = 1440 - i;
                        }

                        // assign the schedule name for each minute of the day that matches
                        for (int j = i; j < i + diff; j++) {
                            minutesInDay[j] = schedule.billboardName;
                        }
                        if (schedule.interval == 0) {
                            break;
                        }
                    }
                }

                List<String> listOfTimes = new ArrayList<>();

                // iterate over minutes
                for (int i = 0; i < minutesInDay.length - 1; i++) {
                    if (minutesInDay[i] == null) continue;

                    // store the name
                    String name = minutesInDay[i];

                    int j = i;

                    // go until the last index
                    while (j + 1 < 1440 && minutesInDay[j + 1] != null  && minutesInDay[j + 1].equals(name)) j++;

                    // add formatted string to listOfTimes
                    listOfTimes.add(Time.minutesToTime(i) + " - " + Time.minutesToTime(j + 1) + " " + name);

                    i = j;
                }

                schedulesList.add(new Day(day, listOfTimes));
            }
        }

        return schedulesList;
    }

    /**
     * Attempts to insert the given schedule on the server.
     *
     * @param schedule The schedule that is being sent to the server.
     * @return The new list of schedules from the server.
     */
    public List<Schedule> insert(Schedule schedule) {
        Session session = SessionService.getInstance();
        Response res = new ClientSocketFactory("/schedule/insert", session.token, null, schedule).Connect();
        return refresh();
    }

    /**
     * Attempts to delete the given schedule on the server.
     *
     * @param schedule The schedule that is being deleted from the server.
     * @return The new list of schedules from the server.
     */
    public List<Schedule> delete(Schedule schedule) {
        Session session = SessionService.getInstance();
        Response res = new ClientSocketFactory("/schedule/delete", session.token, null, schedule).Connect();
        return refresh();
    }
}
