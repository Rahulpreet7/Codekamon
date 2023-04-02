package com.example.codekamon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;


public class PlayerCodesDisplay extends AppCompatActivity {

    private ListView userCodes;
    private ArrayList<String> codeNames = new ArrayList<>();
    private ArrayList<QRCode> userCodesList = new ArrayList<>();
    private PlayerCodesAdapter playerCodesAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_qrcodes);

        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        QRCodesDB qrCodesDB = new QRCodesDB(FirebaseFirestore.getInstance());


        userCodes = findViewById(R.id.userCodes);
        Button backButton = findViewById(R.id.backButton2);

        Intent intent = getIntent();
        Player player = (Player) intent.getSerializableExtra("PLAYER");


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        for (String key : player.getPlayerCodes().keySet()) {
            codeNames.add(key);
        }

        for (int i = 0; i < codeNames.size(); i++){

            qrCodesDB.getCollectionReference().document(codeNames.get(i))
                    .addSnapshotListener( new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            qrCodesDB.getQRCode(value, new OnCompleteListener<QRCode>() {
                                @Override
                                public void onComplete(QRCode item, boolean success) {
                                    userCodesList.add(item);
                                    if (userCodesList.size() >= 2){
                                        Collections.sort(userCodesList, new QRScoreComparator());

                                    }
                                }
                            });
                        }
                    });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        playerCodesAdapter = new PlayerCodesAdapter(PlayerCodesDisplay.this, userCodesList);
        playerCodesAdapter.notifyDataSetChanged();
        userCodes.setAdapter(playerCodesAdapter);

        userCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlayerCodesDisplay.this, CommentsActivity.class);
                intent.putExtra("QRCode",userCodesList.get(position).getName());
                startActivity(intent);
            }
        });




    }


}}






