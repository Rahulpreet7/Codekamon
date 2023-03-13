package com.example.codekamon;

import com.google.android.gms.maps.model.LatLng;
import org.gavaghan.geodesy.*;

import java.text.DecimalFormat;

/**
 * <h1>This class "DistancePlayerToTarget" contains details about the distance between your current location (player's) and code (target's)
 * and etc of the Codekamon</h1>
 *
 * Authors(s): Elisandro Cruz Martinez<br>
 *
 * Package Reference:\n
 * Karumi(2021) Dexter (Version 6.2.3) [Package] https://github.com/Karumi/Dexter<br>
 * Mike Gavaghan(2021) Geodesy (Version 1.1.3) [Package] https://github.com/mgavaghan/geodesy <br>
 *
 */

public class DistancePlayerToTarget implements Comparable {
    /**
     * Attributes of the "DistancePlayerToTarget" class.
     * @param df This is used to round decimal values for the distance
     * @param geoCalc This is a behavior class that contains the actual implementations of Vincenty’s Formulae.
     * @param ref An immutable property class representing a model of Earth used for geodetic calculations
     * @param distance A distance between the current position of the player and the qr code currently within range in the map.
     * @param currentPosition Measurement of elevation, in meters, above or below the reference Ellipsoid given the long and lat of player's current position.
     * @param targetPosition Measurement of elevation, in meters, above or below the reference Ellipsoid given the long and lat of qr code's current position.
     * @param name This has the name of the qr code.
     */
    private static final DecimalFormat df = new DecimalFormat("#.###");
    private static final GeodeticCalculator geoCalc = new GeodeticCalculator();
    private static final Ellipsoid ref = Ellipsoid.WGS84;
    private double distance;
    private GlobalPosition currentPosition;
    private GlobalPosition targetPosition;
    private String name;
    /**
     * Constructor initializes "DistancePlayerToTarget" class
     * @param name name of the codekamon
     * @param currentLocation the long and lat of the player
     * @param targetLocation the long and lat of the Codekamon
     */
    public DistancePlayerToTarget(String name, LatLng currentLocation, LatLng targetLocation){
        this.name = name;
        this.currentPosition = new GlobalPosition(currentLocation.latitude, currentLocation.longitude, 0.0);
        this.targetPosition = new GlobalPosition(targetLocation.latitude, targetLocation.longitude, 0.0);
        this.distance = distanceBetweenPoints();
    }
    /**
     * This method "distanceBetweenPoints" calculates the the two points using the Vincenty’s Formula and returns a double value 3 decimal places.
     * @return calculate the distance between two points (e.g the player and the codekamon)
     */
    private double distanceBetweenPoints(){
        double distance;
        distance = geoCalc.calculateGeodeticCurve(this.ref,this.currentPosition, this.targetPosition).getEllipsoidalDistance();
        return Double.valueOf(df.format(distance));
    }
    /**
     * This method "pointsAreWithinRadius" returns a boolean if the distance between two points are within a given range(i.e radius).
     * @param curr current location of the player
     * @param tar current position of the QR code
     * @param radius the scope of visibility for the player
     * @return Boolean whether the Codekamon is in the visibility/radius of the player
     */
    public static Boolean pointsAreWithinRadius(LatLng curr, LatLng tar, float radius){
        GlobalPosition current_point =  new GlobalPosition(curr.latitude, curr.longitude, 0.0);
        GlobalPosition target_point = new GlobalPosition(tar.latitude,tar.longitude,0.0);
        double distance = geoCalc.calculateGeodeticCurve(ref, current_point,target_point).getEllipsoidalDistance();
        return (distance <= radius) ? true : false;
    }
    /**
     * This method "getName" gets the name of the codekamon
     * @return The name of the Codekamon
     */
    public String get_name(){return this.name;}
    /**
     * This method "getDistance" gets the distance of the player and the codekamon.
     * @return distance of the player to the Codekamon
     */
    public double get_distance(){return this.distance;}
    /**
     * This method "getCoordinates" gets the coordinates location of the codekamon
     * @return the distance (i.e meters) of the Codekamon and Player.
     */
    public GlobalPosition getCodePosition(){
        return this.targetPosition;
    }
    /**
     * This method "compareTo" is used to sort the distances from the closets to furthers within the radius of visibility.
     * @param o object representing the "DistancePlayerToTarget" we must check.
     * @return The Position of Code
     */
    @Override
    public int compareTo(Object o) {
        DistancePlayerToTarget player = (DistancePlayerToTarget) o;
        return (this.distance < player.get_distance()) ? 1 : 0;
    }
}
