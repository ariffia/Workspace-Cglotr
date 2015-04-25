package group13.ecobuddy2;

import android.location.Location;
import android.util.Log;

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
import java.util.ArrayList;

/**
 * Places 14-04-2015
 * - Built for EcoBuddy's places needs
 */
public class Places {

    // Queries
    public static final String GAS_STATION = "gas station";
    public static final String CHARGE_POINT = "charge point charging station";

    // Source json where everything comes from
    public JSONObject sourceJson;

    // Http stuff
    public String request;

    // Places data
    public ArrayList<Place> placeArrayList;

    /**
     * Places Object Oriented
     * - Uses Google Places API to get information about places
     */
    public Places(Location location, Double radiusInMeters, String query, String key) {
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder();
        stringBuilder.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("keyword=" + query);
        stringBuilder.append("&");
        stringBuilder.append("location=" + locationToLatLngString(location));
        stringBuilder.append("&");
        stringBuilder.append("radius=" + radiusInMeters);
        stringBuilder.append("&");
        stringBuilder.append("key=" + key);

        request = stringBuilder.toString();

        // * Replace empty spaces with something else so we can call properly
        //   - Just in case
        this.request = this.request.replace(" ", "%20");

        Log.d("PLACES", this.request);

        // Initialize the place array list
        placeArrayList = new ArrayList<Place>();
    }

    /**
     * Store the information of the places we got from the request
     */
    public class Place {

        // Data
        public String name;
        public String vicinity;
        public LatLng latLng;

        /**
         * Constructor
         */
        public Place() {
        }

        /**
         * Get the place name
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * Get the kinda like the address
         * @return
         */
        public String getVicinity() {
            return vicinity;
        }

        /**
         * Get the coordinates in lat,lng
         * @return
         */
        public LatLng getLatLng() {
            return latLng;
        }
    }

    /**
     * Extract the data from our json and store them locally here
     */
    private void extractData() throws JSONException {

        // We are going to extract the data from the local source json and store
        // the data nicely in "Place" objects
        // - Place objects are going to be in an array list and it is accessible for the world
        //   to use!
        JSONArray resultsArray;
        JSONObject resultObject;
        JSONObject geometry;
        JSONObject location;
        Place tmpPlace;

        // * Get the results and iterate through them
        resultsArray = sourceJson.getJSONArray("results");
        for(int i = 0; i < resultsArray.length(); i++) {

            // * * Temporary place
            tmpPlace = new Place();

            // * * Get the data!
            resultObject = resultsArray.getJSONObject(i);
            geometry = resultObject.getJSONObject("geometry");
            location = geometry.getJSONObject("location");

            // * * * Get the name
            tmpPlace.name = resultObject.getString("name");

            // * * * Get the vicinity
            tmpPlace.vicinity = resultObject.getString("vicinity");

            // * * * Get the location coordinates
            tmpPlace.latLng = new LatLng(
                    location.getDouble("lat"),
                    location.getDouble("lng")
            );

            // * * Add the place to the array list
            placeArrayList.add(tmpPlace);
        }
    }

    /**
     * Connect
     * - Connect to the server and get the json
     */
    public void connect() {
        HttpClient httpClient;
        HttpGet httpGet;
        HttpResponse httpResponse;
        ByteArrayOutputStream byteArrayOutputStream;

        // Set up the http client
        httpClient = new DefaultHttpClient();

        // Set up the http get request
        httpGet = new HttpGet(request);

        // Extract the data
        byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            // * Catch the response
            httpResponse = httpClient.execute(httpGet);
            httpResponse.getEntity().writeTo(byteArrayOutputStream);

            // * Make the json object
            sourceJson = new JSONObject(byteArrayOutputStream.toString());

            // * Now, get the data and store them in the place array list
            extractData();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Location To Latitude and Longitude String
     * @param location
     * @return
     */
    private String locationToLatLngString(Location location) {
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder();
        stringBuilder.append(location.getLatitude());
        stringBuilder.append(",");
        stringBuilder.append(location.getLongitude());

        return stringBuilder.toString();
    }
}
