package com.example.codekamon;

import java.util.ArrayList;
import java.util.Arrays;

public class customPlayerRankList {

    private ArrayList<Player> playerArrayList;

    public customPlayerRankList(ArrayList<Player> playerArrayList) {
        this.playerArrayList = playerArrayList;
    }

    public customPlayerRankList(){
        this.playerArrayList = new ArrayList<>();
        Player[] players = {};
        playerArrayList.addAll(Arrays.asList(players));

    }

    public ArrayList<Player> getPlayerArrayList() {
        return playerArrayList;
    }

    public void setPlayerArrayList(ArrayList<Player> playerArrayList) {
        this.playerArrayList = playerArrayList;
    }
}
