package group13.ecobuddy2;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mathee on 2015-03-25.
 */
public class AutoCompleteTask extends AsyncTask<String, Void, ArrayList<String>> {

    // Result
    // - Be careful, this might be null
    // - Other classes access the result from here
    private static ArrayList<String> resultList;

    // Log stuff
    private static String LOG_TAG = AutoCompleteTask.class.getSimpleName();

    /**
     * Things we do in parallel background
     * @param input
     * @return
     */
    @Override
    protected ArrayList<String> doInBackground(String... input) {
        ArrayList<String> queries = null;

        // If there's no query, there is nothing to look up
        if (input.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string
        String queryJsonStr = null;

        String q = input[0];
        String type = "";  // Old: address
        String key = Utility.getDevKey();

        try {

            // Construct the URL for the query
            final String BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
            final String INPUT_PARAM = "input";
            final String PLACE_TYPE = "types";
            final String KEY_PARAM = "key";

            // Build the request uri
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(INPUT_PARAM, q)
                    .appendQueryParameter(PLACE_TYPE, type)
                    .appendQueryParameter(KEY_PARAM, key)
                    .build();

            // Log
            Log.v(LOG_TAG, "BUILT URI FOR DROPDOWN: " + builtUri.toString());

            // Make the URL?
            URL url = new URL(builtUri.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the result from the stream
            StringBuffer buffer = new StringBuffer();

            try {

                // * Get the input stream from somewhere
                InputStream inputStream = urlConnection.getInputStream();

                // * If there is nothing inside
                if (inputStream == null) {
                    return null;
                }

                // * Read from the input stream
                reader = new BufferedReader(new InputStreamReader(inputStream));

            } catch (FileNotFoundException fe) {
                Log.v(LOG_TAG, "FILE NOT FOUND EXCEPTION CAUGHT...");
            } catch (Exception e) {
                Log.v(LOG_TAG, "EXCEPTION CAUGHT: " + e);
            }

            // * Reading line by line from the buffer
            String line;

            while ((line = reader.readLine()) != null) {

                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            // * Stream was empty.  No point in parsing
            if (buffer.length() == 0) {
                return null;
            }

            // * Transform the buffer to string
            queryJsonStr = buffer.toString();

        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR STATE HIT: ", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        try {
            JSONObject jsonObject = new JSONObject(queryJsonStr);
            JSONParser JSONParser = new JSONParser();

            // Getting the parsed data as a list construct
            queries = JSONParser.parseDropdownLocations(jsonObject);

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }

        // In the end, we return the result in parsed json
        // - I believe it is parsed by JSONParser
        return queries;
    }

    /**
     * Post the result
     * - This takes the output from doInBackground() and publish the result
     * @param arrayLists
     */
    @Override
    protected void onPostExecute(ArrayList<String> arrayLists) {
        resultList = arrayLists;
    }

    /**
     * Get the result
     * - Other classes use this to get the query result
     * - I think it is safer this way
     * @return
     */
    public static ArrayList<String> getResultList() {
        return resultList;
    }
}

