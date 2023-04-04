package com.example.codekamon;

import com.google.android.gms.maps.model.LatLng;
import org.gavaghan.geodesy.*;

import java.text.DecimalFormat;

/**
 * <h1>This class "SpaceBetweenPoints" contains details about the distance between your current location (player's) and code (target's)
 * and etc of the Codekamon</h1>
 *
 * Authors(s): Elisandro Cruz Martinez<br>
 *
 * Package Reference:<br>
 * Mike Gavaghan(2021) Geodesy (Version 1.1.3) [Package] https://github.com/mgavaghan/geodesy <br>
 */

public class SpaceBetweenPoints extends GeoCodeLocation implements GeoDistanceCalculator {
    private double distance; // contains the distance of the two points: current and target.
    /**
     * this constructor does the following:
     * @param name contains the name of the codekamon.
     * @param current contains the current location of the "Player".
     * @param target contains the current location of the "Codekamon".
     */
    public SpaceBetweenPoints(String name, LatLng current, LatLng target) {
        super(name, target.latitude, target.longitude);
        this.distance = distanceBetweenPoints(new GlobalPosition(current.latitude, current.longitude, 0.0));
    }
    /**
     * This method "distanceBetweenPoints" calculates the the two points using the Vincenty’s Formula and returns a double value 3 decimal places.
     * @return calculate the distance between two points (e.g the player and the codekamon)
     */
    private double distanceBetweenPoints(GlobalPosition currentLocation) {
        double distance;
        distance = geoCalc.calculateGeodeticCurve(ref, currentLocation, getLocation()).getEllipsoidalDistance();
        return Double.parseDouble(df.format(distance));
    }
    /**
     * This method "pointsAreWithinRadius" returns a boolean if the distance between two points are within a given range(i.e radius).
     * @param curr   current location of the player
     * @param tar    current position of the QR code
     * @param radius the scope of visibility for the player
     * @return Boolean whether the Codekamon is in the visibility/radius of the player
     */
    public static Boolean pointsAreWithinRadius(LatLng curr, LatLng tar, float radius) {
        GlobalPosition current_point = new GlobalPosition(curr.latitude, curr.longitude, 0.0);
        GlobalPosition target_point = new GlobalPosition(tar.latitude, tar.longitude, 0.0);
        double distance = geoCalc.calculateGeodeticCurve(ref, current_point, target_point).getEllipsoidalDistance();
        return distance <= radius;
    }
    /**
     * This method "getDistance" gets the distance of the player and the codekamon.
     * @return distance of the player to the Codekamon
     */
    public double getDistance() {
        return this.distance;
    }

}