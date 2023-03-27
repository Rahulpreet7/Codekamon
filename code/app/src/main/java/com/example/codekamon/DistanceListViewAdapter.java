package com.example.codekamon;

import static com.example.codekamon.GeoDistanceCalculator.df;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.gavaghan.geodesy.GlobalPosition;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class "DistanceListViewAdapter" is used to display a cell in the list of the codekamon and its details
 * Author: Elisandro Cruz Martinez
 */
public class DistanceListViewAdapter extends ArrayAdapter<SpaceBetweenPoints> {

    /**
     * The class constructor "DistanceListViewAdapter" calls upon and passes it parameneters for "SpceBetweenPoints" ArrayAdapter.
     * @param context
     * @param arrayList - An array list of type "SpaceBetweenPoints" that would need to get wraped and converted to a "VIew".
     */
    public DistanceListViewAdapter(Context context, ArrayList<SpaceBetweenPoints> arrayList) {
        super(context,0, arrayList);
    }

    /**
     * the method "getView" gets overwritten and for every item in "SpaceBetweenPoints" to populate their own individual cell
     * with their name, location, and distance between the player and its own.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // We must check if the view we are viewing is in "adapter_view_codeby" layout
        View viewing = convertView;
        if (viewing == null) {
            viewing = LayoutInflater.from(getContext()).inflate(R.layout.adapter_view_codeby, parent, false);
        }

        // Get the information from the super class, which contains the array of "SpaceBetweenPoints" items.
        // We Get the correct position of the item that has been clicked.
        SpaceBetweenPoints item = getItem(position);

        // We get the location of said item that has been clicked
        GlobalPosition code_item_position = item.getLocation();
        Double latitude = code_item_position.getLatitude(), longitude = code_item_position.getLongitude();

        // Set the information to the xml file "adapter_view_codeby" by this specific cell.

        // Set the name to be viewed
        TextView txt_code_name = viewing.findViewById(R.id.code_name);
        txt_code_name.setText(item.getName());

        // Set the coordiantes/location of the item in the View
        TextView txt_code_coordinates = viewing.findViewById(R.id.code_coordinates);
        String location = "Location: \n("+ df.format(latitude) + "," + df.format(longitude) + ")";
        txt_code_coordinates.setText(location);

        // Set the distance of the item in the View
        TextView txt_code_distance = viewing.findViewById(R.id.code_distance);
        String distance = "Distance: \n" + item.getDistance() + "m";
        txt_code_distance.setText(distance);

        //Return the view we checked before.
        return viewing;
    }
}