package group13.ecobuddy2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-11.
 */
public class DestinationSearch {

    // Field variables
    private static AutoCompleteTextView searchBox;
    private static ArrayList<String> filteredList = new ArrayList<>();

    /**
     * Install auto complete search bar
     */
    public static void installAutoCompleteSearch(Main main, int searchBoxId) {

        // Get the search box
        searchBox = (AutoCompleteTextView) main.findViewById(searchBoxId);

        // Install the adapter
        searchBox.setAdapter(new QueryAutoCompleteAdapter(main, android.R.layout.simple_dropdown_item_1line));
    }

    /**
     * Query auto complete adapter
     */
    public static class QueryAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

        public QueryAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            if (!filteredList.isEmpty()) {
                return filteredList.size();
            } else {
            }
            return 0;
        }

        @Override
        public String getItem(int index) {
            return filteredList.get(index).toString();
        }

        // Called each time the adapter needs to populate another row in the view
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            // convertView is essentially each respective row
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search_item, parent, false);
            }

            final View rowView = convertView;

            try {
                Main.me.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView destinationLocation = (TextView) rowView.findViewById(R.id.search_item_destination);

                        if (!filteredList.isEmpty()) {
                            try {
                                String location = filteredList.get(position);
                                destinationLocation.setText(location);
                                rowView.setTag(location);

                            } catch (IndexOutOfBoundsException IE) {
                            } catch (Exception e) {
                            }
                        }
                    }
                });
            } catch (Exception e) {
            }
            return rowView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                public FilterResults performFiltering(final CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    // Constraint is the input given by the user
                    if (constraint != null) {
                        // Retrieve the autocomplete results.

                        new AutoCompleteTask().execute(constraint.toString());
                        filteredList = AutoCompleteTask.getResultList();
                        if(AutoCompleteTask.getResultList() != null) {

                            // Assign the data to the FilterResults
                            filterResults.values = filteredList;
                            filterResults.count = filteredList.size();
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(final CharSequence constraint, final FilterResults results) {
                    Main.me.runOnUiThread(new Runnable() {
                        public void run() {
                            if (results != null && results.count > 0) {
                                notifyDataSetChanged();
                            } else {
                                notifyDataSetInvalidated();
                            }
                        }
                    });

                }};
            return filter;
        }
    }
}
