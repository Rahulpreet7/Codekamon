package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.Code;

/**
 * This class handles the logic of the main screen.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * onCreate is method is called when the activity is created and sets
     * the icons to start different activities when clicked.
     *
     * @param savedInstanceState The saved instance state of the activity
     */


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
                Intent intent = new Intent(MainActivity.this, QRCodeScanActivity.class);
                //intent.putExtra(DEVICE_ID, androidId);
                startActivity(intent);
            }
        });

        ImageView leaderboards = findViewById(R.id.leaderboards_icon);
        leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked the 'leaderboards'", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, leaderBoard.class);
                startActivity(intent);
            }
        });

        ImageView yourCodes = findViewById(R.id.your_codes_icon);
        yourCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ImageView otherPlayers = findViewById(R.id.other_player_icon);
        otherPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked 'Other Players'", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, otherPlayersActivity.class);
                startActivity(intent);
            }
        });

        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
        playersDB.getPlayer(deviceId, new com.example.codekamon.OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                TextView username = findViewById(R.id.username_text);
                username.setText(item.getUserName());
            }
        });
    }
}