package com.example.codekamon;

/**
 * This class represents a comment.
 */
public class Comment {

    /**
     * Holds the name of player who made the comment.
     */
    private String playerName;

    /**
     * Holds the comment of a player.
     */
    private String comment;

    /**
     * Creates a comment object.
     *
     * @param playerName The name of the player who made the comment.
     * @param comment The comment of the player.
     */
    public Comment(String playerName, String comment){
        this.playerName = playerName;
        this.comment = comment;
    }

    /**
     * Gets the comment.
     * @return The comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     * @param comment has the comment that the player wants to set.
     */
    public void setComment(String comment){
        this.comment = comment;
    }

    /**
     * Gets the name of the player who made the comment.
     * @return the player's name.
     */
    public String getPlayerName(){
        return playerName;
    }

    /**
     * Sets the name of the player who made the comment.
     * @param playerName the name of the player to be set.
     */
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
}
