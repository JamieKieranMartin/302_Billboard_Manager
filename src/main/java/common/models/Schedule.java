package common.models;

import client.components.table.DisplayAs;
import client.components.table.Editable;
import common.sql.SQLITE;
import common.utils.RandomFactory;
import common.utils.scheduling.Time;

import java.io.Serializable;
import java.time.Instant;

/**
 * This class consists of the Schedule object and its associated methods.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 * @author Hieu Nghia Huynh
 */
@SQLITE(type="FOREIGN KEY(billboardName) REFERENCES Billboard(name)")
public class Schedule implements Serializable, Editable {
    /**
     * The schedule ID.
     */
    @SQLITE(type="INTEGER PRIMARY KEY AUTOINCREMENT")
    public int id;

    /**
     * The scheduled billboard.
     */
    @SQLITE(type="VARCHAR(255) NOT NULL")
    public String billboardName;

    /**
     * The schedule days of the week.
     */
    // 0 - 7
    // Every, Sun - Sat
    @SQLITE(type="INTEGER NOT NULL")
    public int dayOfWeek = 0;

    /**
     * The schedule starting time.
     */
    // 0 - 1440 / 1439?
    // 00:00 - 23:59
    @SQLITE(type="INTEGER NOT NULL")
    public int start = 0;

    /**
     * The schedule creation time.
     */
    @SQLITE(type="DATETIME NOT NULL")
    public Instant createTime = Instant.now(); // Time when create the schedule

    /**
     * The schedule duration.
     */
    @SQLITE(type="INTEGER NOT NULL")
    public int duration;

    /**
     * The schedule integer.
     */
    @SQLITE(type="INTEGER NOT NULL")
    public int interval;

    /**
     * An empty constructor just for creating the schedule object.
     */
    public Schedule() {

    }

    /**
     * Schedule object constructor
     *
     * @param id Schedules id.
     * @param billboardName Schedules billboard name.
     * @param start Schedules billboard starting time.
     * @param createTime Schedules billboard creating time.
     * @param duration Schedules billboard duration.
     * @param interval Schedules billboard interval.
     */
    public Schedule(int id,
                    String billboardName,
                    int dayOfWeek,
                    int start,
                    Instant createTime,
                    int duration,
                    int interval) {
        this.id = id;
        this.billboardName = billboardName;
        this.dayOfWeek = dayOfWeek;
        this.start = start;
        this.createTime = createTime;
        this.duration = duration;
        this.interval = interval;
    }

    /**
     * Schedule object constructor
     *
     * @param billboardName Schedules billboard name.
     * @param start Schedules billboard starting time.
     * @param duration Schedules billboard duration.
     * @param createTime Schedules billboard create time.
     * @param interval Schedules billboard interval.
     */
    public Schedule(
        String billboardName,
        int dayOfWeek,
        int start,
        Instant createTime,
        int duration,
        int interval) {
        this.billboardName = billboardName;
        this.dayOfWeek = dayOfWeek;
        this.start = start;
        this.createTime = createTime;
        this.duration = duration;
        this.interval = interval;
    }

    // Display annotation data relative to the objects fields
    /**
     * Returns the schedule id for the Client display.
     *
     * @return The integer that is the schedule id.
     */
    @DisplayAs(value = "Id", index = 0)
    public int getId() {
        return id;
    }

    /**
     * Returns the schedule name for the Client display.
     *
     * @return The string that is the scheduled billboard name.
     */
    @DisplayAs(value = "Billboard Name", index = 1)
    public String getName() {
        return billboardName;
    }

    /**
     * Returns the schedules day of the week for the Client display.
     *
     * @return The string that is the day of the week.
     */
    @DisplayAs(value = "Day Of Week", index = 2)
    public String getDayOfWeek() {
        switch (dayOfWeek) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "Every";
        }
    }

    /**
     * Returns the schedules start time for the Client display.
     *
     * @return The string that is the start time.
     */
    @DisplayAs(value = "Start Time", index = 3)
    public String getStart() {
        return Time.minutesToTime(start);
    }

    /**
     * Returns the schedules duration for the Client display.
     *
     * @return The string that is the duration.
     */
    @DisplayAs(value = "Duration", index = 4)
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the schedules interval for the Client display.
     *
     * @return The string that is the interval.
     */
    @DisplayAs(value = "Interval", index = 5)
    public int getInterval() {
        return interval;
    }

    /**
     * Returns if the schedule is editable for the Client display.
     *
     * @return The boolean that determines whether the schedule is editable.
     */
    @Override
    public boolean isEditable() {
        return false;
    }

    /**
     * Generates a random Schedule object with random variables
     *
     * @param billboardName The name of the billboard.
     * @return A randomised schedule object.
     */
    public static Schedule Random(String billboardName) {
        return new Schedule(
            billboardName,
            RandomFactory.Int(7),
            RandomFactory.Int(1400),
            Instant.now(),
            RandomFactory.Int(30),
            RandomFactory.Int(60)
        );
    }
}
