package com.example.codekamon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * This class represents the QR code scanned and its relative information.
 */
public class QRCode {

    /**
     * Stores the latitude value.
     */
    private double latitude;
    /**
     * Stores the longitude value.
     */
    private double longitude;
    /**
     * Stores the QRcode name.
     */
    private String name;
    /**
     * Stores the score based on hash value.
     */
    private int score;
    /**
     * Stores the hash value of the code.
     */
    private String content;
    /**
     * Stores the image taken converted to string.
     */
    private String photoAsBytes = "";
    /**
     * Stores the image drawn by characters
     */
    private String visualImage = "";


    /**
     * constructor
     *
     * @param content: hash value of QR code
     */
    public QRCode(String content)
    {
        this.name = "default";
        this.content = content;
        this.score = calcScore();
    }



    /**
     * constructor
     *
     * @param name: name of QR code
     * @param content: hash value of QR code
     */
    public QRCode(String name, String content)
    {
        this.name = name;
        this.content = content;
        this.score = calcScore();
    }

    /**
     * get the bitmap back from any String stands for photoAsBytes
     *
     * @param s: photoAsBytes
     * @return bitmap
     */
    public Bitmap getImageAsBitmap(String s)
    {
        byte[] imageBytes = Base64.decode(s, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    /**
     * get the bitmap back from photoAsBytes
     *
     * @return bitmap
     */
    public Bitmap getImageAsBitmap()
    {
        String s = photoAsBytes;
        byte[] imageBytes = Base64.decode(s, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }




    /**
     * Visual Image getter
     *
     * @return visualImage
     */
    public String getVisualImage(){return visualImage;}
    /**
     * Visual Image setter
     *
     * @param  visualImage
     */
    public void setVisualImage(String visualImage){this.visualImage = visualImage;}

    /**
     * photoAsBytes getter
     *
     * @return photoAsbytes
     */
    public String getPhotoAsBytes()
    {
        return photoAsBytes;
    }

    /**
     * photoAsBytes setter
     *
     * @param photoAsBytes
     */
    public void setPhotoAsBytes(String photoAsBytes)
    {
        this.photoAsBytes = photoAsBytes;
    }


    /**
     * name getter
     *
     * @return name
     */
    public String getName(){return this.name;}


    /**
     * set the location of QR code
     *
     * @param latitude: latitude of QR code scanned
     * @param longitude: longitude of QR code scanned
     */
    public void setLocation(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    /**
     * Latitude getter
     *
     * @return latitude
     */
    public double getLatitude()
    {
        return this.latitude;
    }
    /**
     * Latitude getter
     *
     * @return longitude
     */
    public double getLongitude()
    {
        return this.longitude;
    }

    /**
     * content getter
     *
     * @return content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * score getter
     *
     * @return score
     */
    public int getScore()
    {
        return score;
    }


    /**
     * calculate the score based on the hash value
     *
     * @return return_score
     */
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


