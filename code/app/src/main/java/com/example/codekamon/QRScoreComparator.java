package com.example.codekamon;

import java.util.Comparator;

public class QRScoreComparator  implements Comparator<QRCode> {



    @Override
    public int compare(QRCode o1, QRCode o2) {
        return Integer.compare(o2.getScore(), o1.getScore());
    }
} // end PlayerTotalScoreComparator Class{

