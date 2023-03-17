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

public class GeoCodeViewAdapter extends ArrayAdapter<GeoCodeLocation> {

    public GeoCodeViewAdapter(Context context, ArrayList<GeoCodeLocation> arrayList) {
        super(context,0, arrayList);
    }

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
