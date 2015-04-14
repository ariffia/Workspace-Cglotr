package cglotr.fyyygpsui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ClayFaith on 2015-02-02.
 */
public class FyyyFragmentManager {

    public FyyyFragmentManager(FyyyMain fyyy_main) {
    }

    //Get the GoogleMap fragment on the application
    public void getTheMap(FyyyMain fyyy_main) {
        FragmentManager fragment_manager;
        FragmentTransaction fragment_transaction;
        MapFragment map_fragment;

        fragment_manager = fyyy_main.getFragmentManager();
        fragment_transaction = fragment_manager.beginTransaction();
        map_fragment = MapFragment.newInstance();

        fragment_transaction.add(R.id.main, map_fragment);
        fragment_transaction.commit();
        map_fragment.getMapAsync(fyyy_main);

        //Move the camera
    }

    //Move the camera
    public static void moveTheCamera() {
    }
}
