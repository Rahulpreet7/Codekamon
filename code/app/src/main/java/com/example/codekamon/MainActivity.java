package com.example.codekamon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}