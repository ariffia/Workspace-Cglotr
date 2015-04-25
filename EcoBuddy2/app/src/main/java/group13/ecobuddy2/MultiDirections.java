package group13.ecobuddy2;

import android.location.Location;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amir on 2015-04-10.
 */
public class MultiDirections {

    // Field variables
    public String origin;
    public ArrayList<String> stops;
    public String destination;

    // * Directions
    public ArrayList<Directions> multiDirections;

    /**
     * Multi directions
     * - Used for rerouting
     * @param origin
     * @param destination
     */
    public MultiDirections(String origin, String destination) {
        this.origin = origin;
        stops = new ArrayList<String>();
        this.destination = destination;
        multiDirections = new ArrayList<Directions>();
    }

    /**
     * Add a stop
     * - Used in rerouting to add a gas/charging station
     * @param stop
     */
    public void addAStop(String stop) {
        stops.add(stop);
    }

    /**
     * Connect to the http server
     * - Get the routes
     * - Called after all stops are added
     */
    public void connect() {

        // Make sure that there is at least one stop
        Directions tmp;

        if(this.stops.size() > 0) {

            // Clear the old multi directions first
            multiDirections.clear();

            // * Add the directions from the origin to the first stop
            tmp = new Directions(this.origin, stops.get(0), Utility.getDevKey());
            tmp.connect();
            multiDirections.add(tmp);

            // * Go through the other stops
            for(int i = 1; i < stops.size(); i++) {
                tmp = new Directions(stops.get(i - 1), stops.get(i), Utility.getDevKey());
                tmp.connect();
                multiDirections.add(tmp);
            }

            // * Add the directions from the final stop to the destination
            tmp = new Directions(stops.get(stops.size() - 1), this.destination, Utility.getDevKey());
            tmp.connect();
            multiDirections.add(tmp);

        } else {

            // * There should be at least one stop
            //   - Use Directions instead next time
            tmp = new Directions(this.origin, this.destination, Utility.getDevKey());
            tmp.connect();
            multiDirections.add(tmp);
        }
    }

    /**
     * Get the next step
     * - Since the instruction of the current step is contained in the next step
     * @param carLocation
     * @return
     */
    public Directions.Step getNextStep(Location carLocation) {
        if(multiDirections == null) {
            return null;
        }

        // Iterate through and find the matching step
        Iterator<Directions> iterator;
        Directions tmpDirections;
        Directions.Step tmpStep;
        Directions.Step returnCandidate;

        // * Find it!!!
        returnCandidate = null;
        iterator = multiDirections.iterator();
        while(iterator.hasNext()) {
            tmpDirections = iterator.next();
            tmpStep = tmpDirections.getNextStep(carLocation);
            if(tmpStep != null) {
                returnCandidate = tmpStep;
            }
        }
        return returnCandidate;
    }
}
