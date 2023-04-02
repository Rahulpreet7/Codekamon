package com.example.codekamon;

import java.util.Comparator;

/**
 * This class is used to compare the scores of the QR codes.
 */
public class QRScoreComparator  implements Comparator<QRCode> {


    /**
     * Specific implementation of the compare method.
     * @param o1 QRCode object 1
     * @param o2 QRCode object 2
     * @return It returns the value zero if scores of the QR codes are equal,
     * It returns a value less than zero if score of the first QR code is less than the score of the second QR code.
     * It returns a value greater than zero if score of the first QR code is more than the score of the second QR code.
     */
    @Override
    public int compare(QRCode o1, QRCode o2) {
        return Integer.compare(o2.getScore(), o1.getScore());
    }
} // end QRScoreComparator Class{

