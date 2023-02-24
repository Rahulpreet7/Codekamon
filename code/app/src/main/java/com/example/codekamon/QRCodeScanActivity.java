package com.example.codekamon;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.checkerframework.checker.nullness.qual.Nullable;


public class QRCodeScanActivity extends AppCompatActivity {
    public static final String DEVICE_ID = "com.example.codekamon.DEVICE_ID";
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    int stage = 0;
    TextView showScoreText;
    TextView caughtText;
    TextView nameText;
    Button stage_one_button;



    public QRCodeScanActivity() throws NoSuchAlgorithmException {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.show_score);
        super.onCreate(savedInstanceState);
        stage = 0;
        showScoreText = findViewById(R.id.show_score_text);
        stage_one_button = findViewById(R.id.stage_one_button);
        Toast.makeText(QRCodeScanActivity.this, "Clicked 'Your Codes!'", Toast.LENGTH_SHORT).show();


        getSupportActionBar().hide();
        showScoreText.setText("Points: " + 1);
/*
        IntentIntegrator intentIntegrator = new IntentIntegrator(QRCodeScanActivity.this);
        intentIntegrator.setPrompt("Scan a QR code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();


 */







        //ImageView yourCodes = findViewById(R.id.your_codes_icon);
        /*
        yourCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(QRCodeScanActivity.this, "Clicked 'Your Codes!'", Toast.LENGTH_SHORT).show();
            }
        });

         */


        stage_one_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(QRCodeScanActivity.this, "stage 1 finished!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QRCodeScanActivity.this, photoTakingActivity.class);
                //intent.putExtra(DEVICE_ID, androidId);
                startActivity(intent);
            }
        });







    }

    //received help from https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        QRCodeScanActivity.super.onActivityResult(requestCode,resultCode,data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            Bundle bundle = data.getExtras();
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Scan Cancelled", Toast.LENGTH_SHORT).show();

            } else {

                //Toast.makeText(getBaseContext(),intentResult.getRawBytes().toString(), Toast.LENGTH_SHORT).show();
                byte[] encrypted = md.digest(intentResult.getRawBytes());

                StringBuilder sb = new StringBuilder();
                for(byte b : encrypted)
                {
                    sb.append(String.format("%02x", b));
                }
                Toast.makeText(getBaseContext(),sb, Toast.LENGTH_SHORT).show();
                QRCode scannedResult = new QRCode(sb.toString());
                stage ++;
                showScoreText.setText("Points: " + scannedResult.getScore());





                //messageText.setText(intentResult.getContents());
                //messageFormat.setText(intentResult.getFormatName());
            }
        }
    }
}

