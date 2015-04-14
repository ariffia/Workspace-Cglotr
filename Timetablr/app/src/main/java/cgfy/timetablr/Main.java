package cgfy.timetablr;

import android.app.*;
import android.os.Bundle;

/**
 * Created by ClayFaith on 2015-02-24.
 */
public class Main extends android.app.Activity {
    @Override
    protected void onCreate(Bundle saved_bundle) {
        super.onCreate(saved_bundle);

        String[] strings;
        Timetable timetable;

        strings = new String[1];
        strings[0] = "run!";
        timetable = new Timetable();
    }
}
