package group13.ecobuddy2;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class CameraMovement
{
    public static CameraUpdate followCarCameraUpdate;
    public static CameraPosition followCarCameraPosition;

    /**
     * Make the camera follow the car like in a normal GPS would do
     * @param carLocation
     */
    public static void makeTheCameraFollowTheCar(Location carLocation) {
        followCarCameraPosition = new CameraPosition(
                Utility.latLngFromLocation(carLocation),
                (float) 14.0,
                (float) 45.0,
                carLocation.getBearing()
        );
        followCarCameraUpdate = CameraUpdateFactory.newCameraPosition(followCarCameraPosition);
        GMap.mMap.animateCamera(followCarCameraUpdate);
    }

    /**
     * Make the camera follow the car like in a normal GPS would do
     * @param carLocation
     */
    public static void normalCameraMovement(Location carLocation) {
        followCarCameraPosition = new CameraPosition(
                Utility.latLngFromLocation(carLocation),
                (float) 5.0,
                (float) 0.0,
                carLocation.getBearing()
        );
        followCarCameraUpdate = CameraUpdateFactory.newCameraPosition(followCarCameraPosition);
        GMap.mMap.animateCamera(followCarCameraUpdate);
    }
}
