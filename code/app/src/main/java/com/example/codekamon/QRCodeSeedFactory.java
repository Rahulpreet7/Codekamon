package com.example.codekamon;

import android.widget.Toast;

/**
 * This class represents a factory of QR codes' name and visual character drawing,
 * by the information in QR code as seed.
 */
public class QRCodeSeedFactory {

    /**
     * Stores the bytes in QR code as seed.
     */
    private String seed;

    /**
     * Stores name segments corresponding to bit value 0.
     */
    private String[] lowNames = {"cool ", "Fro", "Mo", "Mega", "Spectral", "Crab"};

    /**
     * Stores name segments corresponding to bit value 1.
     */
    private String[] highNames = {"Hot ", "Glo", "Lo", "Ultra", "Sonic", "Shark"};


    /**
     * constructor
     *
     * @param rawBytes: bytes of QR code
     */
    public QRCodeSeedFactory(String rawBytes)
    {
        this.seed = rawBytes;
    }

    /**
     * generate the visual image by seed.
     *
     * @return generatedImage
     */
    public String generateImage()
    {
        String content = seed;
        String[] output= new String[9];
        output[0] ="_____\r\n";
        output[1] ="/         \\\r\n";
        output[2] ="  |__   __|\r\n";
        output[3] =" \\| _   _ |/\r\n";
        output[4] =" @|  | |  |@\r\n";
        output[5] =" /|  ,`  ` , |\\\r\n";
        output[6] ="|           |\r\n";
        output[7] ="| `---` |\r\n";
        output[8] ="\\_____/";

        if(content.charAt(0) == '1'){
            output[3] ="\\|@      @|/\r\n";
        }
        if(content.charAt(1) == '1'){
            output[2] ="|           |\r\n";
        }
        if(content.charAt(2) == '1'){
            output[0] ="_______\r\n";
            output[1] ="|       |\r\n";
            output[7] ="|       |\r\n";
            output[8] ="|_______|";

        }
        if(content.charAt(3) == '1'){
            output[4] ="@|            |@\r\n";
        }
        if(content.charAt(4) == '1'){
            output[6] ="|  ___  |\r\n";
            output[7] ="| /   \\ |\r\n";
        }
        if(content.charAt(5) == '1'){
            output[3] ="  |  _  _ | \r\n";
            output[4] ="  |   ||  | \r\n";
            output[5] ="  |  ,``, | \r\n";
        }


        String generatedImage = "";
        for (int i = 0; i < output.length; i++) {
            generatedImage += output[i];
        }
        //String generatedImage = "---";
        return generatedImage;
    }

    /**
     * generate the visual image by seed.
     *
     * @return generatedName
     */
    public String generateName()
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < lowNames.length; i ++)
        {
            if(seed.charAt(i) == '0')
            {
                sb.append(lowNames[i]);
            }
            else if (seed.charAt(i) == '1')
            {
                sb.append(highNames[i]);
            }
            else
            {

            }
        }
        String generatedName = sb.toString();
        return generatedName;
    }

}
