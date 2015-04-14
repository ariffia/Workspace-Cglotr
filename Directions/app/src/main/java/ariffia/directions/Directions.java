package ariffia.directions;

import com.google.android.gms.maps.model.LatLng;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by ClayFaith on 2015-02-11.
 */
public class Directions {

    /**
     * Get Directions
     * @param request
     * @return
     */
    public static JSONObject getDirections(String request) {

        //Set up the http client
        HttpClient http_client;
        HttpGet http_get;
        HttpResponse http_response;
        JSONObject json;
        ByteArrayOutputStream output_stream;

        //~Get the default client
        http_client = new DefaultHttpClient();

        //Set up the request
        http_get = new HttpGet(request);

        //Make the request and catch the response
        output_stream = new ByteArrayOutputStream();
        try {
            http_response = http_client.execute(http_get);
            if(http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                http_response.getEntity().writeTo(output_stream);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Make the json object
        json = new JSONObject();
        try {
            json = new JSONObject(output_stream.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * Get Basic Request
     * @param origin
     * @param destination
     * @param API_KEY
     * @return
     */
    public static String getBasicRequest(String origin, String destination, String API_KEY) {

        //Make the http string
        StringBuilder sb;

        sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/directions/json?");
        sb.append("origin=" + origin);
        sb.append("&");
        sb.append("destination=" + destination);
        sb.append("&");
        sb.append("mode=driving");
        sb.append("&");
        sb.append("key=" + API_KEY);

        return sb.toString();
    }

    /**
     * Get Route Points
     * @param json
     * @return
     */
    public static Double[][] getRoutePoints(JSONObject json) {

        JSONArray routes;
        JSONObject routes_obj0;
        JSONArray legs;
        JSONObject legs_obj0;
        JSONArray steps;
        JSONObject steps_obj;
        Double[][] ret;

        ret = new Double[0][0];
        try {
            routes = json.getJSONArray("routes");
            routes_obj0 = routes.getJSONObject(0);
            legs = routes_obj0.getJSONArray("legs");
            legs_obj0 = legs.getJSONObject(0);
            steps = legs_obj0.getJSONArray("steps");

            //Get the points
            int steps_length;
            Double[][] points;

            steps_length = steps.length();
            points = new Double[steps_length + 1][2];

            //~Fill in the points
            JSONObject step_point;
            Double lat;
            Double lng;

            for(int i = 0; i < steps_length; i++) {
                steps_obj = steps.getJSONObject(i);
                step_point = steps_obj.getJSONObject("start_location");
                lat = step_point.getDouble("lat");
                lng = step_point.getDouble("lng");
                points[i][0] = lat;
                points[i][1] = lng;
            }

            //~Fill the last point
            steps_obj = steps.getJSONObject(steps_length - 1);
            step_point = steps_obj.getJSONObject("end_location");
            lat = step_point.getDouble("lat");
            lng = step_point.getDouble("lng");
            points[steps_length][0] = lat;
            points[steps_length][1] = lng;

            ret = points;
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Get Route Polyline points
     * @param json
     * @return
     */
    public static List<LatLng> getRoutePolylinePoints(JSONObject json) {

        JSONArray routes;
        JSONObject routes_obj0;
        JSONArray legs;
        JSONObject legs_obj0;
        JSONArray steps;
        JSONObject steps_obj;
        JSONObject polyline;
        String points;
        List<LatLng> lat_lng_list;

        lat_lng_list = null;
        try {
            routes = json.getJSONArray("routes");
            routes_obj0 = routes.getJSONObject(0);
            legs = routes_obj0.getJSONArray("legs");
            legs_obj0 = legs.getJSONObject(0);
            steps = legs_obj0.getJSONArray("steps");

            for(int i = 0; i < steps.length(); i++) {
                steps_obj = steps.getJSONObject(i);
                polyline = steps_obj.getJSONObject("polyline");
                points = polyline.getString("points");
                lat_lng_list = com.google.maps.android.PolyUtil.decode(points);
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

        return lat_lng_list;
    }
}
