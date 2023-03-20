package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.internal.measurement.zzmt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.Arrays;
import java.util.List;

/**
 * This class "MapsActivity" contains the following:<br>
 * - The location of nearby codes to the player(Red Markers)<br>
 * - The position of the player(Blue Markers)<br>
 * - The location of the nearby codes to the player that are searched (Green Markers)<br>
 *
 * Author(s): Elisandro Cruz Martinez, Ryan Rom <br>
 *
 * Package References:<br>
 * Karumi(2021) Dexter (Version 6.2.3) [Package] https://github.com/Karumi/Dexter <br>
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int radius = 200; //Constant radius value, visibility of the other targets from the player.

    private QRCodesDB codesDB;// Used to get instance of Firebase, get Collections of "QRCodes", and etc.

    private LatLng currentPosition = null; //Stores the location of the current player of type LatLng
    private LocationManager locationManager; // Used to get the current location of the player
    private LocationListener locationListener; // Use to check whether the user has moved a certain amount of distance (meters) in some amount of time (miliseconds).

    private GoogleMap gMap;//Stores map when it is loaded up
    private ArrayList<SpaceBetweenPoints> distancePoints = new ArrayList<>();//Stores the marker of a "SpaceBetweenPoints" instance to draw on Map.
    private ArrayList<MarkerOptions> generalcodes = new ArrayList<>();//Stores the general codes that could either be near the player or for searching.
    private ArrayList<MarkerOptions> redcodes = new ArrayList<>(); // Stores the "redcodes" which are the ones that are near the player.
    private DistanceListViewAdapter distancePointsAdapter; //A adapter that only holds the the red codes which show which ones are near the player

    private ListView listView; // A list contain the current codes and their information.
    //***** Buttons and Search Bar****
    private Button bck;// Used to get out of MapsActivity
    private Button resetCamara; // Used to reset the camara back to player in the map.
    private SearchView search;// Used to search for a code the player would like to see.
    //*** Status ***
    private boolean switchListViewDistance = true; // Use to show which of the two adapters: red (near) or green(searched). To display.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        bck = findViewById(R.id.Back); //get Button to get out of the "MapsActivity".
        search = findViewById(R.id.searchView);//get SearchView to query and find specific Codekamons in the map.
        resetCamara = findViewById(R.id.reset_cam);// get Button to move camara back to Player's location.

        codesDB = new QRCodesDB(FirebaseFirestore.getInstance());//contains the instance of the Database and the "QRCodes" collection reference.

        distancePointsAdapter = new DistanceListViewAdapter(this, distancePoints);//use the adapter that contains the following field.
        listView = findViewById(R.id.list_view_codes);//set up the list view to show the contents.
        listView.setAdapter(distancePointsAdapter);// set up the adapter containing the list view contents.

        // Set Up Functionality To Said:
        // Buttons, SearchView, ListView click listeners, and Updating the QRCodesDB
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Reset Camara Button Click Listener
        resetCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom_plyr();
            }
        });
        // Query Search SearchView
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchedTextCode(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    searchedTextReset_();
                }
                return true;
            }
        });
        // Updating codesDB if any new document is made
        codesDB.getCollectionReference().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                generalcodes.clear();
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    codesDB.getQRCode(doc, new OnCompleteListener<QRCode>() {
                        @Override
                        public void onComplete(QRCode code, boolean success) {
                            MarkerOptions item = new MarkerOptions()
                                    .position(new LatLng(code.getLatitude(), code.getLongitude()))
                                    .title(code.getName());
                            generalcodes.add(item);
                        }
                    });
                }
                distancePointsAdapter.notifyDataSetChanged();
            }
        });
        // Quick click Listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getAdapter().getItem(position);
                GlobalPosition pos = (getListViewStatus()) ? ((SpaceBetweenPoints) obj).getLocation() :
                        ((GeoCodeLocation) obj).getLocation();
                LatLng latLng = new LatLng(pos.getLatitude(), pos.getLongitude());
                move_camara(latLng);
                Toast.makeText(MapsActivity.this, "Long click to see qr code details", Toast.LENGTH_SHORT).show();
            }
        });
        // Long click Listener to see details of QR code.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj = adapterView.getAdapter().getItem(i);
                String name = (switchListViewDistance) ? ((SpaceBetweenPoints) obj).getName() : ((GeoCodeLocation) obj).getName();
                Intent intent = new Intent(MapsActivity.this, QRCodeActivity.class);
                intent.putExtra("QRCode name", name);
                startActivity(intent);
                return true;
            }
        });

        // Start The Google Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    /**
     * The "onMapReady" is overwritten to load up the map, ask for permission to get the user location and update the location if
     * permission has been granted.
     * @param googleMap contains a instance of the google map UI.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // If Map Loaded Succesfully give message
        Toast.makeText(this, "Map loaded!", Toast.LENGTH_SHORT).show();
        // Add current Map to the field
        gMap = googleMap;
        // Get location manager set up so we can get up-to-date location changes.
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // If Location has changed(i.e moved certain amount of meters and some amount of time pass by)
        // Update the current location and the near codes to the Player.
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                gMap.clear();
                addplyr();
                zoom_plyr();
                addCodesNearPlayer();
            }
        };
        // Ask for Location Permission if the user has not been asked yet.
        getLocationPermission();
    }

    /**
     * The method "getLocationPermission" prompts the user with the option to allow/disallow the use of their current location.
     * If permission is granted, than the Map still load up, but it will show the current position of the user.
     */
    private void getLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                // check if permission has truly been granted
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                // set up the location request updates listener
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
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
     * The method "isThisIsTheSameLocation" is used to determine whether the user's current
     * position (i.e Blue Marker) matches one of the markers that symbolises the QR code(i.e Red Marker) . This difference
     * is critical since we would have markers that overlapped (e.g the player and the qr code markers). We would want to avoid this/
     *
     * @param point1
     * @param point2
     * @return returns true or false depending if they are the same marker.
     */
    private boolean isThisIsTheSameLocation(LatLng point1, LatLng point2) {
        // Just in Case the Codekamon and the current location of the user IS the same place.
        // So we do not have to show it.
        return (point1.latitude != point2.latitude) && (point1.longitude != point2.longitude);
    }
    /**
     * The method "addplyr" adds a blue marker to were the current player is located
     */
    public void addplyr() {
        // adds a marker to the position of the player in the map
        if(currentPosition != null) {
            MarkerOptions marker = new MarkerOptions().
                    position(currentPosition).
                    title("You").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            gMap.addMarker(marker);
        }
    }
    /**
     * The method "zoom_plyr" zooms into the location the player is current in.
     */
    public void zoom_plyr() {
        if (currentPosition != null)
            move_camara(currentPosition);
    }
    /**
     * The method "move" zooms in a location given the argument: latlng
     * @param latlng
     */
    private void move_camara(LatLng latlng) {
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
    }
    /**
     * The method "addCodesNearPlayer" samples from the generalcodes which ones are near the player and adds it into
     * the "distancePointsAdapter" private field and also the "redcodes" private field lists.
     */
    private void addCodesNearPlayer() {
        distancePointsAdapter.clear();
        redcodes.clear();
        for (MarkerOptions marker : generalcodes) {
            if (isThisIsTheSameLocation(currentPosition, marker.getPosition())) {
                if (SpaceBetweenPoints.pointsAreWithinRadius(currentPosition, marker.getPosition(), radius)) {
                    // if the player is within radius, not the same location add it to redcodes which are to been shown in the map
                    // of Codekamons that are within range of Player's current location.
                    distancePointsAdapter.add(new SpaceBetweenPoints(marker.getTitle(), currentPosition, marker.getPosition()));
                    redcodes.add(marker);
                    gMap.addMarker(marker);
                }
            }
        }
        distancePointsAdapter.notifyDataSetChanged();
    }
    /**
     * the method "getListViewStatus" returns whether the list showing the redcodes (the ones that shows distance) is to be visible
     * or the ones that had been searched (greencodes/markers).
     * @return
     */
    private boolean getListViewStatus() {
        // Used to know which class type are shown in the ListView: "GeoCodeLocation" or "SpaceBetweenPoints".
        return this.switchListViewDistance;
    }
    /**
     * The method "searchedTextCode" queries given the "newText" that the user typed and entered.
     * Will show the searched codes in the map associated with "green" markers.
     * @param newText
     */
    private void searchedTextCode(String newText) {
        // This will show only the green markers that the Player has searched for.
        gMap.clear();
        addplyr();

        ArrayList<GeoCodeLocation> gL = new ArrayList<>();
        ArrayList<MarkerOptions> greencodes = new ArrayList<>();

        for (MarkerOptions m : generalcodes) {
            if (m.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                GeoCodeLocation sampleCode = new GeoCodeLocation(m.getTitle(), m.getPosition().latitude, m.getPosition().longitude);
                m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                greencodes.add(m);
                gL.add(sampleCode);
            }
        }

        for (MarkerOptions gc : greencodes) {
            gMap.addMarker(gc);
        }
        for (MarkerOptions gc : greencodes) {
            gc.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        GeoCodeViewAdapter qrCodeSearchedAdapter = new GeoCodeViewAdapter(this, gL);
        this.switchListViewDistance = false;
        listView.setAdapter(qrCodeSearchedAdapter);
    }
    /**
     * The method "searchedTextReset_" will switch the adapter to show the red codes.
     * The ones that are near the player only.
     */
    private void searchedTextReset_() {
        // if search is reset it means show the Codekamons near player. Red Codes.
        gMap.clear();
        addplyr();

        for (MarkerOptions rc : redcodes) {
            gMap.addMarker(rc);
        }
        listView.setAdapter(distancePointsAdapter);
        this.switchListViewDistance = true;
    }
}
