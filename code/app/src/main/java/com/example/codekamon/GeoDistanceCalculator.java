package com.example.codekamon;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import java.text.DecimalFormat;
/**
 *
 * A interface "GeoDistanceCalculator" contains some static final constant fields.
 * Which would be used as measurements to calculate distances between current player and
 * the codekamon.
 *
 * Author(s): Elisandro Cruz Martinez, Ryan Rom <br>
 *
 * Package References:<br>
 * Mike Gavaghan(2021) Geodesy (Version 1.1.3) [Package] https://github.com/mgavaghan/geodesy <br>
 */
public interface GeoDistanceCalculator {
    /**
     * + df: This is used to round decimal values for the distance
     * + geoCalc: This is a behavior class that contains the actual implementations of Vincenty’s Formulae.
     * + ref: An immutable property class representing a model of Earth used for geodetic calculations
     */
    DecimalFormat df = new DecimalFormat("#.###");
    GeodeticCalculator geoCalc = new GeodeticCalculator();
    Ellipsoid ref = Ellipsoid.WGS84;
}
