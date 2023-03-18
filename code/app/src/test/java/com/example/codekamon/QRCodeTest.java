package com.example.codekamon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class QRCodeTest {
    @Test
    public void testScoreCalculation(){
        QRCode code = new QRCode("name", "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(code.getScore(), 111);
    }
}
