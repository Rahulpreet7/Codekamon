package com.example.codekamon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
public class DistancePlayerCodeAdapter extends ArrayAdapter<DistancePlayerCode> {

    public DistancePlayerCodeAdapter(Context context, ArrayList<DistancePlayerCode> arrayList) {
        super(context,0, arrayList);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewing = convertView;

        if (viewing == null) {
            viewing = LayoutInflater.from(getContext()).inflate(R.layout.adapter_view_codeby, parent, false);
        }

        DistancePlayerCode item = getItem(position);

        ((TextView) viewing.findViewById(R.id.code_name)).setText(item.getName());

        Double latitude = item.getCoordinates().get(0);
        Double longitude = item.getCoordinates().get(1);

        ((TextView) viewing.findViewById(R.id.code_coordinates)).setText("Location: ("+ Double.toString(latitude) + "," + Double.toString(longitude) + ")");
        ((TextView) viewing.findViewById(R.id.code_distance)).setText("Distance: " + Double.toString(item.getDistance()));

        return viewing;
    }
}
