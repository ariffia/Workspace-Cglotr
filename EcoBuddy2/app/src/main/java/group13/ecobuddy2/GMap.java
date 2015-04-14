package group13.ecobuddy2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    }

    /**
     * Draw places
     * - Give this guy a places object and it will draw all places for you <3
     */
    public static class DrawPlacesTask extends AsyncTask<Places, Void, ArrayList<Places.Place>> {

        // Process the places object
        @Override
        protected ArrayList<Places.Place> doInBackground(Places... places) {
            return places[0].placeArrayList;
        }

        // Draw them places
        @Override
        protected void onPostExecute(ArrayList<Places.Place> placeArrayList) {
            MarkerOptions markerOptions;
            Iterator<Places.Place> placeIterator;
            Places.Place place;

            // * Draw the markers
            placeIterator = placeArrayList.iterator();
            while(placeIterator.hasNext()) {

                // * * Get the place
                place = placeIterator.next();

                // *  * Set the marker options
                markerOptions = new MarkerOptions();
                markerOptions.title(place.name);
                markerOptions.snippet(place.vicinity);
                markerOptions.position(place.latLng);

                // * * Actually draw it
                mMap.addMarker(markerOptions);
            }
        }
    }
}
