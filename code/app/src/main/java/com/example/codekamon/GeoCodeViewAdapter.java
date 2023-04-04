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
 * This class "GeoCodeViewAdapter" is used to display a cell in the list of the codekamon and its details.
 * Author: Elisandro Cruz Martinez
 */
public class GeoCodeViewAdapter extends ArrayAdapter<GeoCodeLocation> {
    /**
     *
     * @param context - provides services like resolving resources, getting access to databases and preferences, etc
     * @param arrayList - a arraylist that contains instances of the GeoCodeLocation object
     */
    public GeoCodeViewAdapter(Context context, ArrayList<GeoCodeLocation> arrayList) {
        super(context,0, arrayList);
    }

    /**
     *
     * the method "getView" gets overwritten and for every item in "GeoCodeLocations" to populate their own individual cell
     * with their name and location where the codekamon is located in the map
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
     * @return a View object containing all of the new information that has to be set to view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewing = convertView;

        if (viewing == null) {
            viewing = LayoutInflater.from(getContext()).inflate(R.layout.adapter_to_search_geo_code, parent, false);
        }

        GeoCodeLocation item = getItem(position);

        GlobalPosition code_item_position = item.getLocation();
        Double latitude = code_item_position.getLatitude(), longitude = code_item_position.getLongitude();

        TextView txt_code_name = viewing.findViewById(R.id.code_name_Search);
        txt_code_name.setText(item.getName());

        TextView txt_code_coordinates = viewing.findViewById(R.id.code_coordinates_Search);

        String location = "Location: \n("+ df.format(latitude) + "," + df.format(longitude) + ")";
        txt_code_coordinates.setText(location);

        return viewing;
    }

}
