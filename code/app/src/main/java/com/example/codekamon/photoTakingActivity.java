package com.example.codekamon;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.checkerframework.checker.nullness.qual.Nullable;

public class photoTakingActivity extends AppCompatActivity {

    private Button yes_button;
    private Button no_button;
    private TextView query_text;
    private ImageView photo_show;
    int stage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        stage = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_taking);
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
                    //record location
                    Intent intent = new Intent(photoTakingActivity.this, QRCodeScanActivity.class);
                    startActivity(intent);
                    setContentView(R.layout.show_score);
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
                    setContentView(R.layout.show_score);

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
            photo_show.setImageBitmap(imageBitmap);
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
            //query_text.setText();

        }
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
