package com.example.codekamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.*;
//import com.google.zxing.activity.CodeUtils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;


    MessageDigest md = MessageDigest.getInstance("SHA-256");
    TextView showScoreText;

    public MainActivity() throws NoSuchAlgorithmException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //for algorithm testing
        QRCode code = new QRCode("8a976a83da98f48f0b9f0");
        int a = code.getScore();
        Toast.makeText(MainActivity.this, Integer.toString(a), Toast.LENGTH_SHORT).show();

        //TextView messageText;
        //TextView messageFormat;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        showScoreText = findViewById(R.id.show_score_text);
        ImageView map = findViewById(R.id.map_icon);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked 'map' ", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView scan = findViewById(R.id.add_code_icon);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a QR code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();

                Toast.makeText(MainActivity.this, "Clicked 'add code'", Toast.LENGTH_SHORT).show();


            }


        });

        ImageView leaderboards = findViewById(R.id.leaderboards_icon);
        leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked the 'leaderboards'", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView yourCodes = findViewById(R.id.your_codes_icon);
        yourCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked 'Your Codes'", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView otherPlayers = findViewById(R.id.other_player_icon);
        otherPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked 'Other Players'", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //received help from https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        MainActivity.super.onActivityResult(requestCode,resultCode,data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            Bundle bundle = data.getExtras();
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();

            } else {

                //Toast.makeText(getBaseContext(),intentResult.getRawBytes().toString(), Toast.LENGTH_SHORT).show();
                byte[] encrypted = md.digest(intentResult.getRawBytes());

                StringBuilder sb = new StringBuilder();
                for(byte b : encrypted)
                {
                    sb.append(String.format("%02x", b));
                }
                Toast.makeText(getBaseContext(),sb, Toast.LENGTH_SHORT).show();
                QRCode scannedResult = new QRCode(sb.toString());
                showScoreText.setText("Points Earned: " + scannedResult.getScore());





                //messageText.setText(intentResult.getContents());
                //messageFormat.setText(intentResult.getFormatName());
            }
        }
    }
}