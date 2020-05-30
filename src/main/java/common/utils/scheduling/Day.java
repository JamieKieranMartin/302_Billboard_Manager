package common.utils.scheduling;

import java.util.List;

/**
 * This class consists of the Day object and its associated methods.
 *
 * @author Jamie Martin
 */
public class Day {
    /**
     * The day as a string.
     */
    public String day;

    /**
     * The times in the day.
     */
    public List<String> times;

    /**
     * A constructor that creates the scheduled day list.
     *
     * @param day The day of the list.
     * @param times The list of times scheduled for that day.
     */
    public Day(DayOfWeek day, List<String> times) {
        this.day = day.name();
        this.times = times;
    }
}
