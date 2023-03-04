package com.example.codekamon;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * The PlayersDB class is a controller class to ease manipulation of
 * the Players collection in the database.
 */
public class PlayersDB {

    /**
     * Stores the database instance.
     */
    FirebaseFirestore db;

    /**
     * Stores the Players collection reference.
     */
    CollectionReference playersRef;

    /**
     * Creates the instance of PlayersDB.
     */
    public PlayersDB(){
        this.db = FirebaseFirestore.getInstance();
        this.playersRef = db.collection("Players");
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
        playersRef.document(player.getAndroidId()).set(data);
    }
}
