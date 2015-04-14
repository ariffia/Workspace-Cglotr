package team13.ecobuddyx;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Iterator;
import java.util.List;

/**
 * Main activity
 */
public class MainActivity extends Activity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    public static MapFragment mMapFragment;  // Talk to the map fragment using this
    public static GoogleMap mMap;  // This will be the object to get anything done with map stuff
    private static GoogleApiClient mGoogleApiClient;  // Google API helper. Get us connected
    public static Location mCurrentLocation;  // Store the current location maybe

    /**
     * Called when the activity is first created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        installGoogleMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * When the map is ready, this will be called
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);  // Add a dot on the map showing where we are
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;  // Update the current location
    }

    /**
     * Build Google Api Client
     * Add location services to the activity
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Install the Google map fragment
     */
    public void installGoogleMap() {

        // Get GoogleMap for the map
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        // * Now we start doing stuff
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mMapFragment = MapFragment.newInstance();
        mMapFragment.getMapAsync(this);
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
    }

    /**
     * Set Up Location Request
     */
    public void setUpLocationRequest() {
        LocationRequest mLocationRequest;  //The request itself

        // Now make the request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Get the request
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        );

        // Get the initial location
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    /**
     * Show The Navigation Layout
     * @param view
     */
    public void showTheNavLayout(View view) {
        Intent intent;

        intent = new Intent(this, UserNavActivity.class);
        startActivity(intent);
    }
}
