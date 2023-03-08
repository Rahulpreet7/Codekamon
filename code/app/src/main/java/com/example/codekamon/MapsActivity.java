package com.example.codekamon;

import static android.location.LocationManager.GPS_PROVIDER;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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
     * @param fineLocation
     *  Type:String. Constant ACESS_fineLocation. Used to not write this all the time
     * @param courseLocation
     *  Type:String. Constant ACESS_COARSE_LOCATION Stores the diesel constant value. This is final.
     * @param permissionRequestCode
     *  Type:Integer. Constant permissionRequestCode. Value 101 used to keep a permission value
     * @param DEFAULT_ZOOM
     *  Type:Float. Constant zoom value in order to zoom in to either the player or target marker in the map.
     * @param radius
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
     *  Type:DistanceListViewAdapter. This is used to show the near by QR codes/targets in the database that are within the radius.
     */
    private static final String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String courseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int permissionRequestCode = 101;
    private static final float radius = (float) 50.2;

    private CollectionReference collectionReference;
    private FirebaseFirestore firebase;
    private GoogleMap gMap;
    private Boolean first_time = true;
    private Boolean mLocationPermissionGranted = false;
    private ArrayList<DistancePlayerToTarget> targetsMarkers = new ArrayList<>();
    private DistanceListViewAdapter adapter;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebase = FirebaseFirestore.getInstance();
        collectionReference = firebase.collection("Test_Map");


        adapter = new DistanceListViewAdapter(this, targetsMarkers);
        ListView markersList = findViewById(R.id.listviewNear);
        markersList.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this, "Map loaded!", Toast.LENGTH_SHORT).show();
        gMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                gMap.clear();
                findPlayerMarker();
                moveCamara(currentLocation);

            }
        };
        askLocationPermission();
    }

    private void askLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                currentLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                findPlayerMarker();
                moveCamara(currentLocation);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    /**
     * The method "findPlauerMaker" adds a marker to the current location of the player.
     */
    public void findPlayerMarker() {
        MarkerOptions marker = new MarkerOptions().position(currentLocation).title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        gMap.addMarker(marker);
    }
    /**
     * The method "moveCamara" zooms in at a marker or player location based on the DEFUALT_ZOOM and LngLon
     */
    private void moveCamara(LatLng latlng) {
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
    }
}