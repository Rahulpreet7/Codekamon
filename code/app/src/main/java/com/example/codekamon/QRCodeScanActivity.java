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


public class QRCodeScanActivity extends AppCompatActivity {
    public static final String DEVICE_ID = "com.example.codekamon.DEVICE_ID";
    private MessageDigest md = MessageDigest.getInstance("SHA-256");
    private int stage = 0;
    private TextView showScoreText;
    private TextView caughtText;
    private TextView nameText;
    private Button stage_one_button;
    private static QRCode scannedResult;
    private StringBuilder sb = new StringBuilder();



    public QRCodeScanActivity() throws NoSuchAlgorithmException {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.show_score);
        super.onCreate(savedInstanceState);
        stage = 0;
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




/*

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted, display a dialog or notification to prompt the user
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {



            }
            else {

                intentIntegrator.initiateScan();
            }

        } else {

            // Camera permission is already granted, proceed with taking photos
            intentIntegrator.initiateScan();
        }

*/

        //String[] PERMISSIONS = {android.Manifest.permission.CAMERA}
/*
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            // Permission is granted
        } else {
            // Permission is not granted
            Toast.makeText(getBaseContext(), "Scan Cancelled", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QRCodeScanActivity.this, MainActivity.class);
            startActivity(intent);
        }

 */








        stage_one_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(QRCodeScanActivity.this, "stage 1 finished!", Toast.LENGTH_SHORT).show();
                scannedResult = new QRCode(nameText.getText().toString(), sb.toString());
                //dPlayer player = (Player) getIntent().getSerializableExtra("PLAYER");
                //player.addQR(scannedResult);
                Intent intent = new Intent(QRCodeScanActivity.this, photoTakingActivity.class);
                //can it be simplified?
                intent.putExtra("Name", nameText.getText().toString());
                intent.putExtra("sb", sb.toString());
                //for testing
                //intent.putExtra("Name", "abcd");
                //intent.putExtra("sb", "sb");
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
                    stage ++;
                    showScoreText.setText("Points: " + scannedResult.getScore());





                    //messageText.setText(intentResult.getContents());
                    //messageFormat.setText(intentResult.getFormatName());
                }
            }


        }
    }
}

