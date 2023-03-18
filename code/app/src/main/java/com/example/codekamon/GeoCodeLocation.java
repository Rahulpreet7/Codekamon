package com.example.codekamon;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;

import java.text.DecimalFormat;

public class GeoCodeLocation {
    private String name;
    private GlobalPosition location;
    public GeoCodeLocation(String name, double latitude, double longitude){
        this.name = name;
        this.location = new GlobalPosition(latitude,longitude,0.0);
    }
    /**
     * This method "getName" gets the name of the codekamon
     * @return The name of the Codekamon
     */
    public String getName(){return this.name;}
    /**
     * This method "getCoordinates" gets the coordinates location of the codekamon
     * @return the distance (i.e meters) of the Codekamon and Player.
     */
    public GlobalPosition getLocation(){
        return this.location;
    }
}
