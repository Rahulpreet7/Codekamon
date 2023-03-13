package com.example.codekamon;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import androidx.annotation.NonNull;

import com.example.codekamon.DistancePlayerToTarget;
import com.google.android.gms.maps.model.LatLng;

import org.gavaghan.geodesy.GlobalPosition;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.List;

/**
 @@ -28,78 +30,81 @@
 */

public class DistancePlayerToTargetTest {
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

    @Test
    void checkParameters(){
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();

        assertEquals(53.5232,curr.latitude);
        assertEquals(-113.5263,curr.longitude);

        assertEquals(53.5225, tar.latitude);
        assertEquals(-113.5244, tar.longitude);

    }
    @Test
    void testDistanceBetweenPoints(){
        //Expected: Distance is: 0.09 miles / 0.15 kilometers / 0.08 nautical miles
        // 0.09 miles -> 144.841 m
        // I get approx 148.841 m. Decent Enough

        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        DistancePlayerToTarget points = new DistancePlayerToTarget(this.test_name,curr, tar);
        double dis = points.get_distance();
        boolean approximatelyEqual = (Math.abs(144.841 - dis) < 5) ? true : false;
        assertTrue(approximatelyEqual);
    }
    @Test
    void testIfCoordinatesOfQRcodeWorks(){
        /*
            We already know the coordinates of the player.
            We want to know if we can properly retrieve that of the QR code near it.
         */
        DecimalFormat df = new DecimalFormat("#.####");
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        DistancePlayerToTarget points = new DistancePlayerToTarget(this.test_name,curr, tar);
        GlobalPosition qr_codePosition = points.getCodePosition();
        Boolean approxLat = (Math.abs(qr_codePosition.getLatitude() - 53.5225) < 0.001) ? true : false;
        Boolean approxLon = (Math.abs(qr_codePosition.getLongitude() - -113.5244) < 0.001) ? true : false;
        assertTrue(approxLat);
        assertTrue(approxLon);
    }
    @Test
    void testIfPointsAreWithinDistance(){
        //Expected: Distance is: 0.09 miles / 0.15 kilometers / 0.08 nautical miles
        // 0.09 miles -> 144.841 m
        // I get approx 148.841 m. Decent Enough
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        float radius = (float) 150.2;
        boolean isItInRange = DistancePlayerToTarget.pointsAreWithinRadius(curr, tar, radius);
        assertTrue(isItInRange);
        radius = (float) 140.2;
        isItInRange = DistancePlayerToTarget.pointsAreWithinRadius(curr, tar,radius);
        assertFalse(isItInRange);
    }
    @Test
    void testIfGetCurrentName(){
        //check if name is gotten properly
        LatLng curr = mock_current_test();
        LatLng tar = mock_target_test();
        DistancePlayerToTarget points = new DistancePlayerToTarget(this.test_name,curr, tar);
        assertEquals("null", points.get_name());
    }
}
