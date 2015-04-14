package group13.ecobuddy2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mathee on 2015-02-18.
 */
public class JSONParser {
    private final String LOG_TAG = JSONParser.class.getSimpleName();

    // JSON objects for autocomplete dropdown
    private final String OWM_PREDICTIONS = "predictions";
    private final String OWM_LOCATION_DESCRIPTION = "description";

    // JSON objects for charging locations
    private final String OWM_ID = "ID";
    private final String OWM_UUID = "UUID";
    private final String OWM_ADDRESS_INFO = "AddressInfo";
    private final String OWM_ADDRESS = "AddressLine1";
    private final String OWM_TOWN = "Town";
    private final String OWM_STATEORPROVINCE = "StateOrProvince";
    private final String OWM_LATITUDE = "Latitude";
    private final String OWM_LONGITUDE = "Longitude";
    private final String OWM_CONTACTNUM = "ContactTelephone1";

    /**
     * Parse the dropdown locations
     * @param jQueries
     * @return
     */
    public ArrayList<String> parseDropdownLocations(JSONObject jQueries){
        ArrayList<String> queriesList = new ArrayList<>();
        try {
            JSONArray jsonArray = jQueries.getJSONArray(OWM_PREDICTIONS);

            // * Taking each entry, parses and adds to list object
            for(int i = 0; i < jsonArray.length(); i++) {

                // * * Call getQuery with query JSON object to parse the query
                queriesList.add(jsonArray.getJSONObject(i).getString(OWM_LOCATION_DESCRIPTION));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return the parsed list
        return queriesList;
    }
}

