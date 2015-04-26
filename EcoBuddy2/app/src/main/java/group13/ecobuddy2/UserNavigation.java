package group13.ecobuddy2;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class UserNavigation
{
    /**
     * Navigation variables
     */
    public static String mDestination;
    public static MultiDirections mMultiDirections;
//    public static Places mGasPlaces;
//    public static Places mChargePlaces;
    public static ArrayList<Places> mGasPlacesList;
    public static ArrayList<Places> mChargePlacesList;

    // Along route search distance
    public static final Double GAS_ALONG_SEARCH_RANGE = 2500.0;
    public static final Double CHARGE_ALONG_SEARCH_RANGE = 10000.0;

    // Search icon
    public static ImageButton mapButton;

    // Search box
    public static AutoCompleteTextView searchBox;

    // Stop markers
    // - Markers are added here
    public static ArrayList<StopMarker> stopMarkers;

    // Tracking
    public static Long timer;
    public static Switch trackingSwitch;
    public static boolean isNormal;

    /**
     * Rerouting buttons
     */
    public static Button addButton;
    public static Button clearButton;
    public static Button resetButton;

    // Coloring
    public static final int TRANS_WHITE = 0x90ffffff;

    /**
     * All in one user navigation installation
     */
    public static void installUserNavigation() {

        // Inflate the navigation layer
        // - This is the UI layer, make sure it is always on top
        UserNavigation.inflateNavLayer(Main.me);

        // Install the search icon on click
        UserNavigation.installMapIconOnClick(R.id.mapIcon);

        // Install location search capability
        UserNavigation.installDestinationSearch();

        // Install the rerouting dashboard
        installRerouting();

        // Install tracking switch
        timer = Calendar.getInstance().getTimeInMillis();
        trackingSwitch = (Switch) Main.me.findViewById(R.id.modeSwitch);
        trackingSwitch.setBackgroundColor(TRANS_WHITE);
        isNormal = false;
    }

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
     * Install the search icon stuff
     * - Toggle the auto complete search
     * @param id
     */
    public static void installMapIconOnClick(int id) {
        mapButton = (ImageButton) Main.me.findViewById(id);
        mapButton.setBackgroundColor(TRANS_WHITE);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMapType(GMap.mMap);
            }
        });
    }

    /**
     * Switch the map type
     */
    public static boolean switchMapType(GoogleMap googleMap) {

        // Cycle the map type
        switch (googleMap.getMapType()) {
            case GoogleMap.MAP_TYPE_HYBRID:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                makeALongToast("Switched to normal map type");
                break;
            case GoogleMap.MAP_TYPE_NORMAL:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                makeALongToast("Switched to satellite map type");
                break;
            case GoogleMap.MAP_TYPE_SATELLITE:
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                makeALongToast("Switched to terrain map type");
                break;
            case GoogleMap.MAP_TYPE_TERRAIN:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                makeALongToast("Switched to hybrid map type");
                break;
            default:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                makeALongToast("Switched to hybrid map type");
                break;
        }

        return true;
    }

    /**
     * Install the destination search capability
     * - Includes the search box
     * - Search keyboard
     */
    public static void installDestinationSearch() {

        // Get the search box
        searchBox = (AutoCompleteTextView) Main.me.findViewById(R.id.searchBox);
        searchBox.setBackgroundColor(TRANS_WHITE);

        // Set the search box properties
        searchBox.setText("");
        searchBox.setSingleLine();
        searchBox.setLines(1);

        // * Set the search box on click property
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Make the keyboard listen to when DONE is clicked and do something
        // - This will be called when the enter key is pressed
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // Get the destination
                mDestination = searchBox.getText().toString();

                // Get the vanilla directions on the map
                vanillaDirections(mDestination);

                // Bring down the keyboard
                bringDownTheKeyboard();

                // Clear the search box
                searchBox.setText("");

                /**
                 * Get all the gas and charging stations along the route
                 * - Preload the locations as well
                 */
                getStationsAlongTheRoute();

                return true;
            }
        });
    }

    /**
     * Show the keyboard
     */
    public static void bringUpTheKeyboard() {
        InputMethodManager imm;
        imm = (InputMethodManager) Main.me.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBox, 0, null);
    }

    /**
     * Hide the keyboard
     */
    public static void bringDownTheKeyboard() {
        InputMethodManager imm;
        imm = (InputMethodManager) Main.me.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
    }

    /**
     * Make a long toast
     * @param message
     */
    public static void makeALongToast(String message) {
        Toast toast;
        toast = Toast.makeText(Main.me, message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Install the rerouting UI
     */
    public static void installRerouting() {

        // Get the views
        addButton = (Button) Main.me.findViewById(R.id.add);
        clearButton = (Button) Main.me.findViewById(R.id.clear);
        resetButton = (Button) Main.me.findViewById(R.id.reset);

        // Set add station on click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddAButton(v);
            }
        });

        // Set reset station on click listener
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClearButton();
            }
        });

        // Set the show all charging stations button listener
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickResetButton();
            }
        });

        // Set the background color
        addButton.setBackgroundColor(TRANS_WHITE);
        clearButton.setBackgroundColor(TRANS_WHITE);
        resetButton.setBackgroundColor(TRANS_WHITE);

        // Initiate the stop markers
        stopMarkers = new ArrayList<StopMarker>();
    }

    /**
     * Add a gas/charging station to the route
     * - Reroute thingy
     * - Redraw the map every time a marker (station) is added
     * @param view
     */
    public static void onClickAddAButton(View view) {

        // Check if the marker is not null
        if(GMap.mCurrentClickedMarker == null) {
            makeALongToast("Nothing to add");
            return;
        }

        // Redraw the gas/charge locations
        getStationsAlongTheRouteUsingPreloaded();

        // Else, go on
        Marker tmp;
        tmp = GMap.mCurrentClickedMarker;

        // Make the stop marker
        StopMarker stopMarker;
        stopMarker = new StopMarker(
                tmp.getId(),
                tmp.getTitle(),
                tmp.getSnippet(),
                tmp.getPosition()
        );

        // Add this to the stop marker array list
        stopMarkers.add(stopMarker);

        // Clear the currently clicked marker
        GMap.mCurrentClickedMarker = null;

        /**
         * Redo the search bar directions stuff but assume the current marker as
         * the car location
         */

        // Clear the map
        GMap.mMap.clear();

        // Recalculate multi directions

        // * Iterate through and add all of them
        Iterator<StopMarker> iterator;
        StopMarker tmpStopMarker;
        iterator = stopMarkers.iterator();
        while(iterator.hasNext()) {
            tmpStopMarker = iterator.next();
            mMultiDirections.addAStop(Utility.latLngToString(tmpStopMarker.position));
        }
        new GMap.DrawMultiRouteTask().execute(mMultiDirections);

        // Get the nearby gas stations and draw
//        mGasPlaces = new Places(Utility.latLngToLocation(stopMarker.position),
//                Car.getMaxRangeInMeters(),
//                Places.GAS_STATION,
//                Utility.getDevKey()
//        );
//        new GMap.DrawGasPlacesTask().execute(mGasPlaces);

        // Get the nearby charging station and draw
//        mChargePlaces = new Places(Utility.latLngToLocation(stopMarker.position),
//                Car.getMaxRangeInMeters(),
//                Places.CHARGE_POINT,
//                Utility.getDevKey()
//        );
//        new GMap.DrawChargePlacesTask().execute(mChargePlaces);

        // Draw the car range from the marker
//        GMap.drawCarRange(
//                stopMarker.position,
//                Car.getMaxRangeInMeters(),
//                Color.BLUE
//        );

        // Draw the markers
        // - Indicate the stops
        new GMap.DrawStopMarkers().execute(stopMarkers);

        // Make a toast
        makeALongToast("Adding \"" + stopMarker.snippet + "\" to the route. Please wait...");
    }

    /**
     * Clear out the added stations
     */
    public static void onClickClearButton() {

        // Check if null
        if(stopMarkers == null) {
            makeALongToast("Cleaning up...");
            return;
        }
        if(mDestination == null) {
            makeALongToast("Cleaning up...");
            return;
        }

        // Clear the map
        GMap.mMap.clear();

        // Recalculate multi directions

        // * Iterate through and add all of them
//        Iterator<StopMarker> iterator;
//        StopMarker tmpStopMarker;
//        iterator = stopMarkers.iterator();
//        while(iterator.hasNext()) {
//            tmpStopMarker = iterator.next();
//            mMultiDirections.addAStop(Utility.latLngToString(tmpStopMarker.position));
//        }
        new GMap.DrawMultiRouteTask().execute(mMultiDirections);

        // Draw the markers
        // - Indicate the stops
        new GMap.DrawStopMarkers().execute(stopMarkers);

        // Make a toast
        makeALongToast("Cleaning up...");
    }

    /**
     * Reset the route thingy
     */
    public static void onClickResetButton() {

        // Null handling
        if(stopMarkers == null) {
            makeALongToast("Resetting directions...");
            return;
        }
        if(mDestination == null) {
            makeALongToast("Resetting directions...");
            return;
        }

        // Draw the vanilla directions
        vanillaDirections(mDestination);

        // Redraw the gas/charge locations
        getStationsAlongTheRouteUsingPreloaded();

        // Clear the list
        stopMarkers.clear();

        // Make a toast
        makeALongToast("Resetting directions...");
    }

    /**
     * Draw the charging stations along the entire route
     */
    public static void getStationsAlongTheRoute() {

        // Initiate the variables
        mGasPlacesList = new ArrayList<>();
        mChargePlacesList = new ArrayList<>();

        // If multi directions object is not null then OK
        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<Directions> multiDirectionsIterator;
                Iterator<Directions.Step> stepIterator;
                Directions tmpDirections;
                Directions.Step tmpStep;
                Places tmpGasPlaces;
                Places tmpChargePlaces;
                LatLng tmpMidLatLng;

                // Wait for multi directions to finish
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Check for null
                if(mMultiDirections == null) {
                    getStationsAlongTheRoute();
                }

                // Iterate through the multi directions object
                // - The multi directions object should already be connected
                multiDirectionsIterator = mMultiDirections.multiDirections.iterator();
                while(multiDirectionsIterator.hasNext()) {
                    tmpDirections = multiDirectionsIterator.next();

                    // * Iterate through the steps from each directions object
                    stepIterator = tmpDirections.steps.iterator();
                    while(stepIterator.hasNext()) {
                        tmpStep = stepIterator.next();
                        tmpMidLatLng = Utility.midPoint(
                                tmpStep.startLocation,
                                tmpStep.endLocation
                        );

                        // * * Make the API call and the the charging stations around the
                        //     mid point
                        tmpGasPlaces = new Places(
                                Utility.latLngToLocation(tmpMidLatLng),
                                GAS_ALONG_SEARCH_RANGE,  // Search range in meters
                                Places.GAS_STATION,
                                Utility.getDevKey()
                        );
                        tmpGasPlaces.connect();
                        mGasPlacesList.add(tmpGasPlaces);  // Add to the list for pre loading

                        // * * Draw the charging stations
                        new GMap.DrawChargePlacesTask().execute(tmpGasPlaces);

                        // * * Make the API call and the the charging stations around the
                        //     mid point
                        tmpChargePlaces = new Places(
                                Utility.latLngToLocation(tmpMidLatLng),
                                CHARGE_ALONG_SEARCH_RANGE,  // Search range in meters
                                Places.CHARGING_STATIONS,
                                Utility.getDevKey()
                        );
                        tmpChargePlaces.connect();
                        mChargePlacesList.add(tmpChargePlaces);  // Add to the list for pre loading

                        // * * Draw the charging stations
                        new GMap.DrawGasPlacesTask().execute(tmpChargePlaces);
                    }
                }
            }
        }).start();
    }

    /**
     * Draw the charging stations along the entire route for the reset
     */
    public static void getStationsAlongTheRouteUsingPreloaded() {

        // Check for nulls
        if(mGasPlacesList == null) {
            return;
        }
        if(mChargePlacesList == null) {
            return;
        }

        // If multi directions object is not null then OK
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Iterate through the preloaded locations
                Iterator<Places> gasIterator;
                Iterator<Places> chargeIterator;
                Places tmpGasPlaces;
                Places tmpChargePlaces;

                // * Gas stations
                gasIterator = mGasPlacesList.iterator();
                while(gasIterator.hasNext()) {
                    tmpGasPlaces = gasIterator.next();
                    new GMap.DrawChargePlacesTask().execute(tmpGasPlaces);
                }

                // * Charge stations
                chargeIterator = mChargePlacesList.iterator();
                while(chargeIterator.hasNext()) {
                    tmpChargePlaces = chargeIterator.next();
                    new GMap.DrawChargePlacesTask().execute(tmpChargePlaces);
                }
            }
        }).start();
    }

    /**
     * Stop marker object
     */
    public static class StopMarker {
        String id;
        String title;
        String snippet;
        LatLng position;

        /**
         * Stop marker constructor
         * @param id
         * @param title
         * @param snippet
         * @param position
         */
        public StopMarker(String id, String title, String snippet, LatLng position) {
            this.id = id;
            this.title = title;
            this.snippet = snippet;
            this.position = position;
        }
    }

    /**
     * Car tracking
     * @param trackingStatus
     */
    public static void carTracking(Boolean trackingStatus) {
        if(trackingStatus == false) {
            if(isNormal == false) {
                CameraMovement.normalCameraMovement(Car.mCurrentLocation);
                isNormal = true;
            }
        } else {
            // Make the camera track the car
            CameraMovement.makeTheCameraFollowTheCar(Car.mCurrentLocation);
            isNormal = false;
        }
    }

    /**
     * Tell the user what to do
     * @param trackingStatus
     */
    public static void directionsWhatToDoToast(Boolean trackingStatus, Long period) {

        // Error handling
        if(trackingStatus == false) {
            return;
        }
        if(timer == null) {
            return;
        }
        if(mMultiDirections == null) {
            return;
        }

        // Toast the instruction
        Long hasBeenThisLong;
        hasBeenThisLong = Calendar.getInstance().getTimeInMillis() - timer;
        if(hasBeenThisLong > period) {

            // * Reset the timer
            timer = Calendar.getInstance().getTimeInMillis();

            // * Toast it
            Directions.Step currentStep;
            String instruction;
            currentStep = UserNavigation.mMultiDirections.getNextStep(Car.mCurrentLocation);
            if(currentStep == null) {
                return;
            }
            instruction = currentStep.instructions;
            instruction = instruction.replaceAll("<b>", "");
            instruction = instruction.replaceAll("</b>", "");
            UserNavigation.makeALongToast(
                    instruction
            );

            // * Say it!
            Voice.sayThis(instruction);
        }
    }

    /**
     * Get the switch status
     * @return
     */
    public static boolean getTrackingSwitchMode() {
        if(trackingSwitch == null) {
            return false;
        }
        return trackingSwitch.isChecked();
    }

    /**
     * Show the original directions on the map
     * - No stops
     */
    public static void vanillaDirections(String destination) {

        // Clear the map
        GMap.mMap.clear();

        // Initiate multi directions
        mMultiDirections = new MultiDirections(
                Utility.locationToString(Car.mCurrentLocation),
                destination
        );
        new GMap.DrawMultiRouteTask().execute(mMultiDirections);

        // Get the nearby gas stations and draw
//        mGasPlaces = new Places(Car.mCurrentLocation,
//                Car.getPossibleRangeInMeters(),
//                Places.GAS_STATION,
//                Utility.getDevKey()
//        );
//        new GMap.DrawGasPlacesTask().execute(mGasPlaces);

        // Get the nearby charging station and draw
//        mChargePlaces = new Places(Car.mCurrentLocation,
//                Car.getPossibleRangeInMeters(),
//                Places.CHARGE_POINT,
//                Utility.getDevKey()
//        );
//        new GMap.DrawChargePlacesTask().execute(mChargePlaces);

        // Draw the car range
//        GMap.drawCarRange(
//                Utility.latLngFromLocation(Car.mCurrentLocation),
//                Car.getPossibleRangeInMeters(),
//                Color.GREEN
//        );
    }
}
