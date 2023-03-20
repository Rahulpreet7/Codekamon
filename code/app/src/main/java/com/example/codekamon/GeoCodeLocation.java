package com.example.codekamon;
import org.gavaghan.geodesy.GlobalPosition;

/**
 * This contains the information of the Codekamon. Its name and location found. <br>
 *
 * Author(s): Elisandro Cruz Martinez <br>
 *
 * Package References:<br>
 * Mike Gavaghan(2021) Geodesy (Version 1.1.3) [Package] https://github.com/mgavaghan/geodesy <br>
 */
public class GeoCodeLocation {
    private String name; // Contains the name of the Codekamon
    private GlobalPosition location; // Contains the Geo location of the Codekamon
    /**
     * This class constructor intiailizes the fields with the following:
     * @param name the name of the codekamon.
     * @param latitude the latitude of which they are located.
     * @param longitude the longitude of which they are located.
     */
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
