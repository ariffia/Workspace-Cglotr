package group13.ecobuddy2;
import group13.ecobuddy2.UserNavigation.StopMarker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class GMap
{
    public static MapFragment mMapFragment;  // Talk to the map fragment using this
    public static GoogleMap mMap;  // This will be the object to get anything done with map stuff

    // Marker
    // - Track the currently clicked marker
    public static Marker mCurrentClickedMarker;

    /**
     * Install the Google map fragment
     */
    public static void installGoogleMap(Main main, Integer id) {

        // Get GoogleMap for the map

        // * Require fragment transaction
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        // * Get the fragment manager
        fragmentManager = main.getFragmentManager();

        // * Get the fragment transaction
        fragmentTransaction = fragmentManager.beginTransaction();

        // * Get the map instance
        mMapFragment = MapFragment.newInstance();

        // * Sync the map
        mMapFragment.getMapAsync(main);

        // * Publish the map on the layout
        fragmentTransaction.add(id, mMapFragment);
        fragmentTransaction.commit();
    }

    /**
     * Setup the map to our needs
     */
    public static void setUpMap() {

        // Enable the blue dot
        mMap.setMyLocationEnabled(true);

        // Disable the button that moves the camera to the blue dot
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Disable the the google toolbar that appears when a marker is selected
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Disable the compass
        mMap.getUiSettings().setCompassEnabled(false);

        // Setup the marker on click listener
        setupMarkerOnClickListener();
    }

    /**
     * Setup what happens if a marker is clicked
     * - Update the currently clicked marker
     */
    public static void setupMarkerOnClickListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mCurrentClickedMarker = marker;

                // This needs to be false so that the default calls will be triggered
                return false;
            }
        });
    }

    /**
     * Draw directions route task
     * - Draws detailed route on the map
     */
    public static class DrawRouteTask extends AsyncTask<Directions, Void, Directions> {

        /**
         * Takes directions and return the polyline to draw
         * - Connect the object as well
         * @param directions
         * @return
         */
        @Override
        protected Directions doInBackground(Directions... directions) {
            directions[0].connect();
            return directions[0];
        }

        /**
         * Actually drawing the route
         * - Check for nulls
         * @param directions
         */
        @Override
        protected void onPostExecute(Directions directions) {
            ArrayList<LatLng> points;

            // Return if points is empty
            if(directions.getAllPolylinePointsFromSteps().size() <= 0) {
                return;
            }

            // Drawing the route
            PolylineOptions routeOptions = new PolylineOptions();

            // * Get the polyline
            points = directions.getAllPolylinePointsFromSteps();

            // * Set the route options
            routeOptions.geodesic(true);
            routeOptions.addAll(points);
            routeOptions.color(Color.CYAN);

            // * Actually draw it
            mMap.addPolyline(routeOptions);
        }
    }

    /**
     * Draw multi route task
     * - Same with the normal route task, but this is for multi directions
     */
    public static class DrawMultiRouteTask extends AsyncTask<MultiDirections, Void, MultiDirections> {

        /**
         * Pass the multi directions object to the on post execute
         * @param args
         * @return
         */
        @Override
        protected MultiDirections doInBackground(MultiDirections... args) {
            args[0].connect();
            return args[0];
        }

        /**
         * Actually drawing the polyline
         * @param multiDirections
         */
        @Override
        protected void onPostExecute(MultiDirections multiDirections) {

            // Return if multi directions is empty
            // - Important for error handling
            if(multiDirections.multiDirections.size() <= 0) {
                return;
            }

            // Draw the start and end markers
            drawStartAndEnd(
                    Utility.latLngFromLocation(Car.mCurrentLocation),  // Start lat lng
                    multiDirections.getLastDirections().endLocation  // End lat lng
            );

            // Draw the routes
            PolylineOptions routeOptions;
            Iterator<Directions> iterator;
            ArrayList<LatLng> multiRoutes;

            // * Combine all the polyline points
            iterator = multiDirections.multiDirections.iterator();
            multiRoutes = new ArrayList<>();
            while(iterator.hasNext()) {
                multiRoutes.addAll(iterator.next().getAllPolylinePointsFromSteps());
            }

            // * Specify the route options
            routeOptions = new PolylineOptions();
            routeOptions.geodesic(true);
            routeOptions.color(Color.BLUE);

            // * * Add the polyline
            routeOptions.addAll(multiRoutes);

            // * Actually draw it in the map
            GMap.mMap.addPolyline(routeOptions);
        }
    }

    /**
     * Draw gas places
     * - Give this guy a places object and it will draw all places for you <3
     */
    public static class DrawGasPlacesTask extends AsyncTask<Places, Void, ArrayList<Places.Place>> {

        /**
         * Process the places object
         * @param places
         * @return
         */
        @Override
        protected ArrayList<Places.Place> doInBackground(Places... places) {
            places[0].connect();
            return places[0].placeArrayList;
        }

        /**
         * Draw them places
         * @param placeArrayList
         */
        @Override
        protected void onPostExecute(ArrayList<Places.Place> placeArrayList) {
            MarkerOptions markerOptions;
            Iterator<Places.Place> placeIterator;
            Places.Place place;

            // Return if empty
            if(placeArrayList.size() <= 0) {
                return;
            }

            // * Draw the markers
            placeIterator = placeArrayList.iterator();
            while(placeIterator.hasNext()) {

                // * * Get the place
                place = placeIterator.next();

                // *  * Set the marker options
                markerOptions = new MarkerOptions();
                markerOptions.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                markerOptions.title(place.name);
                markerOptions.snippet(place.vicinity);
                markerOptions.position(place.latLng);

                // * * Actually draw it
                mMap.addMarker(markerOptions);
            }
        }
    }

    /**
     * Draw charge places
     * - Give this guy a places object and it will draw all places for you <3
     */
    public static class DrawChargePlacesTask extends AsyncTask<Places, Void, ArrayList<Places.Place>> {

        /**
         * Process the places object
         * @param places
         * @return
         */
        @Override
        protected ArrayList<Places.Place> doInBackground(Places... places) {
            places[0].connect();
            return places[0].placeArrayList;
        }

        /**
         * Draw them places
         * @param placeArrayList
         */
        @Override
        protected void onPostExecute(ArrayList<Places.Place> placeArrayList) {
            MarkerOptions markerOptions;
            Iterator<Places.Place> placeIterator;
            Places.Place place;

            // Return if empty
            if(placeArrayList.size() <= 0) {
                return;
            }

            // * Draw the markers
            placeIterator = placeArrayList.iterator();
            while(placeIterator.hasNext()) {

                // * * Get the place
                place = placeIterator.next();

                // *  * Set the marker options
                markerOptions = new MarkerOptions();
                markerOptions.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                );
                markerOptions.title(place.name);
                markerOptions.snippet(place.vicinity);
                markerOptions.position(place.latLng);

                // * * Actually draw it
                mMap.addMarker(markerOptions);
            }
        }
    }

    /**
     * Draw stop markers
     * - Shows where the stops are to the user
     */
    public static class DrawStopMarkers
            extends AsyncTask<ArrayList<StopMarker>, Void, ArrayList<StopMarker>>
    {

        /**
         * Just pass the variables
         * @param params
         * @return
         */
        @Override
        protected ArrayList<StopMarker> doInBackground(ArrayList<StopMarker>... params) {
            return params[0];
        }

        /**
         * Actually drawing them
         * @param stopMarkers
         */
        @Override
        protected void onPostExecute(ArrayList<StopMarker> stopMarkers) {
            MarkerOptions markerOptions;
            Iterator<StopMarker> stopMarkerIterator;
            StopMarker stopMarker;

            // Iterate through all of them and draw as well
            stopMarkerIterator = stopMarkers.iterator();
            while(stopMarkerIterator.hasNext()) {
                stopMarker = stopMarkerIterator.next();

                // * Set the options
                markerOptions = new MarkerOptions();
                markerOptions.title(stopMarker.title);
                markerOptions.snippet(stopMarker.snippet);
                markerOptions.position(stopMarker.position);
                markerOptions.icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                );

                // * Draw it on the map
                GMap.mMap.addMarker(markerOptions);
            }

            return;
        }
    }

    /**
     * Draw the start and end marker
     * @param start
     * @param end
     */
    public static void drawStartAndEnd(LatLng start, LatLng end) {
        MarkerOptions startOptions;
        MarkerOptions endOptions;

        // Set the marker options for the start marker
        startOptions = new MarkerOptions();
        startOptions.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        );
        startOptions.position(start);

        // Set the marker options for the end marker
        endOptions = new MarkerOptions();
        endOptions.icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        );
        endOptions.position(end);

        // Draw both of them
        GMap.mMap.addMarker(startOptions);
        GMap.mMap.addMarker(endOptions);
    }

    /**
     * Show how far the car can go on the map
     */
    public static void drawCarRange(LatLng center,
                                    Double radiusInMeters,
                                    int color)
    {
        CircleOptions circleOptions;

        circleOptions = new CircleOptions();
        circleOptions.center(center);
        circleOptions.radius(radiusInMeters);
        circleOptions.strokeColor(color);
        circleOptions.strokeWidth((float) 0.0);
        circleOptions.fillColor(0x1000ff00);

        GMap.mMap.addCircle(circleOptions);
    }

    /**
     * Pass a multi directions object and this async task will draw the markers
     */
    public static class DrawChargingMarkersTask
        extends AsyncTask<MultiDirections, Void, LatLng>
    {
        /**
         * We might not need this class
         * @param params
         * @return
         */
        @Override
        protected LatLng doInBackground(MultiDirections... params) {
            Iterator<Directions> multiDirectionsIterator;
            Directions tmpDirections;
            Places tmpPlaces;

            // Check for null
            if(params[0] == null) {
                return null;
            }

            // Iterate through the multi directions object
            // - The multi directions object should already be connected
            multiDirectionsIterator = params[0].multiDirections.iterator();
            while(multiDirectionsIterator.hasNext()) {
                tmpDirections = multiDirectionsIterator.next();

                // * Get the charging stations around the end of the directions object's end
                tmpPlaces = new Places(
                        Utility.latLngToLocation(tmpDirections.endLocation),
                        Car.getMaxRangeInMeters(),
                        Places.CHARGE_POINT,
                        Utility.getDevKey()
                        );
                tmpPlaces.connect();

                // * Iterate through places object to get individual place
            }

            return null;
        }
    }
}
