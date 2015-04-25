package group13.ecobuddy2;

import android.location.Location;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class Car
{
    // Store the current location
    public static Location mCurrentLocation;

    // Car status
    public static Double battery = 0.2;
    public static Double fuel = 0.2;

    // Car range
    public static Double batteryRange = 50.0;
    public static Double fuelRange = 100.0;

    /**
     * Call this on location change
     */
    public static void carUpdate(Location location) {
        mCurrentLocation = location;
    }

    /**
     * Get how far can the car travel with its current status
     * @return
     */
    public static Double getPossibleRangeInKm() {
        return battery*batteryRange + fuel*fuelRange;
    }

    /**
     * Get how far the car can travel in meters
     * @return
     */
    public static Double getPossibleRangeInMeters() {
        return getPossibleRangeInKm()*1000;
    }

    /**
     * Get how far can the car travel with max gas and battery
     * @return
     */
    public static Double getMaxRangeInKm() {
        return 1*batteryRange + fuel*fuelRange;
    }

    /**
     * Get how far the car can travel in meters with max gas and battery
     * @return
     */
    public static Double getMaxRangeInMeters() {
        return getMaxRangeInKm()*1000;
    }
}
