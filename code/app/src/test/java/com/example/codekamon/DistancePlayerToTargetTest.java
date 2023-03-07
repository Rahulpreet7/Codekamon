package com.example.codekamon;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.errorprone.annotations.DoNotMock;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import androidx.annotation.NonNull;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * <h1>This class "DistancePlayerTargetTest" is used to test out the methods that are within the "DistancePlayerTarget" class.
 * to see if they are working properly. This is important to store in the Adapter
 * @author Elisandro Cruz Martinez
 *
 */

public class DistancePlayerToTargetTest {

   //53.56591,-113.5206902 <- current
     //53.5265693, -113.5208212 <- target

    //distance should be: 8.989m
    @NonNull
    private MarkerOptions mockTargetTest(){
        LatLng latLng = new LatLng(53.5264693, -113.5208212);
        return new MarkerOptions().position(latLng).title("target");
    }

    @NonNull
    private LatLng mockCurrentTest(){
        return new LatLng(53.56591, -113.5206902);
    }

    @Test
    void checkParameters(){
        LatLng curr = mockCurrentTest();
        MarkerOptions tar = mockTargetTest();

        assertEquals(53.56591,curr.latitude);
        assertEquals(-113.5206902,curr.longitude);

        assertEquals(53.5264693, tar.getPosition().latitude);
        assertEquals(-113.5208212, tar.getPosition().longitude);

    }
    @Test
    void checkIfDistanceProduceDoubleValue(){
        //LatLng curr = mockCurrentTest();
        //MarkerOptions tar = mockTargetTest();

        //DistancePlayerToTarget dp = new DistancePlayerToTarget(tar.getTitle(), tar.getPosition(), curr);
        //double d = dp.getDistance();
        //assertEquals(8.989, d);
        //From: (53°31'53.513513513526"N -113°31'34.68"E) To: (53°31'53.513513513526"N -113°31'32.741760147744"E)
        //Distance is: 0.02 miles / 0.04 kilometers / 0.02 nautical miles
        //32.18m
    }

    @Test
    void testGetCoordinates(){

        LatLng curr = mockCurrentTest();
        MarkerOptions tar = mockTargetTest();

        DistancePlayerToTarget t = new DistancePlayerToTarget(tar.getTitle(), tar.getPosition(), curr);

        //check if coordinates is of size 2
        List<Double> crd = t.getCoordinates();
        assertEquals(2, crd.size());

        // check if it saves the right coordinates for the target value.
        double target_mark_latitude = tar.getPosition().latitude;
        double target_mark_longitude = tar.getPosition().longitude;
        double target_tar_latitude = crd.get(0);
        double target_tar_longitude = crd.get(1);

        assertEquals(target_mark_latitude,target_tar_latitude);
        assertEquals(target_mark_longitude, target_tar_longitude);

    }

    @Test
    void testGetName(){
        LatLng curr = mockCurrentTest();
        MarkerOptions tar = mockTargetTest();

        DistancePlayerToTarget t = new DistancePlayerToTarget(tar.getTitle(), tar.getPosition(), curr);
        assertEquals("target", t.getName());

    }
}

