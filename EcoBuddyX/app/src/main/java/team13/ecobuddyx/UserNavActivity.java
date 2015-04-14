package team13.ecobuddyx;

import android.app.Activity;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 2015-03-06.
 */
public class UserNavActivity extends Activity
{
    public MainActivity mainActivity;

    public static EditText destinationEditText;
    public static Button goButton;
    public static ListView listView;
    public static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_navigation);

        //Assign the views on the variables
        destinationEditText = (EditText) findViewById(R.id.editText);
        goButton = (Button) findViewById(R.id.button2);
        listView = (ListView) findViewById(R.id.listView);

        //List view thingy

        //~Set up the adapter
        ArrayList<String> stringArrayList;
        ArrayAdapter<String> stringArrayAdapter;

        stringArrayList = new ArrayList<String>();
        for(int i = 0; i < 10; i++) {
            stringArrayList.add("Hello");
        }
        stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.for_list_view_test, stringArrayList);
        listView.setAdapter(stringArrayAdapter);

        //TEST: PlacesOO
        new Thread(new Runnable() {
            @Override
            public void run() {
                PlacesOO placesOO;

                placesOO = new PlacesOO(MainActivity.mCurrentLocation, 500, PlacesOO.GAS_STATION);
                Log.i(this.getClass().getSimpleName(), placesOO.request);
                try {
                    placesOO.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.i(this.getClass().getSimpleName(), placesOO.sourceJson.toString());
            }
        }).start();

        /*
        String[] columns = {"_id", "name", "description"};
        int[] ints = {0, 1, 2};
        MatrixCursor matrixCursor = new MatrixCursor(columns, 0);
        matrixCursor.addRow(columns);
        matrixCursor.addRow(columns);
        matrixCursor.addRow(columns);

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.user_navigation, matrixCursor, columns, ints, 0);

        listView = (ListView) findViewById(R.id.listView);
        listView.setBackgroundColor(Color.BLACK);
        listView.setAdapter(simpleCursorAdapter);
        */
    }

    public void go(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DirectionsOO directionsOO = null;
                List<LatLng> latLngList;

                directionsOO = new DirectionsOO("hamilton", "toronto", getString(R.string.key));
                while(directionsOO == null) {

                }
                new DrawRouteTask().execute(directionsOO.getAllPolylinePointsFromSteps());
            }
        }).start();
    }

    /**
     * Draw Route Task
     * - Draw the route on the map
     */
    public class DrawRouteTask extends AsyncTask<List<LatLng>, Void, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(List<LatLng>... latLngList) {
            return latLngList[0];
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            PolylineOptions polylineOptions;

            polylineOptions = new PolylineOptions();
            polylineOptions.addAll(points);

            if(MainActivity.mMap != null) {
                MainActivity.mMap.addPolyline(polylineOptions);
            }
        }
    }

    public void clearTheMap(View view) {
        MainActivity.mMap.clear();
    }
}
