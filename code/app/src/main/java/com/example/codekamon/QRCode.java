package com.example.codekamon;

public class QRCode {

    private double latitude;
    private double longitude;

    private int score;
    private String content;

    public QRCode(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    private int calcScore()
    {
        for(int i = 0; i < content.length(); i++)
        {


        }
        return 0;
    }
}


