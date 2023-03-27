package com.example.codekamon;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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



    /**
     * Creates the instance of PlayersDB.
     * @param firestore this contains a argument that holds the database.
     */

    public PlayersDB(FirebaseFirestore firestore) {
        this.db = firestore;
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        this.collectionReference = db.collection("Players");

    }

    public PlayersDB(FirebaseFirestore firestore, boolean simple){
        this.db = firestore;
        this.collectionReference = db.collection("Players");
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    /**
     * Adds a player to the 'Players' collection in the database.
     *
     * @param player The player to be added to the database.
     * @param listener
     */
    public void addPlayer(Player player, OnCompleteListener<Player> listener) {
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
        collectionReference
                .document(player.getAndroidId())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listener.onComplete(player, true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onComplete(null, false);
                    }
                });
    }

    /**
     * Gets the player from the database.
     *
     * @param deviceId The device id of the player
     * @param listener The listener to handle the result
     */
    public void getPlayer(String deviceId, OnCompleteListener<Player> listener) {

        collectionReference.document(deviceId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        this.getPlayer(snapshot, listener);
                    } else {
                        listener.onComplete(null, false);
                    }
                })
                .addOnFailureListener(failure -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Gets the player from the database.
     * @param context The context of the application
     * @param listener The listener to handle the result
     */
    public void getPlayer(Context context, OnCompleteListener<Player> listener){
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        getPlayer(deviceId, new OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                listener.onComplete(item, success);
            }
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
        player.setUserRankSimple(playerRank);
        listener.onComplete(player, true);
    }

    public void deletePlayer(String id, OnCompleteListener<Player> listener){
        collectionReference
                .document(id)
                .delete()
                .addOnSuccessListener(unused -> {
                    listener.onComplete(null, true);
                })
                .addOnFailureListener(e -> {
                    listener.onComplete(null, false);
                });
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

