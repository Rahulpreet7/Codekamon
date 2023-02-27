package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.example.codekamon.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    FirebaseFirestore db;
    GoogleMap googleMa;
    double latitude;
    double longitude;

    private ArrayList<MarkerOptions> markers = new ArrayList<>();
    DistancePlayerViewAdapter adapter;
    private ArrayList<DistancePlayerCode> codes = new ArrayList<>();
    private static int REQUEST_CODE = 101;
    private ActivityMapsBinding binding;
    final float visibility = (float) 0.01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // binding = ActivityMapsBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        setContentView(R.layout.activity_maps);

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Test_Map");

        adapter = new DistancePlayerViewAdapter(this, codes);
        ListView markersList = findViewById(R.id.listviewNear);
        markersList.setAdapter(adapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                assert queryDocumentSnapshots != null;
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    // Add the New One
                    LatLng latlng = new LatLng((Double) doc.getData().get("lati"), (Double) doc.getData().get("long"));
                    MarkerOptions i = new MarkerOptions().position(latlng).title((String) doc.getData().get("name"));
                    markers.add(i);
                }
            }
        });

        getLocation();


    }

    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMa = googleMap;
        //googleMa.clear();
        displayPlayerLocation();
        //Populate The Map With QR Codes that are in the Database
        //if(markers != null){
          //  for(int i = 0; i < markers.size(); i++) {
            //    if (in_visibility(markers.get(i))) {
              //      codes.add(new DistancePlayerCode(markers.get(i),currentLocation));
                //    googleMa.addMarker(markers.get(i));
                //}
            //}
       // }
        //adapter.notifyDataSetChanged();
    }
    public void displayPlayerLocation() {
        try {
            Log.d("Daf", "Here");
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            LatLng latlng = new LatLng(latitude, longitude);

            MarkerOptions playerMarker =
                    new MarkerOptions().position(latlng).title("You").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            Toast.makeText(getApplicationContext(), "Latitude: " + latitude + " Longitude: " + longitude, Toast.LENGTH_LONG).show();

            googleMa.animateCamera(CameraUpdateFactory.newLatLng(latlng));
            googleMa.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
            googleMa.addMarker(playerMarker);
            googleMa.getUiSettings().setZoomControlsEnabled(true);
            googleMa.getUiSettings().setCompassEnabled(true);

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), (CharSequence) e, Toast.LENGTH_LONG).show();
        }
    }

    private boolean in_visibility(MarkerOptions m){
        // Only the markers that are within the
        // visibility of vision from the player's current position
        double mLat = m.getPosition().latitude;
        double mLong = m.getPosition().longitude;
        return (latitude - visibility <= mLat && mLat <= latitude + visibility) &&
                (longitude - visibility <= mLong && mLong <= longitude + visibility);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
                Log.d("GetLOcaca", "Here");

            }
        }
    }
}