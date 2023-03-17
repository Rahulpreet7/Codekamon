package com.example.codekamon;

public class Comment {
    private String playerName;
    private String comment;

    public Comment(String playerName, String comment){
        this.playerName = playerName;
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
}
