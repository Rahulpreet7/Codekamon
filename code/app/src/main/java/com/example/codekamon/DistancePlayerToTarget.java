package com.example.codekamon;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>This class "DistancePlayerToTarget" contains details about the location and etc of the Codekamon
 * @author Elisandro Cruz Martinez
 *
 */
public class DistancePlayerToTarget implements Comparable {

    /**
     * The Class "Viewing" obtains the data the user wants to add/edit to his/her records!.
     * @param EARTH_RADIUS                Type:double.                         Constant ACESS_FINE_LOCATION. Used to not write this all the time
     * @param KM_TO_M                     Type:Integer.    This constant is used to convert kilometers to meters from "distance" from player to target.
     * @param target                      Type:LatLng.     This stores the long and lat of the codekamon that has been recorded and found in that place in the map.
     * @param distance                    Type:double      This stores the distance in meters from player to codekamon.
     * @param name                        Type:String.     This stores the name of the codekamon
     */

    private static final double EARTH_RADIUS = 6371e3;
    private static final int KM_TO_M = 1000;
    private LatLng target;
    private double distance;
    private String name;

    /**
     * The constructor "DistancePlayerToTarget" builds the class.
     * @param currentLocation    Type: Location         This parameters contains the location of the player.
     * @param name               Type: String           This parameter contains the name of the codekamon.
     * @param location           Type: LatLng           This parameter contains the location of this specific codekamon.
     */
    public DistancePlayerToTarget(String name, LatLng location, Location currentLocation){
       this.target = location;
       this.name = name;
       this.distance = distance(currentLocation);
    }

    /**
     * This method "isDistanceInRadius" checks if the distance between the player and the codekamon is within the visibility range of the player given a defualt radius in meters.
     * @param currentLocation       Type: Location         This parameters contains the location of the player.
     * @param RADIUS                Type: Float            This parameter contains the radius which the player can see the near by codekamons.
     * @param latLng                Type: LatLng           This parameter contains the long and lat of the codekamon.
     * @return true/false                                  returns True if distance is within the radius stated from the visibility of the player. Else False
     */
    public static Boolean isDistanceInRadius(Location currentLocation, LatLng latLng, float RADIUS){

        double φ1 = currentLocation.getLatitude() * Math.PI/180; // φ, λ in radians
        double φ2 = latLng.latitude * Math.PI/180;
        double Δφ = (latLng.latitude-currentLocation.getLatitude()) * Math.PI/180;
        double Δλ = (latLng.longitude-currentLocation.getLongitude()) * Math.PI/180;

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = EARTH_RADIUS * c; // in metres
        return (d < RADIUS) ? true : false;

    }

    /**
     * This method "distance" gets the distance between the player and the codekamon in meters
     * @param currentLocation       Type: Location         This parameters contains the location of the player.
     */
    private double distance(Location currentLocation){
        double φ1 = currentLocation.getLatitude() * Math.PI/180; // φ, λ in radians
        double φ2 = this.target.latitude * Math.PI/180;
        double Δφ = (this.target.latitude-currentLocation.getLatitude()) * Math.PI/180;
        double Δλ = (this.target.longitude-currentLocation.getLongitude()) * Math.PI/180;

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                  Math.cos(φ1) * Math.cos(φ2) *
                          Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = EARTH_RADIUS * c; // in metres
        DecimalFormat df = new DecimalFormat("#.###");
        d = Double.valueOf(df.format(d));

        return d;
    }
    /**
     * This method "getName" gets the name of the codekamon
     * @return this.name
     */
    public String getName(){
        return this.name;
    }
    /**
     * This method "getDistance" gets the distance of the player and the codekamon.
     * @return this.distance
     */
    public double getDistance(){
        return this.distance;
    }
    /**
     * This method "getTarget" gets the LatLng of the codekamon
     * @return this.target
     */
    public LatLng getTarget(){
        return this.target;
    }
    /**
     * This method "getCoordinates" gets the coordinates location of the codekamon
     * @return ArrayList
     */
    public List<Double> getCoordinates(){
        return Arrays.asList(this.target.latitude, this.target.longitude);
    }
    /**
     * This method "compareTo" is used to sort the distances from the closets to furthers within the radius of visibility.
     * @param o                 Type: Object        This contains the object that is going to be compared.
     * @return ArrayList
     */
    @Override
    public int compareTo(Object o) {
        DistancePlayerToTarget player = (DistancePlayerToTarget) o;
        return (this.distance < player.getDistance()) ? 1 : 0;
    }
}
