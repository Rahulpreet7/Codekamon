package com.example.codekamon;

import static org.junit.jupiter.api.Assertions.*;
import static java.util.UUID.randomUUID;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * Unit test for the Player class.
 */
public class PlayerTest {


    /**
     * Tests the Player object constructors.
     */
    @Test
    public void testPlayerConstructor(){
        String playerId = randomUUID().toString();
        Player player = new Player("player1", "player@email.com", playerId);
        assertTrue(player.getUserName().equals("player1"));
        assertTrue(player.getEmail().equals("player@email.com"));
        assertTrue(player.getAndroidId().equals(playerId));
        assertTrue(player.getHighestScore() == 0);
        assertTrue(player.getLowestScore() == -1);
        assertTrue(player.getTotalScore() == 0);
        assertTrue(player.getNumScanned() == 0);
        assertTrue(player.getUserRank() == 0);
        assertTrue(player.getPlayerCodes().isEmpty());
    }


}
