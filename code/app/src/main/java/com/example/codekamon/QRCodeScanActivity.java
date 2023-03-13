package com.example.codekamon;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * This class handles the logic of scanning process.
 */
public class QRCodeScanActivity extends AppCompatActivity {

    public static final String DEVICE_ID = "com.example.codekamon.DEVICE_ID";
    /**
     * the digest rule to convert QRcode to bytes.
     */
    private MessageDigest md = MessageDigest.getInstance("SHA-256");
    /**
     * the text showing scores.
     */
    private TextView showScoreText;
    /**
     * the text frame for input name.
     */
    private TextView nameText;
    /**
     * the button for proceed.
     */
    private Button stage_one_button;
    /**
     * stores the scanned result as QRcode.
     */
    private static QRCode scannedResult;
    /**
     * stores the built up raw bytes.
     */
    private StringBuilder sb = new StringBuilder();


    public QRCodeScanActivity() throws NoSuchAlgorithmException {
    }
    /**
     * onCreate is method is called when the activity is created and sets
     * the views and activities when clicked.
     *
     * @param savedInstanceState The saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.show_score);
        super.onCreate(savedInstanceState);

        showScoreText = findViewById(R.id.show_score_text);
        stage_one_button = findViewById(R.id.stage_one_button);
        nameText = findViewById(R.id.naming_textframe);
        Toast.makeText(QRCodeScanActivity.this, "Clicked 'Your Codes!'", Toast.LENGTH_SHORT).show();


        getSupportActionBar().hide();
        showScoreText.setText("Points: " + 1);

        //if test, command this.

        IntentIntegrator intentIntegrator = new IntentIntegrator(QRCodeScanActivity.this);
        //intentIntegrator.setPrompt("Scan a QR code");
        //intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();



        stage_one_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(QRCodeScanActivity.this, "stage 1 finished!", Toast.LENGTH_SHORT).show();
                scannedResult = new QRCode(nameText.getText().toString(), sb.toString());

                Intent intent = new Intent(QRCodeScanActivity.this, photoTakingActivity.class);
                //can't simplified
                intent.putExtra("Name", nameText.getText().toString());
                intent.putExtra("sb", sb.toString());
                //for testing
                //intent.putExtra("Name", "abcd");
                //intent.putExtra("sb", "sb");
                startActivity(intent);


            }
        });







    }


    /**
     * onAcitvity result is called when an activity is called and executed.
     *
     * @param requestCode identify who this result came from
     * @param resultCode identify the child activity through its setResult()
     * @param data intent returns to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        QRCodeScanActivity.super.onActivityResult(requestCode,resultCode,data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        // if the intentResult is null then "cancelled"
        if (intentResult != null) {
            System.out.println(intentResult);
            if(data == null)
            {
                Intent intent = new Intent(QRCodeScanActivity.this, MainActivity.class);
                startActivity(intent);

            }else
            {
                Bundle bundle = data.getExtras();

                if (intentResult.getContents() == null) {
                    Intent intent = new Intent(QRCodeScanActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getBaseContext(), "Scan Cancelled", Toast.LENGTH_SHORT).show();

                } else {

                    //Toast.makeText(getBaseContext(),intentResult.getRawBytes().toString(), Toast.LENGTH_SHORT).show();
                    byte[] encrypted = md.digest(intentResult.getRawBytes());


                    for(byte b : encrypted)
                    {
                        sb.append(String.format("%02x", b));
                    }
                    Toast.makeText(getBaseContext(),sb, Toast.LENGTH_SHORT).show();
                    scannedResult = new QRCode(sb.toString());

                    showScoreText.setText("Points: " + scannedResult.getScore());


                }
            }


        }
    }
}

