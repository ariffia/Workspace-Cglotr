package group13.ecobuddy2;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class Main extends Activity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    // Hello there, this is me
    public static Main me;

    /**
     * Google API helper. Get us connected. You should instantiate a client object in
     * your Activity's onCreate(Bundle) method and then call connect() in onStart() and
     * disconnect() in onStop(), regardless of the state
     */
    public static GoogleApiClient mGoogleApiClient;

    /**
     * Called when the activity is first created
     * - This is where most setups are done
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get and store the developer key
        // - Do this first since its very important
        // - Many things are dependent on this
        Utility.setDevKey(getString(R.string.key));

        // Get me!
        me = this;

        // Install google map
        GMap.installGoogleMap(this, R.id.map);

        // Install location services
        // - This will provide us with GPS capability and other stuff as well for location
        //   purposes
        // - Build google api client needs to be called in onCreate()
        buildGoogleApiClient();
    }

    /**
     * Called just before the activity becomes visible to the user
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Connect google api client
        mGoogleApiClient.connect();

        // Install user navigation
        // - This is the UI layer and it should be on top of the map

        // * Inflate the navigation layer
        Navigation.inflateNavLayer(this);

        // * Install the search icon on click
        Navigation.installSearchIconOnClick(R.id.searchIcon);

        // * Set the search box
        Navigation.setSearchBox(R.id.autoCompleteSearch);

        // * Set up the keyboard
        Navigation.setupKeyboard();
    }

    /**
     * Called when the activity is no longer visible to the user
     */
    @Override
    protected void onStop() {

        // The client needs to be disconnected here
        mGoogleApiClient.disconnect();
    }

    /**
     * Called just before the activity starts interacting with the user
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Called when the map is ready
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Get the map
        GMap.mMap = googleMap;

        // Setup the map
        // - Set the map first before doing this!
        GMap.setUpMap();
    }

    /**
     * Called when the client is ready
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d("CONNECTION", "Connected");

        // Setup location request
        // - Can only be executed if GoogleApiClient is connected
        setUpLocationRequest();

        // Install auto complete search bar
        DestinationSearch.installAutoCompleteSearch(this, R.id.autoCompleteSearch);

        // TEST
        // Places test
        new Thread(new Runnable() {
            @Override
            public void run() {
                Places places;

                places = new Places(
                        Car.mCurrentLocation,
                        100000,
                        Places.CHARGE_POINT,
                        Utility.getDevKey()
                );
                try {
                    places.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("PLACES", places.request);

                // * Lets print all of the places we have
                for(int i = 0; i < places.placeArrayList.size(); i++) {
                    Log.d("PLACE", places.placeArrayList.get(i).name);
                    Log.d("PLACE", places.placeArrayList.get(i).vicinity);
                    Log.d("PLACE", Utility.latLngToString(places.placeArrayList.get(i).latLng));
                }

                // Draw the places on the map
                new GMap.DrawPlacesTask().execute(places);
            }
        }).start();
    }

    /**
     * Called when the client is connected or disconnected from the service
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("CONNECTION", "Suspended");
    }

    /**
     * This is called every time the car location changes
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

        // Update the car's location in Car class
        Car.mCurrentLocation = location;
    }

    /**
     * Provides callbacks for scenarios that result in a failed attempt to connect the client to the service
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("CONNECTION", "Failed");
    }

    /**
     * Build Google Api Client. Add location services to the activity
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Set Up Location Request
     */
    public void setUpLocationRequest() {
        LocationRequest mLocationRequest;  // The request itself

        // Now make the request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Get the request
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        );

        // Get the initial location
        Car.mCurrentLocation =
                LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    /**
     * Someone will want to use this, so here it is
     * @return
     */
    public static Main getMe() {
        return me;
    }
}

