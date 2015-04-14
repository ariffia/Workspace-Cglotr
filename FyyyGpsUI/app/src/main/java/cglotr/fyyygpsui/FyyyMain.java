package cglotr.fyyygpsui;

import android.app.Activity;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ClayFaith on 2015-01-31.
 */
public class FyyyMain extends Activity implements OnMapReadyCallback {

    GoogleMap google_map;

    FyyyLocationManager fyyy_location_manager;
    FyyyFragmentManager fyyy_fragment_manager;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.layout_main);

        //Initiating FyyyLocationManager
        fyyy_location_manager = new FyyyLocationManager(this.getApplicationContext());

        fyyy_fragment_manager = new FyyyFragmentManager(this);

        fyyy_fragment_manager.getTheMap(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        google_map = map;
        Log.d("map", "google map is ready");

        LatLng sydney = new LatLng(-33.867, 151.206);

        google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
    }
}
