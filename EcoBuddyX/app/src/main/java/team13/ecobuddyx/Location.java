package team13.ecobuddyx;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

/**
 * Created by ClayFaith on 2015-03-20.
 */
public class Location {

    /**
     * Set Up Location Request
     */
    public static void setUpLocationRequest(
            GoogleApiClient googleApiClient,
            android.location.Location location,
            Context context
            ) {
        LocationRequest mLocationRequest;  //The request itself

        //Now make the request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //Get the request
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                mLocationRequest,
                (com.google.android.gms.location.LocationListener) context
        );
    }
}
