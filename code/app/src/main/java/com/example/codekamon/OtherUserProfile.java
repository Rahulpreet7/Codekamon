package com.example.codekamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OtherUserProfile extends AppCompatActivity {

    private TextView OtherUserName;
    private TextView OtherUserEmail;
    private TextView OtherUserScore;
    private TextView OtherUserCodes;
    private String userName;
    private String userEmail;
    private String userScore;
    private String userNumCodes;
    private ImageView Qrcodes;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_user_profile);
        OtherUserName = findViewById(R.id.OtherUserName);
        OtherUserEmail = findViewById(R.id.OtherUserEmail);
        OtherUserScore = findViewById(R.id.OtherUserScore);
        OtherUserCodes = findViewById(R.id.OtherUserCodes);
        Qrcodes = findViewById(R.id.QRCodeImage);
        Button backButton = findViewById(R.id.backButton2);


        Intent intent = getIntent();
        Player player = (Player) intent.getSerializableExtra("PLAYER");

        userName = player.getUserName();
        userEmail = player.getEmail();
        userScore = player.getTotalScore().toString();
        userNumCodes = player.getNumScanned().toString();

        OtherUserName.setText("Name: " + userName);
        OtherUserEmail.setText("Email: "+ userEmail);
        OtherUserScore.setText("Total Score: " + userScore);
        OtherUserCodes.setText("Total Codes Scanned: " + userNumCodes);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Qrcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OtherUserProfile.this, PlayerCodesDisplay.class);
                intent1.putExtra("PLAYER", player);
                startActivity(intent1);
            }
        });


    }






}