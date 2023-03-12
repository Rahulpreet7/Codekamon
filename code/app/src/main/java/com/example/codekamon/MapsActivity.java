package com.example.codekamon;

import static android.location.LocationManager.GPS_PROVIDER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
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

import org.gavaghan.geodesy.GlobalPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1> This class contains the Google map, the location of nearby codes to the player(Red Markers)
 * and the position of the player(Blue Marker)
 *
 * @author Elisandro Cruz Martinez, Ryan Rom
 *
 * Package References:
 * Karumi(2021) Dexter (Version 6.2.3) [Package] https://github.com/Karumi/Dexter
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * @param radius
     *  Type:Float. Constant radius value, visibility of the other targets from the player.
     * @param currentPosition
     *  Type:Location. Stores the location of the current player of type LatLng
     * @param collectionReference
     *  Type:CollectionReference. Stores the collection that is obtained from firebase firestore database for "Codekamon" project.
     * @param firebase
     *  Type:FirebaseFirestore. Stores the reference to the remote database for the "Codekamon" project.
     * @param gMap
     *  Type:GoogleMap. Stores map when it is loaded up
     * @param qr_code_markers
     *  Type:ArrayList<MarkersOptions>. Stores the marker of targets/QR codes of from the database at that snapshot.
     * @param qr_code_items
     *  Type:ArrayList<DistancePlayerToTarget>. Stores the distance of player to the target and vice versa instance of the DistancePlayerToTarget class.
     * @param adapter
     *  Type:DistanceListViewAdapter. This is used to show the near by QR codes/targets in the database that are within the radius.
     * @param showDatabaseUpdated
     * Type:Boolean. This is used to notify the player if the database markers had change, so they know after 100 m and 3000 mili seconds the map will change.
     */
    private LatLng currentPosition;
    private static final int radius = 200;
    private CollectionReference collectionReference;
    private FirebaseFirestore firebase;
    private GoogleMap gMap;
    private ArrayList<DistancePlayerToTarget> qr_code_items = new ArrayList<>();
    private ArrayList<MarkerOptions> qr_code_markers = new ArrayList<>();
    private DistanceListViewAdapter adapter;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private boolean showDatabaseUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebase = FirebaseFirestore.getInstance();
        collectionReference = firebase.collection("Test_Map");


        adapter = new DistanceListViewAdapter(this, qr_code_items);
        ListView listViewNear;
        listViewNear = findViewById(R.id.listviewNear);
        listViewNear.setAdapter(adapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                qr_code_markers.clear();
                if(showDatabaseUpdated)
                    Toast.makeText(MapsActivity.this, "Change: Updated Codes In List", Toast.LENGTH_SHORT).show();
                showDatabaseUpdated = true;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    MarkerOptions item = new MarkerOptions()
                            .position(new LatLng((Double) doc.getData().get("lati"), (Double) doc.getData().get("long")))
                            .title((String) doc.getData().get("name"));
                    qr_code_markers.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
        listViewNear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DistancePlayerToTarget click_point = (DistancePlayerToTarget) parent.getAdapter().getItem(position);
                GlobalPosition getCodePosition = click_point.getCodePosition();
                move_camara(
                        new LatLng(getCodePosition.getLatitude(), getCodePosition.getLongitude())
                );
            }
        });


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
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                gMap.clear();
                qr_code_items.clear();

                addPlayerMarker();

                Toast.makeText(MapsActivity.this, "Location Changed!", Toast.LENGTH_SHORT).show();

                for (MarkerOptions marker : qr_code_markers) {
                    if(isThisIsTheSameLocation(currentPosition, marker.getPosition())){
                        if(DistancePlayerToTarget.pointsAreWithinRadius(currentPosition, marker.getPosition(), radius)){
                            qr_code_items.add(
                                    new DistancePlayerToTarget(marker.getTitle(), currentPosition, marker.getPosition())
                            );
                            gMap.addMarker(marker);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };

        getLocationPermission();
    }

    /**
     * The method "getLocationPermission" prompts the user with the option to allow/disallow the use of their current location.
     * If permission is granted, than the Map will show the current position of the user.
     **/

    private void getLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 100, locationListener);


                // This is called when we first get the location. Never called again
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                currentPosition = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                addPlayerMarker();
                move_camara(currentPosition);
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {}
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    /**
     * The method "isThisIsTheSameLocation" is used to determine whether the user's current
     * position (i.e Blue Marker) matches one of the markers that symbolises the QR code(i.e Red Marker) . This difference
     * is critical since we would have markers that overlapped (e.g the player and the qr code markers). We would want to avoid this/
     * @param point1
     * @param point2
     * @return
     */
    private boolean isThisIsTheSameLocation(LatLng point1, LatLng point2){
        return (point1.latitude != point2.latitude) && (point1.longitude != point2.longitude);
    }
    /**
     * The method "findPlauerMaker" adds a marker to the current location of the player.
     */
    public void addPlayerMarker() {
        MarkerOptions marker = new MarkerOptions().
                position(currentPosition).
                title("You").
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        gMap.addMarker(marker);
        move_camara(marker.getPosition());
    }
    /**
     * The method "move" zooms in at a marker or player location based on a zoom of 16 and a LatLng of that area being zoomed in.
     * @param latlng
     */
    private void move_camara(LatLng latlng) {
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
    }
}