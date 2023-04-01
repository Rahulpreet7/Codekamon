package com.example.codekamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class handles post-scanning process.
 */
public class photoTakingActivity extends AppCompatActivity {
    /**
     *  used to get the current location of the player taking photo
     */
    private LocationManager locationManager; // Used to get the current location of the player
    /**
     *  use to check whether the player has moved from current location
     */
    private LocationListener locationListener; // Use to check whether the user has moved a certain amount of distance (meters) in some amount of time (miliseconds).
    /**
     *  the current location of player
     */
    private LatLng currentLocation = null;
    /**
     * the button for yes responses.
     */
    private Button yes_button;
    /**
     * the button for no responses.
     */
    private Button no_button;
    /**
     * the text showing interactions.
     */
    private TextView query_text;
    //for test only
    private ImageView photo_show;
    /**
     * stores the current stage
     */
    int stage = 0;
    /**
     * the current database
     */
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**
     * stores the value to upload to DB
     */
    final CollectionReference collectionReference = db.collection("QRCodes");
    //final CollectionReference collectionReference2 = db.collection("Images");


    QRCode passedResult;

    /**
     * onCreate is method is called when the activity is created and sets
     * the views and activities when clicked.
     *
     * @param savedInstanceState The saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        stage = 0;
        super.onCreate(savedInstanceState);

        // ---Location Permission Request-----
        // get location permission from user
        getLocationPermission();
        // get current location every 0 mili seconds and for every 0 m
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // update location when listener catches it. This is synchronous while you are getting code
        locationListener = location -> currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //------------------------------------

        setContentView(R.layout.photo_taking);
        Intent intent = getIntent();
        passedResult = new QRCode(intent.getStringExtra("Name"), intent.getStringExtra("sb"));
        passedResult.setVisualImage(intent.getStringExtra("visual"));

        //passedResult.setVisualImage("---");
        yes_button = findViewById(R.id.yes_button);
        no_button = findViewById(R.id.no_button);
        photo_show = findViewById(R.id.photo_show);
        query_text = findViewById(R.id.query_text);
        getSupportActionBar().hide();

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stage == 0) {
                    Toast.makeText(photoTakingActivity.this, "going to take photos...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (stage == 1) {
                    Toast.makeText(photoTakingActivity.this, "recording location...", Toast.LENGTH_SHORT).show();
                    double lati = (currentLocation != null) ? currentLocation.latitude : 0.0;
                    double longi = (currentLocation != null) ? currentLocation.longitude: 0.0;
                    passedResult.setLocation(lati, longi);
                    stage++;
                    onStageChange();
                }
                else if(stage == 2)
                {
                    //upload to db
                    HashMap<String, QRCode> data = new HashMap<>();
                    data.put("QRCode content: ", passedResult);
                    collectionReference
                            .document(passedResult.getName())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(photoTakingActivity.this, "store success!", Toast.LENGTH_SHORT).show();
                                    // These are a method which gets executed when the task is succeeded
                                    //Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(photoTakingActivity.this, "store failed!", Toast.LENGTH_SHORT).show();
                                    // These are a method which gets executed if there’s any problem
                                    //Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });
                    //back to main
                    Intent intent = new Intent(photoTakingActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent);
                    finish();

                }
            }
        });
        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stage == 0)
                {
                    Toast.makeText(photoTakingActivity.this, "skip taking photos...", Toast.LENGTH_SHORT).show();
                }
                else if(stage == 1)
                {
                    Toast.makeText(photoTakingActivity.this, "skip recording location...", Toast.LENGTH_SHORT).show();
                    //setContentView(R.layout.show_score);
                }
                stage ++;
                onStageChange();
            }
        });
    }

    /**
     * onAcitvity result is called when an activity is called and executed.
     *
     * @param requestCode identify who this result came from
     * @param resultCode identify the child activity through its setResult()
     * @param data intent returns to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoTakingActivity.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            String s = bitmapToString(imageBitmap);
            //photo_show.setImageBitmap(imageBitmap);


            //how to change string back to bitmap, in order to show the image.
            //byte[] imageBytes = Base64.decode(s, Base64.DEFAULT);
            //Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            //photo_show.setImageBitmap(bitmap);


            //photo_show.setImageBitmap(passedResult.getPhotoSurrounding());

            passedResult.setPhotoAsBytes(s);
            //WIP

            //System.out.println(s == null);
            /*
            YuvImage yuvimage = new YuvImage(s.getBytes(), ImageFormat.NV21, 600, 600, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvimage.compressToJpeg(new Rect(0,0,20,20), 80, baos);
            byte[] jdata = baos.toByteArray();
            Bitmap s3 = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);

            //can't convert
            //Bitmap s2 = BitmapFactory.decodeByteArray(s.getBytes(),0, s.getBytes().length);
            System.out.println(s3 == null);
            //System.out.println(bitmapToString(s2).substring(10));
            //System.out.println("-----------------");
            System.out.println(bitmapToString(s3).substring(10));
            photo_show.setImageBitmap(s3);
            //photo_show.setImageBitmap(imageBitmap);

            //this will lead to err.
            //passedResult.setPhotoSurrounding(imageBitmap);

             */


            stage ++;
            onStageChange();
            //setPic(photo_show);
        }
    }

    protected void onStageChange() {
        if(stage == 1)
        {
            query_text.setText("Record the geolocation of the code?");
        }
        if(stage == 2)
        {
            query_text.setText("You earned " + passedResult.getScore() + " points!");
            no_button.setVisibility(View.INVISIBLE);
            yes_button.setText("Return");

            String deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
            playersDB.getPlayer(deviceId, new OnCompleteListener<Player>() {
                @Override
                public void onComplete(Player item, boolean success) {
                    item.addQR(passedResult);

                }
            });

        }
    }

    /**
     * turn the bitmap of image to string, in order to upload to DB.
     *
     * @param imageBitmap: the image in bitmap.
     * @return String holding bitmap information.
     */
    protected String bitmapToString(Bitmap imageBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String s = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return s;

    }
    /**
     * "getLocationPermission" requests the user to allow the use of their location
     */
    private void getLocationPermission() {
        // get location permission
        Dexter.withActivity(this).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                // check if permission has truly been granted
                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {}
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {permissionToken.continuePermissionRequest();}
        }).check();
    }
}
