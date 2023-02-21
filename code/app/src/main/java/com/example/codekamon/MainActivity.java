package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
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

        Intent intent = getIntent();

        //deviceId -> Needs to be passed to other activities that need the users details
        String deviceId = intent.getStringExtra(SignUpActivity.DEVICE_ID);

        firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("Players");
        collectionReference.document(deviceId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TextView username = findViewById(R.id.username_text);
                username.setText(task.getResult().get("Username").toString());
            }
        });
    }
}