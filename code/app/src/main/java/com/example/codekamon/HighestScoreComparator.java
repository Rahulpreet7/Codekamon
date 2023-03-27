package com.example.codekamon;

import java.util.Comparator;

/**
 * This class is used to compare the highest scores of the players.
 */
public class HighestScoreComparator implements Comparator<Player> {

    /**
     * Specific implementation of the compare method.
     * @param o1 Player 1 object.
     * @param o2 Player 2 object.
     * @return It returns the value zero if highest scores of the players are equal,
     * It returns a value less than zero if highest score of the first player is less than the highest score of the second player.
     * It returns a value greater than zero if highest score of the first player is more than the highest score of the second player.
     */
    @Override
    public int compare(Player o1, Player o2) {
        return Integer.compare(o2.getHighestScore(), o1.getHighestScore());
    } // end compare

} // end PlayerSingleScoreComparator Class
