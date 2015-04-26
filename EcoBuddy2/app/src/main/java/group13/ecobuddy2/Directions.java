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
import java.util.Iterator;
import java.util.List;

/**
 * Directions 13-04-2015
 * - Built for EcoBuddy's directions needs
 */
public class Directions
{
    // Http stuff
    public String request;
    public JSONObject json;

    // Json data

    // * Status
    public String status;

    // * Summary
    public String summary;

    // * Journey information
    public int distanceInMeters;
    public int durationInMinutes;

    // * Addresses
    public String startAddress;
    public String endAddress;

    // * Locations
    public LatLng startLocation;
    public LatLng endLocation;

    // * List of steps to do the journey
    public ArrayList<Step> steps;

    // Current step
    int currentStep;

    /**
     * Directions
     * - This is a class built based on object oriented idea, so use this class as an object
     * - This class should represent the json response in its entirety
     * - Built specifically for EcoBuddy's directions requests
     * @param origin Current location
     * @param destination Destination to go to
     * @param apiKey Google's API key with Directions enabled
     */
    public Directions(String origin, String destination, String apiKey) {

        // Make the request
        // - This is going to be an object without the data
        // - use connect() to request the data from the server
        // - This is done so for safety reasons since android does not like to wait
        this.request = makeRequest(origin, destination, apiKey);

        // * Replace empty spaces with something else so we can call properly
        this.request = this.request.replace(" ", "%20");

        // Print the request just to make sure
        Log.d("DIRECTIONS", this.request);
    }

    /**
     * Connect
     * - Actually get the data from a http request
     * - Do this in a thread or it will crash because it takes time to get them
     */
    public void connect() {

        // This will request the data from the server
        this.json = getJsonViaHttp();

        // Process the json and put the data in the variables
        extractDataFromTheJson();
    }

    /**
     * Step
     * - This stores the information of each step
     * - To get to a destination we need to do this steps
     */
    public class Step {

        // Variables for the step
        public int distanceInMeters;
        public int durationInSeconds;
        public String instructions;
        public String travelMode;
        public LatLng startLocation;
        public LatLng endLocation;
        public List<LatLng> polylinePoints;

        /**
         * Step
         * - The constructor of the step
         * @param step
         * @throws JSONException
         */
        public Step(JSONObject step) throws JSONException {

            // Get some data and put them in the variables
            distanceInMeters = step.getJSONObject("distance").getInt("value");
            durationInSeconds = step.getJSONObject("duration").getInt("value");
            instructions = step.getString("html_instructions");
            travelMode = step.getString("travel_mode");

            // Get the start location
            startLocation = new LatLng(
                    step.getJSONObject("start_location").getDouble("lat"),
                    step.getJSONObject("start_location").getDouble("lng")
            );

            // Get the end location
            endLocation = new LatLng(
                    step.getJSONObject("end_location").getDouble("lat"),
                    step.getJSONObject("end_location").getDouble("lng")
            );

            // Get the polyline
            polylinePoints = com.google.maps.android.PolyUtil.decode(
                    step.getJSONObject("polyline").getString("points")
            );
        }
    }

    /**
     * Make Request
     * - Make the http request to use Google's Directions API
     * @param origin
     * @param destination
     * @param apiKey
     * @return
     */
    private String makeRequest(String origin, String destination, String apiKey) {

        // Make the http string
        StringBuilder sb;

        sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/directions/json?");
        sb.append("origin=" + origin);
        sb.append("&");
        sb.append("destination=" + destination);
        sb.append("&");
        sb.append("mode=driving");
        sb.append("&");
        sb.append("key=" + apiKey);

        // Return it
        return sb.toString();
    }

    /**
     * Get Json Via Http
     * - Get the json from the server via http
     * @return
     */
    private JSONObject getJsonViaHttp() {
        HttpClient httpClient;
        HttpGet httpGet;
        HttpResponse httpResponse;
        ByteArrayOutputStream byteArrayOutputStream;
        JSONObject json;

        // Get the default client
        httpClient = new DefaultHttpClient();

        // Set up the request
        httpGet = new HttpGet(request);

        // Make the request and catch the response
        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                httpResponse.getEntity().writeTo(byteArrayOutputStream);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Make the json object
        json = new JSONObject();
        try {
            json = new JSONObject(byteArrayOutputStream.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        // Done and return!
        return json;
    }

    /**
     * Get Data
     * - Take the json from the field and process it to get our variables
     */
    public void extractDataFromTheJson() {

        // We are going to go through the json data and get the much needed data
        JSONArray routes;
        JSONObject routesObj0;
        JSONArray legs;
        JSONObject legsObj0;
        JSONArray steps;

        // We might fail if the data are not available
        try {
            routes = json.getJSONArray("routes");
            routesObj0 = routes.getJSONObject(0);
            legs = routesObj0.getJSONArray("legs");
            legsObj0 = legs.getJSONObject(0);

            // * Get the status code
            this.status = json.getString("status");

            // * Get summary
            this.summary = routesObj0.getString("summary");

            // * Get distance
            this.distanceInMeters = legsObj0.getJSONObject("distance").getInt("value");

            // * Get duration
            this.durationInMinutes = legsObj0.getJSONObject("duration").getInt("value");

            // * Get start address
            this.startAddress = legsObj0.getString("start_address");

            // * Get end address
            this.endAddress = legsObj0.getString("end_address");

            // * Get start location
            this.startLocation = new LatLng(
                    legsObj0.getJSONObject("start_location").getDouble("lat"),
                    legsObj0.getJSONObject("start_location").getDouble("lng")
            );

            // * Get end location
            this.endLocation = new LatLng(
                    legsObj0.getJSONObject("end_location").getDouble("lat"),
                    legsObj0.getJSONObject("end_location").getDouble("lng")
            );

            // * Fill the steps
            JSONObject tempStepJson;
            ArrayList<Step> stepList;

            // * Get the steps
            steps = legsObj0.getJSONArray("steps");
            stepList = new ArrayList<Step>();

            // * Get the elements
            for (int i = 0; i < steps.length(); i++) {
                tempStepJson = steps.getJSONObject(i);
                stepList.add(new Step(tempStepJson));
            }

            // * Put the steps
            this.steps = stepList;
        }

        // Catch this exception!!!
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get All Polyline Points From Steps
     * - Method to get the polyline points to draw a good route on the map
     * @return
     */
    public ArrayList<LatLng> getAllPolylinePointsFromSteps() {

        // We are going to aggregate all the polyline from all the step in one big chunk
        ArrayList<LatLng> retLatLngArrayList;
        Iterator<Step> stepIterator;
        Iterator<LatLng> polylineLatLngIterator;

        // Iterating through the whole steps and extract the data we want
        retLatLngArrayList = new ArrayList<LatLng>();

        // * Check if empty and return
        if(steps == null) {
            return retLatLngArrayList;
        }

        // * Go on if not empty
        stepIterator = steps.iterator();
        while(stepIterator.hasNext()) {
            polylineLatLngIterator = stepIterator.next().polylinePoints.iterator();
            while(polylineLatLngIterator.hasNext()) {
                retLatLngArrayList.add(polylineLatLngIterator.next());
            }
        }

        // Yay, return the aggregate
        return retLatLngArrayList;
    }

    /**
     * Get the current step that the car is in
     * - Return the longest step match
     * - Return null if there is no match
     * @param carLocation
     */
    public Step getNextStep(Location carLocation) {
        Iterator<Step> iterator;
        Step tmp;
        Step returnCandidate;
        Boolean in;

        // Initial is null
        returnCandidate = null;

        // Iterate through the steps and find match if any

        // * Check if empty
        if(steps.size() <= 0) {
            return null;
        }

        // * Else, iterate through
        // - The instruction for the current step is contained in the next step
        for(int i = 0; i < steps.size() - 1; i++) {
            tmp = steps.get(i);
            in = com.google.maps.android.PolyUtil.isLocationOnPath(
                    Utility.latLngFromLocation(carLocation),
                    tmp.polylinePoints,
                    false,  // Geodesic stuff
                    100.0  // Match radius in meters
            );
            if(in) {
                returnCandidate = steps.get(i + 1);
            }
        }
        return returnCandidate;
    }
}
