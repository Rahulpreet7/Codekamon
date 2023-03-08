package com.example.codekamon;


import java.util.Comparator;

public class TotalScoreComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        return Integer.compare(o2.getTotalScore(), o1.getTotalScore());
    } // end compare

} // end PlayerTotalScoreComparator Class
