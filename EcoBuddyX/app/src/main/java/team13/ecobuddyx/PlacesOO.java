package team13.ecobuddyx;

import android.location.Location;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Amir on 2015-03-14.
 */
public class PlacesOO {

    //Queries
    public static final String GAS_STATION = "gas%20station";

    //Source json where everything comes from
    JSONObject sourceJson;

    //Http stuff
    String request;

    /**
     * Places Object Oriented
     * - Uses Google Places API to get information about places
     */
    public PlacesOO(Location location, Integer radius, String query) {
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder();
        stringBuilder.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("keyword=" + query);
        stringBuilder.append("&");
        stringBuilder.append("location=" + locationToLatLngString(location));
        stringBuilder.append("&");
        stringBuilder.append("radius=" + radius);
        stringBuilder.append("&");
        stringBuilder.append("key=" + "AIzaSyDUiYe7CjHzAdCq9h_VuBsn2p0W0FGkrng");

        request = stringBuilder.toString();
    }

    /**
     * Connect
     * - Connect to the server and get the json
     */
    public boolean connect() throws IOException, JSONException {
        HttpClient httpClient;
        HttpGet httpGet;
        HttpResponse httpResponse;
        ByteArrayOutputStream byteArrayOutputStream;

        //Set up the http client
        httpClient = new DefaultHttpClient();

        //Set up the http get request
        httpGet = new HttpGet(request);

        //Catch the response
        httpResponse = httpClient.execute(httpGet);
        byteArrayOutputStream = new ByteArrayOutputStream();
        if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            httpResponse.getEntity().writeTo(byteArrayOutputStream);
        } else {
            return false;
        }

        //Make the json object
        sourceJson = new JSONObject(byteArrayOutputStream.toString());

        return true;
    }

    /**
     * Location To Latitude and Longitude String
     * @param location
     * @return
     */
    public String locationToLatLngString(Location location) {
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder();
        stringBuilder.append(location.getLatitude());
        stringBuilder.append(",");
        stringBuilder.append(location.getLongitude());

        return stringBuilder.toString();
    }
}
