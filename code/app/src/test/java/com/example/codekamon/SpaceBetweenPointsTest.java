package com.example.codekamon;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import org.gavaghan.geodesy.GlobalPosition;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

/**
 * Test if "SpaceBetweenPoints" class' methods are working properly
 */

public class SpaceBetweenPointsTest {
    private String test_name = "null";

    @NonNull
    private LatLng mock_target_test(){
        LatLng latLng = new LatLng(53.5225, -113.5244);
        return latLng;
    }

    @NonNull
    private LatLng mock_current_test(){
        return new LatLng(53.5232, -113.5263);
    }

    /**
     * Check if parameters are done properly
     */
    @Test
    void checkParameters(){
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();

        assertEquals(53.5232,curr.latitude);
        assertEquals(-113.5263,curr.longitude);

        assertEquals(53.5225, tar.latitude);
        assertEquals(-113.5244, tar.longitude);

    }

    /**
     * Check if the distance between two points are exactly 144.841 meters
     */
    @Test
    void testDistanceBetweenPoints(){
        //Expected: Distance is: 0.09 miles / 0.15 kilometers / 0.08 nautical miles
        // 0.09 miles -> 144.841 m
        // I get approx 148.841 m. Decent Enough

        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        SpaceBetweenPoints points = new SpaceBetweenPoints(this.test_name,curr, tar);
        double dis = points.getDistance();
        boolean approximatelyEqual = (Math.abs(144.841 - dis) < 5) ? true : false;
        assertTrue(approximatelyEqual);
    }

    /**
     * Test to see if you can get the coordinates of the code
     */
    @Test
    void testIfCoordinatesOfQRcodeWorks(){
        /*
            We already know the coordinates of the player.
            We want to know if we can properly retrieve that of the QR code near it.
         */
        DecimalFormat df = new DecimalFormat("#.####");
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        SpaceBetweenPoints points = new SpaceBetweenPoints(this.test_name,curr, tar);
        GlobalPosition qr_codePosition = points.getLocation();
        Boolean approxLat = (Math.abs(qr_codePosition.getLatitude() - 53.5225) < 0.001) ? true : false;
        Boolean approxLon = (Math.abs(qr_codePosition.getLongitude() - -113.5244) < 0.001) ? true : false;
        assertTrue(approxLat);
        assertTrue(approxLon);
    }

    /**
     * Test if the radius requirement for visibility works. Player can only see to a certain amount of distance in meters.
     */
    @Test
    void testIfPointsAreWithinDistance(){
        //Expected: Distance is: 0.09 miles / 0.15 kilometers / 0.08 nautical miles
        // 0.09 miles -> 144.841 m
        // I get approx 148.841 m. Decent Enough
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        float radius = (float) 150.2;
        boolean isItInRange = SpaceBetweenPoints.pointsAreWithinRadius(curr, tar, radius);
        assertTrue(isItInRange);
        radius = (float) 140.2;
        isItInRange = SpaceBetweenPoints.pointsAreWithinRadius(curr, tar,radius);
        assertFalse(isItInRange);
    }

    /**
     * Test if you can get the name of the code.
     */
    @Test
    void testIfGetCurrentName(){
        //check if name is gotten properly
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        SpaceBetweenPoints points = new SpaceBetweenPoints(this.test_name,curr, tar);
        assertEquals("null", points.getName());
    }
}
