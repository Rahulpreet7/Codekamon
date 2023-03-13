package com.example.codekamon;


import java.util.Comparator;

/**
 * This class is used to compare the total scores of the players.
 */
public class TotalScoreComparator implements Comparator<Player> {

    /**
     * Specific implementation of the compare method.
     * @param o1 Player 1 object.
     * @param o2 Player 2 object.
     * @return It returns the value zero if total scores of the players are equal,
     * It returns a value less than zero if total score of the first player is less than the total score of the second player.
     * It returns a value greater than zero if total score of the first player is more than the total score of the second player.
     */
    @Override
    public int compare(Player o1, Player o2) {
        return Integer.compare(o2.getTotalScore(), o1.getTotalScore());
    } // end compare

} // end PlayerTotalScoreComparator Class
