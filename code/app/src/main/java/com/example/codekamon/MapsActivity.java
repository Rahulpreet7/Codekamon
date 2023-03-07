package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <h1>This class contains the Google map, the location of the codes, and the player
 * @author Elisandro Cruz Martinez, Ryan Rom
 *
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * The Class "Viewing" obtains the data the user wants to add/edit to his/her records!.
     * @param FINE_LOCATION
     *  Type:String. Constant ACESS_FINE_LOCATION. Used to not write this all the time
     * @param COURSE_LOCATION
     *  Type:String. Constant ACESS_COARSE_LOCATION Stores the diesel constant value. This is final.
     * @param PERMISSION_REQUEST_CODE
     *  Type:Integer. Constant PERMISSION_REQUEST_CODE. Value 101 used to keep a permission value
     * @param DEFAULT_ZOOM
     *  Type:Float. Constant zoom value in order to zoom in to either the player or target marker in the map.
     * @param RADIUS
     *  Type:Float. Constant radius value, visibility of the other targets from the player.
     * @param currentLocation
     *  Type:Location. Stores the location of the current player of type LatLng
     * @param collectionReference
     *  Type:CollectionReference. Stores the collection that is obtained from firebase firestore database for "Codekamon" project.
     * @param firebase
     *  Type:FirebaseFirestore. Stores the reference to the remote database for the "Codekamon" project.
     * @param gMap
     *  Type:GoogleMap. Stores map when it is loaded up
     * @param mLocationPermissionGranted
     *  Type:Boolean. Stores a true/false value if user allowed access to the player's current location
     * @param mFusedLocationProviderClient
     *  Type:FusedLocationProviderClient. This is used to obtain the current location if all of the permissions are past
     * @param targetsMakers
     *  Type:ArrayList. Stores the targets/QR codes of from the database
     * @param adapter
     *  Type:DistanceListViewAdapter. This is used to show the near by QR codes/targets in the database that are within the RADIUS.
     */
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final float DEFAULT_ZOOM = 16;
    private static final float RADIUS = (float) 50.2;

    private LatLng currentLocation;
    private CollectionReference collectionReference;
    private FirebaseFirestore firebase;
    private GoogleMap gMap;
    private Boolean firstTimeStartingUp = true;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ArrayList<DistancePlayerToTarget> targetsMarkers = new ArrayList<>();
    private DistanceListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebase = FirebaseFirestore.getInstance();
        collectionReference = firebase.collection("Test_Map");

        adapter = new DistanceListViewAdapter(this, targetsMarkers);
        ListView markersList = findViewById(R.id.listviewNear);
        markersList.setAdapter(adapter);

        markersList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (gMap != null) {
                DistancePlayerToTarget t = (DistancePlayerToTarget) parent.getAdapter().getItem(position);
                moveCamaraToCurrentLocation(t.getTarget());
            }
        });

        getLocationPermission();
    }
    /**
     * The method "getTargetsToGoogleMap" checks if thd database has gotten new Codekamons appearing in the map
     * , it will only allow to show and display the codekamons that are within the RADIUS of visibility of the player
     */
    private void getTargetsToGoogleMap() {
        collectionReference.addSnapshotListener((QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException error) -> {
            targetsMarkers.clear();
            gMap.clear();
            findPlayerMarker();

            if(firstTimeStartingUp) {
                Toast.makeText(MapsActivity.this, "Db Updated. Refresh map to see changes!", Toast.LENGTH_SHORT).show();
                firstTimeStartingUp = false;
            }

            assert queryDocumentSnapshots != null;
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                if (doc.getData().get("lati") != null && doc.getData().get("long") != null) {
                    double lat = (double) doc.getData().get("lati"), lon = (double) doc.getData().get("long");
                    LatLng targetLatlng = new LatLng(lat, lon);

                    if (DistancePlayerToTarget.isDistanceInRadius(currentLocation, targetLatlng, RADIUS)) {
                        if(doc.getData().get("name") != null) {
                            MarkerOptions i = new MarkerOptions().position(targetLatlng).title((String) doc.getData().get("name"));
                            targetsMarkers.add(
                                    new DistancePlayerToTarget(
                                            (String) doc.getData().get("name"), targetLatlng, currentLocation
                                    ));
                            gMap.addMarker(i);
                        }
                    }
                }
            }

            Collections.sort(targetsMarkers);
            adapter.notifyDataSetChanged();
        });

    }
    /**
     * The method "findPlauerMaker" adds a marker to the current location of the player.
     */
    public void findPlayerMarker(){
        MarkerOptions marker = new MarkerOptions().position(currentLocation).title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        gMap.addMarker(marker);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap gMapp) {
        Toast.makeText(this, "Success: Map loaded!", Toast.LENGTH_SHORT).show();
        gMap = gMapp;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            gMap.getUiSettings().setZoomControlsEnabled(true);
            gMap.getUiSettings().setCompassEnabled(true);
        }
    }


    /**
     * The method "moveCamaraToCurrentLocation" zooms in at a marker or player location based on the DEFUALT_ZOOM and LngLon
     */
    private void moveCamaraToCurrentLocation(LatLng latlng){
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, MapsActivity.DEFAULT_ZOOM));
    }

    /**
     * The method "initMap" this is used to add the map to the fragment "map" inside the "activity_maps" layout.
     */
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(MapsActivity.this);
    }
    /**
     * The method "getDeviceLocation" checks the permission to access the location and than zooms in to the location of the player
     */
    public void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                Task<Location> t = mFusedLocationProviderClient.getLastLocation();

                t.addOnSuccessListener((Location location) -> {
                    if(location != null){
                        currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        Toast.makeText(this, ""+currentLocation.latitude + "," + currentLocation.longitude, Toast.LENGTH_SHORT).show();

                        findPlayerMarker();

                        moveCamaraToCurrentLocation(currentLocation);
                        getTargetsToGoogleMap();
                    }
                });
            }

        }catch (SecurityException s){
            Toast.makeText(this, "ERROR: current location not found", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * The method "getLocationPermission" checks and obtains the permission to access the location for the device this app is running
     */
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
    /**
     * The method "onRequestPermissionsResult" checks whether the app has permission by the user ot access specific information.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                for (int grantResult : grantResults)
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;
                        return;
                    }
                mLocationPermissionGranted = true;
                initMap();
            }
        }
    }

}