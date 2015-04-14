package group13.ecobuddy2;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class Navigation {

    // Search box
    private static AutoCompleteTextView searchBox;

    /**
     * Inflate the navigation layer
     * @param main
     */
    public static void inflateNavLayer(Main main) {
        LayoutInflater inflater;

        // Get the inflater
        inflater = main.getLayoutInflater();

        // Inflate the navigation layer
        inflater.inflate(R.layout.navigation, (RelativeLayout) main.findViewById(R.id.main));
    }

    /**
     * Set the search box
     * @param id
     */
    public static void setSearchBox(int id) {
        searchBox = (AutoCompleteTextView) Main.getMe().findViewById(id);
    }

    /**
     * Install the search icon stuff
     * - Toggle the auto complete search
     * @param id
     */
    public static void installSearchIconOnClick(int id) {
        final ImageButton searchIcon = (ImageButton) Main.getMe().findViewById(id);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchBoxVisibility(R.id.autoCompleteSearch);
            }
        });
    }

    /**
     * Toggle the visibility of the search box
     * - Also pulls up/down the keyboard
     */
    public static void toggleSearchBoxVisibility(int id) {

        // OK, first we need to get the search box
        final AutoCompleteTextView searchBox =
                (AutoCompleteTextView) Main.getMe().findViewById(id);

        // If it is visible hide it
        // - It is invisible by default
        if(searchBox.getVisibility() == View.VISIBLE) {
            searchBox.setVisibility(View.INVISIBLE);

            // Hide the keyboard
            InputMethodManager imm;

            // * Force the keyboard to hide itself
            imm = (InputMethodManager) Main.getMe().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }

        // If it is invisible, show it dammit
        else if(searchBox.getVisibility() == View.INVISIBLE) {
            searchBox.setVisibility(View.VISIBLE);

            // Show the keyboard
            InputMethodManager imm;

            // * Force the keyboard to show up
            imm = (InputMethodManager) Main.getMe().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        // Else. This should not happen
        else {
        }
    }

    /**
     * Setup the keyboard
     */
    public static void setupKeyboard() {

        // Make the keyboard listen to when DONE is clicked and do something
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // * First, make the request and draw
                new DrawRouteTask().execute(searchBox.getText().toString());

                // * Second, clear the text field
                searchBox.setText("");

                // * Third, Close the keyboard
                InputMethodManager imm;

                // * * Force the keyboard to hide itself
                imm = (InputMethodManager) Main.getMe().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);

                return true;
            }
        });
    }

    /**
     * Draw Route Task
     * - Draws detailed route on the map (snaps to the road).
     */
    public static class DrawRouteTask extends AsyncTask<String, Void, ArrayList<LatLng>> {

        private String LOG_TAG = DrawRouteTask.class.getSimpleName();


        @Override
        protected ArrayList<LatLng> doInBackground(String... input) {
            Directions testDirections;

            testDirections = new Directions(
                    Utility.locationToString(Car.mCurrentLocation),
                    input[0],
                    Utility.getDevKey()
            );
            testDirections.connect();

            return testDirections.getAllPolylinePointsFromSteps();
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> points) {

            // Clear the map every time we are drawing directions
            GMap.mMap.clear();

            // Drawing the route
            PolylineOptions routeOptions = new PolylineOptions();
            routeOptions.addAll(points);
            routeOptions.color(Color.BLUE);
            GMap.mMap.addPolyline(routeOptions);
        }
    }
}
