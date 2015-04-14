package ariffia.directions;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ClayFaith on 2015-02-10.
 */
public class Main extends Activity {

    public static Double[][] g_points;

    @Override
    protected void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setContentView(R.layout.home);

        //Http get
        new Thread(new Runnable() {
            @Override
            public void run() {
                String request;
                JSONObject json;
                Double[][] points;
                List<LatLng> lat_lng_list;

                request = Directions.getBasicRequest("hamilton", "toronto", getString(R.string.API_KEY));
                json = Directions.getDirections(request);
                points = Directions.getRoutePoints(json);
                lat_lng_list = Directions.getRoutePolylinePoints(json);

                //Print
                Log.d("ariffia_msg", "Request " + request);
                Log.d("ariffia_msg", "Length " + new Integer(points.length).toString());
                for(int i = 0; i < points.length; i++) {
                    Log.d(
                            "ariffia_msg",
                            new Integer(i).toString() +
                                    " " +
                                    points[i][0].toString() + ", " + points[i][1].toString()
                    );
                }

                //Print
                Iterator<LatLng> lat_lng_iterator;

                lat_lng_iterator = lat_lng_list.iterator();

                while(lat_lng_iterator.hasNext()) {
                    Log.d("POLYLINE POINTS", lat_lng_iterator.next().toString());
                }
            }
        }).start();
    }
}
