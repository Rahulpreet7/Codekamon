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

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TextView messageText;
        //TextView messageFormat;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

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
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();

                Toast.makeText(MainActivity.this, "Clicked 'add code'", Toast.LENGTH_SHORT).show();

            }


            protected void onAcitivtyResult(int requestCode, int resultCode, @Nullable Intent data)
            {
                MainActivity.super.onActivityResult(requestCode,resultCode,data);
                IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                // if the intentResult is null then
                // toast a message as "cancelled"
                if (intentResult != null) {
                    if (intentResult.getContents() == null) {
                        Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        // if the intentResult is not null we'll set
                        // the content and format of scan message
                        Toast.makeText(getBaseContext(),intentResult.getContents() , Toast.LENGTH_SHORT).show();
                        //messageText.setText(intentResult.getContents());
                        //messageFormat.setText(intentResult.getFormatName());
                    }
                }
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
}