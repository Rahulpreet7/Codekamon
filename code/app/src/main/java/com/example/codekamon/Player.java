package com.example.codekamon;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a player and has details about a player
 * such as username, email, and etc.
 */
public class Player implements Serializable {

    /**
     * Holds the username of the player.
     */
    private String userName;

    /**
     * Holds the email of the player.
     */
    private String email;

    /**
     * Holds the codes scanned by the player.
     */
    private HashMap<String,String> playerCodes;

    /**
     * Holds the highest score of the player.
     */
    private Integer highestScore;

    /**
     * Holds the lowest score of the player.
     */
    private Integer lowestScore;

    /**
     * Holds the total score of the player.
     */
    private Integer totalScore;

    /**
     * Holds the number of codes scanned by the player.
     */
    private Integer numScanned;

    /**
     * Holds the android id of the player.
     */
    private String androidId;

    /**
     * Holds the rank of the user
     */
    private Integer userRank;



    public Player() {

    }


    /**
     * Creates a player object which represents a player with details about the player.
     *
     * @param userName The username of the player
     * @param email The email of the player
     * @param androidId The android id of the player
     */
    public Player(String userName, String email, String androidId) {
        this.userName = userName;
        this.email = email;
        this.androidId = androidId;
        highestScore = 0;
        lowestScore = -1;
        totalScore = 0;
        numScanned = 0;
        userRank = 0;
        playerCodes = new HashMap<>();
    }

    /**
     * Adds the specified qr code to the player object.
     *
     * @param code The QR code to be added
     * @return true (Qr code added successfully) or false ( Qr code could not be added because the player has already scanned this code )
     */
    public Boolean addQR(QRCode code){
        String name = code.getName();
        String id = code.getContent();
        int score = code.getScore();
        if(playerCodes.containsValue(id)){
            return false;
        }

        if(score > highestScore){
            highestScore = score;
        }
        if(score < lowestScore || lowestScore == -1){
            lowestScore = score;
        }
        totalScore += score;
        playerCodes.put(name, id);
        numScanned++;
        updateDatabase();
        return true;
    }

    /**
     * This updates the database with the data contained in the current player object
     */
    public void updateDatabase() {
        // Create Firestore collection
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Players");
        DocumentReference myAccountRef = collectionReference.document(androidId);

        myAccountRef
                .update("ScannedCodes", playerCodes);
        myAccountRef
                .update("Number Of Codes Scanned", numScanned);
        myAccountRef
                .update("Total Score", totalScore);
        myAccountRef
                .update("Highest Score", highestScore);
        myAccountRef
                .update("Lowest Score", lowestScore);
        myAccountRef
                .update("Player Ranking", userRank);

    } //

    public void updateRanking(){
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference =
                db.collection("Players");
        DocumentReference myAccountRef = collectionReference.document(androidId);

        myAccountRef
                .update("Player Ranking", userRank);
    }

    /**
     * Deletes the specified qr code from the player object.
     *
     * @param code The QR code to be deleted
     * @return true (Qr code deleted successfully) or false ( Qr code could not be deleted because the player have not scanned this code )
     */
    public Boolean deleteQR(QRCode code){
        String name = code.getName();
        if(!playerCodes.containsKey(name)){
            return false;
        }
        // TODO: change highest/lowest score if this is that code
        totalScore -= code.getScore();
        playerCodes.remove(name);
        numScanned--;
        return true;
    }

    /**
     * Gets the username of the player.
     *
     * @return The username of the player
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the player
     *
     * @param userName The username of the player
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the email of the player.
     *
     * @return The email of the player.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the player.
     *
     * @param email The email of the player
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the highest score of the player.
     *
     * @return highestScore
     */
    public Integer getHighestScore() {
        return highestScore;
    }

    /**
     * Sets the highest score of the player.
     * @param highestScore - the new highest score to be set.
     */
    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
    }

    /**
     * Gets the lowest score of the player.
     *
     * @return lowestScore
     */
    public Integer getLowestScore() {
        return lowestScore;
    }

    /**
     * Sets the lowest score of the player.
     * @param lowestScore - the newest lowest score to be set.
     */
    public void setLowestScore(Integer lowestScore) {
        this.lowestScore = lowestScore;
    }

    /**
     * Gets the total score of the player.
     *
     * @return totalScore
     */
    public Integer getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the total score of the player.
     * @param totalScore - the new total score needed to be set.
     */
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Gets the number of codes scanned by the player.
     *
     * @return numScanned
     */
    public Integer getNumScanned() {
        return numScanned;
    }

    /**
     * Sets the number of codes scanned by the player.
     * @param numScanned number to be scanned.
     */
    public void setNumScanned(Integer numScanned) {
        this.numScanned = numScanned;
    }

    /**
     * Gets the android id of the player
     * @return androidId
     */
    public String getAndroidId() {
        return androidId;
    }

    /**
     * Sets the android id of the player
     */
    public void setAndroidId(String androidId){
        this.androidId = androidId;
    }

    /**
     * Gets the QR codes scanned by the player
     *
     * @return playerCodes
     */
    public HashMap<String, String> getPlayerCodes() {
        return playerCodes;
    }

    /**
     * Sets the QR codes scanned by the player
     *
     * @param playerCodes The QR codes scanned by the player
     */
    public void setPlayerCodes(HashMap<String, String> playerCodes) {
        this.playerCodes = playerCodes;
    }

    /**
     * Gets the ranking of the player
     *
     * @return  The ranking of the player in Integer form
     */
    public Integer getUserRank() {
        return userRank;
    }

    /**
     * Sets the ranking of the player
     *
     * @param userRank The ranking of the player
     */
    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
        updateRanking();

    }

    /**
     * Sets the ranking of the player.
     *
     * @param userRank The ranking of the player
     */
    public void setUserRankSimple(Integer userRank){
        this.userRank = userRank;
    }


}
