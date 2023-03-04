package com.example.codekamon;

import java.util.ArrayList;

public class Player {
    String userName;
    String email;
    ArrayList<QRCode> playerCodes;
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
}
