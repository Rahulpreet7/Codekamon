package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DescriptorProtos;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final float DEFAULT_ZOOM = 16;
    private Location currentLocation;
    //private FusedLocationProviderClient fusedClient;
    private FirebaseFirestore firebase;
    private GoogleMap gMap;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ArrayList<MarkerOptions> targetCoordinates = new ArrayList<>();
    private ArrayList<DistancePlayerToTarget> targetsMarkers = new ArrayList<>();
    private DistanceListViewAdapter adapter;
    private CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebase = FirebaseFirestore.getInstance();
        collectionReference = firebase.collection("Test_Map");

        adapter = new DistanceListViewAdapter(this, targetsMarkers);
        ListView markersList = findViewById(R.id.listviewNear);
        markersList.setAdapter(adapter);

        getLocationPermission();
    }
    private void getTargetsToGoogleMap(){
        //Set Up The Adapter Here
        Log.d(TAG, ""+ currentLocation);
        Log.d(TAG, (gMap != null) ? "True" : "False");

        targetsMarkers.clear();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                targetCoordinates.clear();
                targetsMarkers.clear();

                Toast.makeText(MapsActivity.this, "database has changed. Updating...", Toast.LENGTH_SHORT).show();
                assert queryDocumentSnapshots != null;
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    // Add new items in the Test_Map
                    if(doc != null) {
                        Object lat = doc.getData().get("lati");
                        Object lon = doc.getData().get("long");
                        LatLng latlng = new LatLng( lat !=null ? (Double) lat : 0, lon != null ? (Double) lon : 0);
                        MarkerOptions i = new MarkerOptions().position(latlng).title((String) doc.getData().get("name"));
                        targetCoordinates.add(i);
                    }
                }

                for(int i = 0; i < targetCoordinates.size(); i++){
                    MarkerOptions m = targetCoordinates.get(i);

                    targetsMarkers.add(
                            new DistancePlayerToTarget(
                                    m.getTitle(),
                                    m.getPosition(),
                                    currentLocation
                            ));
                    gMap.addMarker(m);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMapp) {
        Toast.makeText(this, "Success: Map loaded!", Toast.LENGTH_LONG).show();
        gMap = gMapp;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
    }
    private void moveCamaraToCurrentLocation(LatLng latlng, float zoom){
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
        // add extra options to manipulate map
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
    }
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }
    public void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                Task<Location> t = mFusedLocationProviderClient.getLastLocation();
                t.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            currentLocation = location;
                            moveCamaraToCurrentLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            getTargetsToGoogleMap();
                        }
                    }
                });

            }

        }catch (SecurityException s){
            Toast.makeText(this, "ERROR: current location not found", Toast.LENGTH_LONG).show();
        }
    }
    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions
                        ,PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions,
                    PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                    //init map
                }
            }
        }
    }

}