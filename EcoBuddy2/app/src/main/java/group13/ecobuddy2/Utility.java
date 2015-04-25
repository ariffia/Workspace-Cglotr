package group13.ecobuddy2;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-12.
 */
public class Utility {

    // Google API key
    private static String devKey;

    /**
     * Get the developer key
     * @return
     */
    public static String getDevKey() {
        return devKey;
    }

    /**
     * Set the developer key
     * @param key
     */
    public static void setDevKey(String key) {
        devKey = key;
    }

    /**
     * Convert location to lat,lng format
     * @param location
     * @return
     */
    public static String locationToString(Location location) {
        StringBuilder sb = new StringBuilder();

        sb.append(location.getLatitude());
        sb.append(",");
        sb.append(location.getLongitude());

        return sb.toString();
    }

    /**
     * Convert lat lng to string
     * @param latLng
     * @return
     */
    public static String latLngToString(LatLng latLng) {
        StringBuilder sb = new StringBuilder();

        sb.append(latLng.latitude);
        sb.append(",");
        sb.append(latLng.longitude);

        return sb.toString();
    }

    /**
     * Convert Location to LatLng
     * @param location
     * @return
     */
    public static LatLng latLngFromLocation(Location location) {
        LatLng latLng;

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        return latLng;
    }

    public static Location latLngToLocation(LatLng latLng) {
        Location tmp;
        tmp = Car.mCurrentLocation;
        tmp.setLatitude(latLng.latitude);
        tmp.setLongitude(latLng.longitude);
        return new Location(tmp);
    }
}
