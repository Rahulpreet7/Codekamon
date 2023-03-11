package com.example.codekamon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class QRCode {

    private double latitude;
    private double longitude;
    private String name;
    private int score;
    private String content;
    //private Bitmap photoSurrounding;
    private String photoAsBytes = "";

    public QRCode(String content)
    {
        this.name = "default";
        this.content = content;
        this.score = calcScore();
    }

    public QRCode(String name, String content)
    {
        this.name = name;
        this.content = content;
        this.score = calcScore();
    }
    public String getPhotoAsBytes()
    {
        return photoAsBytes;
    }
    public void setPhotoAsBytes(String photoAsBytes)
    {
        this.photoAsBytes = photoAsBytes;
    }
    //public void setPhotoSurrounding(Bitmap _bitmap)
    //{
        //this.photoSurrounding = _bitmap;
    //}
    public String getName(){return this.name;}

    public Bitmap getPhotoSurrounding() {
        Bitmap returner = BitmapFactory.decodeByteArray(photoAsBytes.getBytes(), 0, photoAsBytes.getBytes().length);
        return returner;
        //return photoSurrounding;
    }

    public void setLocation(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return this.latitude;
    }
    public double getLongitude()
    {
        return this.longitude;
    }

    public String getContent()
    {
        return content;
    }

    public int getScore()
    {
        return score;
    }

    private int calcScore()
    {

        int return_score = 0;
        int repeat = 0;
        if (content.length()==1){
            return_score = 1;
        }
        else{
            for(int i = 1; i < content.length(); i++){
                if(content.charAt(i) == content.charAt(i-1)){
                    repeat++;

                    if(i == content.length()-1){
                        if(content.charAt(i) == '0'){
                            return_score += Math.pow(20,repeat);
                        }
                        else if('1' <= content.charAt(i) && content.charAt(i) <= '9'){
                            return_score += Math.pow(Character.getNumericValue(content.charAt(i)), repeat);
                        }
                        else if('a' <= content.charAt(i) && content.charAt(i) <= 'f'){
                            return_score += Math.pow(content.charAt(i) + 10 - 'a', repeat);
                        }
                    }

                }
                else if(repeat > 0){
                    if(content.charAt(i-1) == '0'){
                        return_score += Math.pow(20,repeat);
                    }
                    else if('1' <= content.charAt(i-1) && content.charAt(i-1) <= '9'){
                        return_score += Math.pow(Character.getNumericValue(content.charAt(i-1)), repeat);
                    }
                    else if('a' <= content.charAt(i-1) && content.charAt(i-1) <= 'f'){
                        return_score += Math.pow(content.charAt(i - 1) + 10 - 'a', repeat);
                    }
                    repeat = 0;


                    if(i == content.length()-1){
                        if(content.charAt(i) == '0'){
                            return_score += Math.pow(20,repeat);
                        }
                        else if('1' <= content.charAt(i) && content.charAt(i) <= '9'){
                            return_score += Math.pow(Character.getNumericValue(content.charAt(i)), repeat);
                        }
                        else if('a' <= content.charAt(i) && content.charAt(i) <= 'f'){
                            return_score += Math.pow(content.charAt(i) + 10 - 'a', repeat);
                        }
                    }


                }
            }
        }
        return return_score;



    }
}


