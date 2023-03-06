package com.example.codekamon;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Serializable {
    String userName;
    String email;
    HashMap<String,String> playerCodes;
    Integer highestScore;
    Integer lowestScore;
    Integer totalScore;
    Integer numScanned;
    String androidId;

    public Player(String userName, String email, String androidId) {
        this.userName = userName;
        this.email = email;
        this.androidId = androidId;
        highestScore = 0;
        lowestScore = 0;
        totalScore = 0;
        numScanned = 0;
    }

    public Boolean addQR(QRCode code) {
        String name = code.getName();
        String id = code.getContent();
        int score = code.getScore();
        if (playerCodes.containsValue(id)) {
            return false;
        }

        if (score > highestScore) {
            highestScore = score;
        }
        if (score < lowestScore || lowestScore == -1) {
            lowestScore = score;
        }
        totalScore += score;
        playerCodes.put(name, id);
        numScanned++;
        return true;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
    }

    public Integer getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(Integer lowestScore) {
        this.lowestScore = lowestScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getNumScanned() {
        return numScanned;
    }

    public void setNumScanned(Integer numScanned) {
        this.numScanned = numScanned;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId){
        this.androidId = androidId;
    }

    public HashMap<String, String> getPlayerCodes() {
        return playerCodes;
    }

    public void setPlayerCodes(HashMap<String, String> playerCodes) {
        this.playerCodes = playerCodes;
    }
}
