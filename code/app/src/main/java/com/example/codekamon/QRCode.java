package com.example.codekamon;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the QR code scanned and its
 * relative information.
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

    private ArrayList<HashMap<String,String>> comments = new ArrayList<>();

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
        this.score = this.calcScore();
    }

    /**
     * Adds a comment to the qr code.
     *
     * @param comment The comment to the qr code.
     */
    public void addComment(Comment comment){
        HashMap<String,String> map = new HashMap<>();
        map.put("playerName", comment.getPlayerName());
        map.put("comment", comment.getComment());
        comments.add(0, map);
    }

    public void setComments(ArrayList<HashMap<String,String>> comments){
        this.comments = comments;
    }

    public ArrayList<HashMap<String, String>> getComments(){
        return comments;
    }


    /**
     * Photo bitmap getter
     *
     * @return photoAsBytes
     */
    public String getPhotoAsBytes()
    {
        return photoAsBytes;
    }

    /**
     * Photo bitmap setter
     * @param photoAsBytes set the photo bytes.
     *
     */
    public void setPhotoAsBytes(String photoAsBytes)
    {
        this.photoAsBytes = photoAsBytes;
    }


    /**
     * QRCode Name setter
     * @param name
     */
    public void setName(String name){this.name = name;}

    /**
     * QRCode Name getter
     *
     * @return name
     */
    public String getName(){return this.name;}
    /**
     * QRCode Name setter
     * @param content
     */
    public void setContent(String content) {this.content = content;}
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
     * score setter
     * @param score
     */
    public void setScore(int score){this.score = score;}
    /**
     * calculate the score based on the hash value
     *
     * @return return_score
     */
    public int calcScore()
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


