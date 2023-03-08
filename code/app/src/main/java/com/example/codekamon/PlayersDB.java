package com.example.codekamon;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The PlayersDB class is a controller class to ease manipulation of
 * the Players collection in the database.
 */
public class PlayersDB {
    /**
     * Stores the tag for this class for logging purposes.
     */
    public static String TAG = "PlayersDB";

    /**
     * Stores the database instance.
     */
    private FirebaseFirestore db;


    /**
     * Stores the Players collection reference.
     */
    private CollectionReference collectionReference;


    private ArrayList<Player> playerArrayList = new ArrayList<>();


    /**
     * Creates the instance of PlayersDB.
     */

    public PlayersDB() {
        this.db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        this.collectionReference = db.collection("Players");

    }

    /**
     * Adds a player to the 'Players' collection in the database.
     *
     * @param player The player to be added to the database.
     */
    public void addPlayer(Player player) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("Android Id", player.getAndroidId());
        data.put("Username", player.getUserName());
        data.put("Email", player.getEmail());
        data.put("Highest Score", player.getHighestScore());
        data.put("Number Of Codes Scanned", player.getNumScanned());
        data.put("Lowest Score", player.getLowestScore());
        data.put("Total Score", player.getTotalScore());
        data.put("ScannedCodes", player.getPlayerCodes());
        data.put("Player Ranking", player.getUserRank());
        collectionReference.document(player.getAndroidId()).set(data);
    }

    /**
     * Gets the player from the database.
     *
     * @param context  The context of the application
     * @param listener The listener to handle the result
     */
    public void getPlayer(Context context, OnCompleteListener<Player> listener) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        collectionReference.document(deviceId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Log.i(TAG, "Player found .");
                        this.getPlayer(snapshot, listener);
                    } else {
                        Log.i(TAG, "Failed to get Player from database.");
                        listener.onComplete(null, false);
                    }
                })
                .addOnFailureListener(failure -> {
                    Log.i(TAG, "Failed to get Player from database.");
                    listener.onComplete(null, false);
                });
    }

    /**
     * Gets a player from the database.
     *
     * @param snapshot The snapshot of the player to get
     * @param listener The listener to handle the result
     */
    public void getPlayer(DocumentSnapshot snapshot, OnCompleteListener<Player> listener) {
        String username = snapshot.get("Username").toString();
        String email = snapshot.get("Email").toString();
        String androidId = snapshot.get("Android Id").toString();
        Integer highestScore = Integer.parseInt(snapshot.get("Highest Score").toString());
        Integer lowestScore = Integer.parseInt(snapshot.get("Lowest Score").toString());
        Integer numScanned = Integer.parseInt(snapshot.get("Number Of Codes Scanned").toString());
        Integer totalScore = Integer.parseInt(snapshot.get("Total Score").toString());
        Integer playerRank = Integer.parseInt(snapshot.get("Player Ranking").toString());
        HashMap<String, String> qrCodes = (HashMap<String, String>) snapshot.get("ScannedCodes");
        Player player = new Player(username, email, androidId);
        player.setHighestScore(highestScore);
        player.setLowestScore(lowestScore);
        player.setNumScanned(numScanned);
        player.setPlayerCodes(qrCodes);
        player.setTotalScore(totalScore);
        player.setUserRank(playerRank);
        listener.onComplete(player, true);
    }





    /**
     * Gets the Players collection reference.
     *
     * @return The Players collection reference
     */
    public CollectionReference getCollectionReference() {
        return collectionReference;
    }
}

