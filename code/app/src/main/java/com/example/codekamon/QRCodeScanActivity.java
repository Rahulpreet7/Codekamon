package com.example.codekamon;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
     * the text showing visual;
     */
    private TextView visualText;
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

    /**
     * stores a factory to build name and visuals by QR code seed.
     */
    private QRCodeSeedFactory seedfactory;

    /**
     * stores the visual Image generated.
     */
    private String generatedImage = "";

    /**
     * stores the times other player had scanned it.
     */
    int otherScannedCount;

    /**
     * stores the codes in the QRCode scan DB.
     */
    ArrayList<QRCode> codes;

    /**
     * stores the players in the player DB.
     */
    ArrayList<Player> players;

    PlayersDB playersdb = new PlayersDB(FirebaseFirestore.getInstance());


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
        visualText = findViewById(R.id.visual);
        Toast.makeText(QRCodeScanActivity.this, "Clicked 'Your Codes!'", Toast.LENGTH_SHORT).show();


        getSupportActionBar().hide();
        showScoreText.setText("Points: " + 1);


        otherScannedCount = 0;

        //if test, command this
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
                intent.putExtra("visual", generatedImage);


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
        Toast.makeText(getBaseContext(), "Scanned", Toast.LENGTH_SHORT).show();
        QRCodeScanActivity.super.onActivityResult(requestCode,resultCode,data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        // if the intentResult is null then "cancelled"
        if (intentResult != null) {
            System.out.println(intentResult);
            if(data == null)
            {
                Toast.makeText(getBaseContext(), "Scan Cancelled", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QRCodeScanActivity.this, MainActivity.class);
                startActivity(intent);

            }else
            {
                Bundle bundle = data.getExtras();

                if (intentResult.getContents() == null) {
                    Toast.makeText(getBaseContext(), "Scan Cancelled", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QRCodeScanActivity.this, MainActivity.class);

                    startActivity(intent);


                } else {
                    Toast.makeText(getBaseContext(), "Scan Succeed", Toast.LENGTH_SHORT).show();



                    //collectionReference.
                    //System.out.println(intentResult.getRawBytes());
                    //System.out.println("------------------print start---------------------");

                    //another set of rule
                    byte[] encrypted;
                    if(intentResult.getRawBytes() != null)
                    {
                        encrypted = md.digest(intentResult.getRawBytes());
                    } else
                    {
                        //int seed = (int)encrypted[0];
                        String scanResult = bundle.getString("SCAN_RESULT");
                        encrypted = scanResult.getBytes();
                    }


                    String s = "";
                    for(int i = 0; i <= 5; i ++)
                    {
                        s += ((int)encrypted[i])%2;
                    }
                    //int seed = (int)encrypted[0];
                    //s = Integer.toBinaryString(seed);
                    seedfactory = new QRCodeSeedFactory(s);
                    nameText.setText(seedfactory.generateName());
                    generatedImage = seedfactory.generateImage();
                    //generatedImage = "---";
                    visualText.setText(generatedImage);



                    for(byte b : encrypted)
                    {
                        sb.append(String.format("%02x", b));
                    }
                    //Toast.makeText(getBaseContext(),sb, Toast.LENGTH_SHORT).show();

                    //System.out.println("------------------" + scanResult +"---------------------");
                    //System.out.println("------------------" + sb.toString() +"---------------------");
                    scannedResult = new QRCode(sb.toString());



                    FirebaseFirestore playerDB;
                    playerDB = FirebaseFirestore.getInstance();
                    CollectionReference collectionReference = playerDB.collection("Players");





                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                FirebaseFirestoreException error) {

                            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                            {
                                Log.d(TAG, String.valueOf(doc.getData().get("ScannedCodes")));
                                //String content = doc.getId();
                                String PrevContent = "";
                                HashMap<String, String> playercodes= (HashMap<String, String>)doc.getData().get("ScannedCodes");



                                for(Map.Entry<String, String> entry : playercodes.entrySet())
                                {
                                    PrevContent = entry.getValue();
                                    if(PrevContent != null && PrevContent != "")
                                    {
                                        String a = PrevContent.substring(0,21);
                                        String b = sb.toString().substring(0,21);
                                        System.out.println("-------Value: " + a +  "-----------");
                                        System.out.println("-------content: " + b + "-----------");
                                        System.out.println("-------equals: " + a.equals(b) + "-----------");
                                        if(a.equals(b))
                                        {
                                            System.out.println("------- triggered -----------");

                                            otherScannedCount ++;
                                            showScoreText.setText("Points: " + scannedResult.getScore() + "\n" + otherScannedCount +  " player(s) had scanned it.");

                                            break;
                                        }
                                    }


                                }



                            }


                        }
                    });
                    System.out.println("----------------count: "+ otherScannedCount + "  -----------------");


                    players = new ArrayList<>();


                    showScoreText.setText("Points: " + scannedResult.getScore() + "\n" + otherScannedCount +  " players had scanned it.");
                    //showScoreText.setText("Points: " + scannedResult.getScore());

                }
            }


        }
    }
}

