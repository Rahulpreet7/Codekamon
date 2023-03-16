package com.example.codekamon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

/**
 * This class is used to display the leader board for the game.
 */
public class leaderBoard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView playerRank;
    private TextView playerScore;
    private ListView rankingList;
    private Spinner sortbyOption;
    private ArrayList<Player> playerArrayList = new ArrayList<>();
    private totalScoreRankArrayAdapter totalScoreRankArrayAdapter;
    private highestScoreRankArrayAdapter highestScoreRankArrayAdapter;

    /**
     * It creates the leader board activity.
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        playerRank = findViewById(R.id.playerRank);
        playerScore = findViewById(R.id.playerScore);
        rankingList = findViewById(R.id.rankingList);

        sortbyOption = findViewById(R.id.sortbyOption);
        sortbyOption.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sorty_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sortbyOption.setAdapter(adapter);

    }

    /**
     * It retrieves the object from a given position and then leads to the user profile activity.
     * @param parent View.
     * @param view View.
     * @param position The integer value of the of the item selected from the list.
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");

        String current_option = (String) parent.getItemAtPosition(position);
        if (current_option.equals("Total Score")){

            collectionReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                playerArrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Player player = document.toObject(Player.class);
                                    PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
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

                            String deviceId = Settings.Secure.getString(leaderBoard.this.getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
                            playersDB.getPlayer(deviceId, new com.example.codekamon.OnCompleteListener<Player>() {
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
        else if (current_option.equals("Highest Scoring QR code")){

            collectionReference
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                playerArrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Player player = document.toObject(Player.class);
                                    PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
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

                            highestScoreRankArrayAdapter = new highestScoreRankArrayAdapter(leaderBoard.this,playerArrayList);
                            Collections.sort(playerArrayList, new HighestScoreComparator());
                            totalScoreRankArrayAdapter.notifyDataSetChanged();

                            rankingList.setAdapter(highestScoreRankArrayAdapter);

                            String deviceId = Settings.Secure.getString(leaderBoard.this.getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
                            playersDB.getPlayer(deviceId, new com.example.codekamon.OnCompleteListener<Player>() {
                                @Override
                                public void onComplete(Player item, boolean success) {

                                    playerScore.setText("Score: " + item.getHighestScore().toString());
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

    /**
     * It is a callback method to be invoked when the selection disappears from this view.
     * @param parent View.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}