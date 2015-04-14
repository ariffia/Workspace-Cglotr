package group13.ecobuddy2;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class Car
{
    // Store the current location
    public static Location mCurrentLocation;

    // Car status
    static Double battery = 0.5;
    static Double fuel = 0.5;

//    /**
//     * Build Google Api Client. Add location services to the activity
//     */
//    protected static synchronized void buildGoogleApiClient(Main main) {
//        mGoogleApiClient = new GoogleApiClient.Builder(main)
//                .addConnectionCallbacks(main)
//                .addOnConnectionFailedListener(main)
//                .addApi(LocationServices.API)
//                .build();
//    }

//    /**
//     * Get the car's current location
//     * @return
//     */
//    public static Location getCurrentLocation() {
//        return mCurrentLocation;
//    }

//    /**
//     * Set the car's current location
//     * - Basically an update
//     */
//    public static void setCurrentLocation(Location location) {
//        mCurrentLocation = location;
//    }

    /**
     * Get how far can the car travel with its current status
     * @return
     */
    public static Double getPossibleRangeInKm() {
        return battery*50 + fuel*100;
    }
}
