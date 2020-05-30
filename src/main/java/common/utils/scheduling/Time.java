package common.utils.scheduling;

import java.util.Date;

/**
 * Simple utility functions for time conversions.
 *
 * @author Jamie Martin
 */
public class Time {
    /**
     * Utility function that converts minutes to a time string.
     *
     * @param minutes An integer of minutes that needs converting.
     * @return The minutes converted to a string time with hours and minutes.
     */
    public static String minutesToTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%02d:%02d", hours, mins);
    }

    /**
     * Utility function that converts a date to an integer in minutes.
     *
     * @param date The date that needs converting.
     * @return The integer of time in minutes.
     */
    public static int timeToMinute(Date date) {
        int hours = date.getHours();
        int minutes = date.getMinutes();
        return (hours * 60) + (minutes);
    }
}
