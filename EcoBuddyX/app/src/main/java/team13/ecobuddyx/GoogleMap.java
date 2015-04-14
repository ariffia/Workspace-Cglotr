package team13.ecobuddyx;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.MapFragment;

/**
 * Google map
 */
public class GoogleMap extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
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
    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

    }

    /**
     * Install the Google map fragment
     */
    public static void installGoogleMap(
            FragmentTransaction fragmentTransaction,
            MapFragment mapFragment,
            Context context)
    {
        mapFragment = MapFragment.newInstance();
        mapFragment.getMapAsync((com.google.android.gms.maps.OnMapReadyCallback) context);
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();
    }
}
