package com.example.codekamon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * This class represents a factory of converting the string PhotoAsBytes to bitmap.
 */
public class bitmapFactory {
    
    private String photoAsBytes;

    /**
     * constructor
     *
     * @param photoAsBytes: string representation of the photo
     */
    public bitmapFactory(String photoAsBytes)
    {
        this.photoAsBytes = photoAsBytes;
    }

    /**
     * generate the image bitmap by photoAsBytes.
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

}
