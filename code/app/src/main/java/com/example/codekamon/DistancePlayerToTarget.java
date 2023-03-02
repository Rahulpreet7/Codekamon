package com.example.codekamon;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

public class DistancePlayerToTarget implements Comparable {
    private static final double EARTH_RADIUS = 6371e3;
    private static final int KM_TO_M = 1000;
    private LatLng target;
    private double distance;
    private String name;

    public DistancePlayerToTarget(String name, LatLng location, Location currentLocation){
       this.target = location;
       this.name = name;
       this.distance = distance(currentLocation);
    }

    public static Boolean isDistanceInRadius(Location currentLocation, LatLng latLng, float RADIUS){

        double
                φ1 = currentLocation.getLatitude() * Math.PI/180,
                φ2 = latLng.latitude * Math.PI/180,
                Δλ = (latLng.longitude - currentLocation.getLongitude()) * Math.PI/180;

        double distance = Math.acos( Math.sin(φ1)*Math.sin(φ2) + Math.cos(φ1)*Math.cos(φ2) * Math.cos(Δλ) ) * EARTH_RADIUS;
        double convert_km_to_m = distance/KM_TO_M;

        return (convert_km_to_m <= RADIUS) ? true : false;

    }
    private double distance(Location currentLocation){

        double
                φ1 = currentLocation.getLatitude() * Math.PI/180,
                φ2 = this.target.latitude * Math.PI/180,
                Δλ = (this.target.longitude - currentLocation.getLongitude()) * Math.PI/180;

        double distance = Math.acos( Math.sin(φ1)*Math.sin(φ2) + Math.cos(φ1)*Math.cos(φ2) * Math.cos(Δλ) ) * EARTH_RADIUS;
        double convert_km_to_m = Math.round(distance/KM_TO_M * 100) / 100;
        return convert_km_to_m;
    }

    public String getName(){
        return this.name;
    }

    public double getDistance(){
        return this.distance;
    }

    public List<Double> getCoordinates(){
        return Arrays.asList(this.target.latitude, this.target.longitude);
    }

    @Override
    public int compareTo(Object o) {
        DistancePlayerToTarget player = (DistancePlayerToTarget) o;
        return (this.distance < player.getDistance()) ? 1 : 0;
    }
}
