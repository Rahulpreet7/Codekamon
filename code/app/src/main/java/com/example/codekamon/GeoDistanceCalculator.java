package com.example.codekamon;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import java.text.DecimalFormat;

public interface GeoDistanceCalculator {
    /**
     * @param df This is used to round decimal values for the distance
     * @param geoCalc This is a behavior class that contains the actual implementations of Vincenty’s Formulae.
     * @param ref An immutable property class representing a model of Earth used for geodetic calculations
     */
    DecimalFormat df = new DecimalFormat("#.###");
    GeodeticCalculator geoCalc = new GeodeticCalculator();
    Ellipsoid ref = Ellipsoid.WGS84;
}
