package cglotr.fyyygpsui;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ClayFaith on 2015-02-01.
 */
public class FyyyLocationManager implements LocationListener {

    //Location manager stuff
    LocationManager location_manager;
    Criteria criteria;
    String provider;
    Location location;

    //Main
    public static void main(String[] strings) {
        Log.d("gps", "FyyyGPS OK");
    }

    //Constructor
    public FyyyLocationManager(Context context) {

        //Location manager
        location_manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = location_manager.getBestProvider(criteria, true);
        location = location_manager.getLastKnownLocation(provider);
        Log.d("location2", "latitude = " + new Double(location.getLatitude()).toString());
        Log.d("location2", "longitude = " + new Double(location.getLongitude()).toString());
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("provider enabled", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("provider disabled", provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location2", "latitude = " + new Double(location.getLatitude()).toString());
        Log.d("location2", "longitude = " + new Double(location.getLongitude()).toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("provider_status", provider + new Integer(status).toString());
    }

    //
    // Method: Get the latest location
    //
    public void getLatestLocation() {
        Log.d("location2", "latitude = " + new Double(location.getLatitude()).toString());
        Log.d("location2", "longitude = " + new Double(location.getLongitude()).toString());
    }
}
