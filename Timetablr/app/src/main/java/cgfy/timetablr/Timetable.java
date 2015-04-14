package cgfy.timetablr;

import android.util.Log;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by ClayFaith on 2015-02-24.
 */
public class Timetable {

    Day[] days;

    public Timetable() {
        days = new Day[7];

    }

    public static void main(String[] args) {
    }

    /**
     * Activity class
     */
    public class Activity {

        String name;
        String location;
        Time start_time;
        long duration;

        public Activity(String name, String location, long start_time, long duration) {
            this.name = name;
            this.location = location;
            this.start_time = new Time(start_time);
            this.duration = duration;
        }

        public String getPrintName() {
            return name;
        }

        public String getPrintLocation() {
            return location;
        }

        public String getPrintStartTime() {
            return start_time.toString();
        }

        public String getPrintEndTime() {
            return new Time(start_time.getTime() + duration).toString();
        }
    }

    /**
     * Day class
     */
    public class Day {

        Activity[] activities;

        public Day(int num_of_activities) {
            activities = new Activity[num_of_activities];
        }

        public void addAnActivity(Activity activity, int location) {
            activities[location] = activity;
        }
    }
}
