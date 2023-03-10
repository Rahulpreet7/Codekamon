package com.example.codekamon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.gavaghan.geodesy.GlobalPosition;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>This class "DistanceListViewAdapter" is used to display a cell in the list of the codekamon and its details.
 * @author Elisandro Cruz Martinez
 *
 */
public class DistanceListViewAdapter extends ArrayAdapter<DistancePlayerToTarget> {

    private static final DecimalFormat df = new DecimalFormat("0.0000");

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

        GlobalPosition code_item_position = item.getCodePosition();
        Double latitude = code_item_position.getLatitude(), longitude = code_item_position.getLongitude();

        TextView txt_code_name = viewing.findViewById(R.id.code_name);
        txt_code_name.setText(item.get_name());

        TextView txt_code_coordinates = viewing.findViewById(R.id.code_coordinates);

        String location = "Location: \n("+ df.format(latitude) + "," + df.format(longitude) + ")";
        txt_code_coordinates.setText(location);

        TextView txt_code_distance = viewing.findViewById(R.id.code_distance);
        String distance = "Distance: \n" + item.get_distance() + "m";
        txt_code_distance.setText(distance);

        return viewing;
    }
}
