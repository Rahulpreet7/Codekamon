package com.example.codekamon;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class photoTakingActivity extends AppCompatActivity {

    private Button yes_button;
    private Button no_button;
    private TextView query_text;
    private ImageView photo_show;
    int stage = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("QRCodes");
    final CollectionReference collectionReference2 = db.collection("Images");


    QRCode passedResult;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {





        stage = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_taking);
        Intent intent = getIntent();
        passedResult = new QRCode(intent.getStringExtra("Name"), intent.getStringExtra("sb"));
        yes_button = findViewById(R.id.yes_button);
        no_button = findViewById(R.id.no_button);
        photo_show = findViewById(R.id.photo_show);
        query_text = findViewById(R.id.query_text);

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stage == 0) {
                    Toast.makeText(photoTakingActivity.this, "going to take photos...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                else if(stage == 1)
                {
                    Toast.makeText(photoTakingActivity.this, "recording location...", Toast.LENGTH_SHORT).show();
                    //fixme: record location
                    double lati = 0;
                    double longi = 0;
                    passedResult.setLocation(lati, longi);
                    stage ++;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoTakingActivity.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            String s = bitmapToString(imageBitmap);
            //photo_show.setImageBitmap(imageBitmap);
            //photo_show.setImageBitmap(passedResult.getPhotoSurrounding());

            passedResult.setPhotoAsBytes(s);
            //WIP
            /*
            System.out.println(s == null);

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

    protected void onStageChange()
    {
        if(stage == 1)
        {
            query_text.setText("Record the geolocation of the code?");
        }
        if(stage == 2)
        {
            query_text.setText("You earned " + passedResult.getScore() + " points!");
            no_button.setVisibility(View.INVISIBLE);
            yes_button.setText("Return");

            PlayersDB playersDB = new PlayersDB();
            playersDB.getPlayer(this, new OnCompleteListener<Player>() {
                @Override
                public void onComplete(Player item, boolean success) {
                    item.addQR(passedResult);

                }
            });

        }
    }

    protected String bitmapToString(Bitmap imageBitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String s = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return s;

    }
/*
    private void setPic(ImageView imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }


 */


}
