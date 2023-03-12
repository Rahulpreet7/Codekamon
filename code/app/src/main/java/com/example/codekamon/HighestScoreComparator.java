package com.example.codekamon;

import java.util.Comparator;

public class HighestScoreComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        return Integer.compare(o2.getHighestScore(), o1.getHighestScore());
    } // end compare

} // end PlayerSingleScoreComparator Class
