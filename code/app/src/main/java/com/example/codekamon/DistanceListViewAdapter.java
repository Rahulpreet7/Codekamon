package com.example.codekamon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * <h1>This class "DistanceListViewAdapter" is used to display a cell in the list of the codekamon and its details.
 * @author Elisandro Cruz Martinez
 *
 */
public class DistanceListViewAdapter extends ArrayAdapter<DistancePlayerToTarget> {

    public DistanceListViewAdapter(Context context, ArrayList<DistancePlayerToTarget> arrayList) {
        super(context,0, arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewing = convertView;

        if (viewing == null) {
            viewing = LayoutInflater.from(getContext()).inflate(R.layout.adapter_view_codeby, parent, false);
        }

        DistancePlayerToTarget item = getItem(position);

        Double latitude;
        latitude = item.getCoordinates().get(0);
        Double longitude;
        longitude = item.getCoordinates().get(1);

        TextView txt_code_name = viewing.findViewById(R.id.code_name);
        txt_code_name.setText(item.getName());

        TextView txt_code_coordinates = viewing.findViewById(R.id.code_coordinates);

        String location = "Location: \n("+ latitude + "," + longitude + ")";
        txt_code_coordinates.setText(location);

        TextView txt_code_distance = viewing.findViewById(R.id.code_distance);
        String distance = "Distance: \n" + item.getDistance() + "m";
        txt_code_distance.setText(distance);

        return viewing;
    }
}
