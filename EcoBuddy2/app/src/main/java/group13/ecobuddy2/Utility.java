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

    /**
     * Convert lat lng to location
     * @param latLng
     * @return
     */
    public static Location latLngToLocation(LatLng latLng) {
        Location tmp;
        tmp = Car.mCurrentLocation;
        tmp.setLatitude(latLng.latitude);
        tmp.setLongitude(latLng.longitude);
        return new Location(tmp);
    }

    /**
     * Find midpoint of the start/end destination
     * @param startCoords
     * @param endCoords
     * @return
     */
    public static LatLng midPoint(LatLng startCoords, LatLng endCoords) {
        double dLon = Math.toRadians(endCoords.longitude - startCoords.longitude);
        double Bx = Math.cos(Math.toRadians(endCoords.latitude)) * Math.cos(dLon);
        double By = Math.cos(Math.toRadians(endCoords.latitude)) * Math.sin(dLon);

        Double latitude = Math.toDegrees(Math.atan2(
                Math.sin(Math.toRadians(startCoords.latitude)) + Math.sin(Math.toRadians(endCoords.latitude)),
                Math.sqrt((Math.cos(Math.toRadians(startCoords.latitude)) + Bx) * (Math.cos(Math.toRadians(startCoords.latitude)) + Bx) + By * By)));

        Double longitude = startCoords.longitude + Math.toDegrees(Math.atan2(By, Math.cos(Math.toRadians(startCoords.latitude)) + Bx));

        return new LatLng(latitude, longitude);
    }
}
