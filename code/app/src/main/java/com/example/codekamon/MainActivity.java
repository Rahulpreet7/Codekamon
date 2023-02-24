package com.example.codekamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.zxing.activity.CodeUtils;

import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    //public static final String DEVICE_ID = "com.example.codekamon.DEVICE_ID";
    //String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


    MessageDigest md = MessageDigest.getInstance("SHA-256");
    TextView showScoreText;

    public MainActivity() throws NoSuchAlgorithmException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //for algorithm testing
        //QRCode code = new QRCode("bb999ee55abc");
        //int a = code.getScore();
        //Toast.makeText(MainActivity.this, Integer.toString(a), Toast.LENGTH_SHORT).show();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        showScoreText = findViewById(R.id.show_name_text);
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
                Intent intent = new Intent(MainActivity.this, QRCodeScanActivity.class);
                //intent.putExtra(DEVICE_ID, androidId);
                startActivity(intent);



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