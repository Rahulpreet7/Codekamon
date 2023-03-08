package com.example.codekamon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class leaderBoard extends AppCompatActivity {

    private TextView playerRank;
    private TextView playerScore;
    private ListView rankingList;
    private Spinner sortbyOption;
    private ArrayList<Player> playerArrayList = new ArrayList<>();
    private totalScoreRankArrayAdapter totalScoreRankArrayAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        playerRank = findViewById(R.id.playerRank);
        playerScore = findViewById(R.id.playerScore);
        rankingList = findViewById(R.id.rankingList);
        sortbyOption = findViewById(R.id.sortbyOption);



        //Intent intent = getIntent();
        //Bundle bundle = intent.getExtras();
        //String deviceID = bundle.getString("DEVICE_ID");



        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");

        collectionReference
                //.orderBy("totalScore")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            playerArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Player player = document.toObject(Player.class);
                                PlayersDB playersDB = new PlayersDB();
                                playersDB.getPlayer(document, new com.example.codekamon.OnCompleteListener<Player>() {
                                    @Override
                                    public void onComplete(Player item, boolean success) {
                                        playerArrayList.add(item);
                                    }
                                });

                            }// Populate the listview
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        totalScoreRankArrayAdapter = new totalScoreRankArrayAdapter(leaderBoard.this,playerArrayList);
                        Collections.sort(playerArrayList, new TotalScoreComparator());
                        totalScoreRankArrayAdapter.notifyDataSetChanged();

                        rankingList.setAdapter(totalScoreRankArrayAdapter);

                        PlayersDB playersDB = new PlayersDB();
                        playersDB.getPlayer(leaderBoard.this, new com.example.codekamon.OnCompleteListener<Player>() {
                            @Override
                            public void onComplete(Player item, boolean success) {


                                playerScore.setText("Score: " + item.getTotalScore().toString());
                                playerRank.setText("Rank: "+item.getUserRank().toString());

                            }
                        });

                        rankingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(leaderBoard.this, OtherUserProfile.class);

                                intent.putExtra("PLAYER",playerArrayList.get(position));
                                startActivity(intent);
                            }
                        });


                    }
                });




    }


}

