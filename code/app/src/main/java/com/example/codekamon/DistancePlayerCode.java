package com.example.codekamon;

import android.location.Location;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DistancePlayerCode implements Comparable {
    MarkerOptions marker;
    double distance;
    String name;

    public DistancePlayerCode(MarkerOptions m, Location currentLocation){
       this.marker = m;
       this.name = m.getTitle();
       this.distance = distance(currentLocation);
    }

    public MarkerOptions getMarker(){
        return this.marker;
    }

    private double distance(Location currentLocation){
        double y = Math.pow((currentLocation.getLatitude() - this.marker.getPosition().latitude), 2)
                , x = Math.pow((currentLocation.getLongitude() - this.marker.getPosition().longitude), 2);
        return Math.sqrt(y + x);
    }

    public String getName(){
        return this.name;
    }

    public double getDistance(){
        return this.distance;
    }

    public List<Double> getCoordinates(){
        return Arrays.asList(this.marker.getPosition().latitude, this.marker.getPosition().longitude);
    }

    @Override
    public int compareTo(Object o) {
       DistancePlayerCode player = (DistancePlayerCode) o;
        int i = (distance < player.getDistance()) ? 1 : 0;
        return i;
    }
}
