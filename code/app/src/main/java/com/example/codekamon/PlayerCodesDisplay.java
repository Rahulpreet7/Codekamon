package com.example.codekamon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to display the QR codes of a player.
 */
public class PlayerCodesDisplay extends AppCompatActivity {

    private ListView userCodes;
    private ArrayList<String> codeNames = new ArrayList<>();
    private ArrayList<QRCode> userCodesList = new ArrayList<>();
    private PlayerCodesAdapter playerCodesAdapter;


    /**
     * Used to display the QR codes of a player.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method, preserves and restores an activity’s UI state in a timely fashion.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_all_qrcodes);
        Integer score = 0;
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
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        playerCodesAdapter = new PlayerCodesAdapter(PlayerCodesDisplay.this, userCodesList);
        playerCodesAdapter.notifyDataSetChanged();
        userCodes.setAdapter(playerCodesAdapter);

        userCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlayerCodesDisplay.this, QRCodeActivity.class);
                intent.putExtra("QRCode name",userCodesList.get(position).getName());
                startActivity(intent);
            }
        });

        if ( player.getAndroidId().equals(deviceId)){

            userCodes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View v,
                                               final int position, long arg3) {
                    androidx.appcompat.app.AlertDialog alertDialog = new AlertDialog.Builder(PlayerCodesDisplay.this)
                            .setTitle("Delete Code?")
                            .setMessage("Are you sure you want to delete this Code?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {


                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    player.deleteQR(userCodesList.get(position));
                                    userCodesList.remove(position);
                                    if (!userCodesList.isEmpty()) {
                                        Integer total_score = 0 ;
                                        for (int j = 0; j < userCodesList.size(); j++){
                                             total_score += userCodesList.get(j).getScore();
                                        }
                                        player.setTotalScore(total_score);
                                        player.setNumScanned(userCodesList.size()-1);
                                        player.setHighestScore(userCodesList.get(0).getScore());
                                        player.setLowestScore(userCodesList.get(userCodesList.size() -1).getScore());
                                        player.updateDatabase();
                                    }
                                    else {
                                        player.setHighestScore(0);
                                        player.setLowestScore(-1);
                                        player.setTotalScore(0);
                                        player.updateDatabase();
                                    }
                                    playerCodesAdapter.notifyDataSetChanged();


                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                    return true;
                }
            });
        }




    }


}}






