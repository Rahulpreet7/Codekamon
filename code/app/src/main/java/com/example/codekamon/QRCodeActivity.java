package com.example.codekamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Shows the page for viewing the details of a qr code.
 * Handles the logic of the page.
 */
public class QRCodeActivity extends AppCompatActivity {
    private String codeName;

    /**
     * Gets run when the activity is created
     * @param savedInstanceState The saved instance state of the activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        codeName = intent.getStringExtra("QRCode name");

        QRCodesDB codesDB = new QRCodesDB(FirebaseFirestore.getInstance());
        codesDB.getQRCode(codeName, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                setContentView(R.layout.activity_qr_code);
                TextView comments = findViewById(R.id.comments_text);
                TextView codename = findViewById(R.id.qr_code_name_text);
                View back = findViewById(R.id.backButton3);
                TextView codeVisual = findViewById(R.id.code_visualization_text);
                TextView score = findViewById(R.id.score_display_text);
                ImageView locationImage = findViewById(R.id.code_location_image);
                bitmapFactory factory = new bitmapFactory(item.getPhotoAsBytes());
                locationImage.setImageBitmap(factory.getImageAsBitmap());
                codename.setText(codeName);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


                codeVisual.setText(item.getVisualImage());
                comments.setText("See " + Integer.toString(item.getComments().size()) + " comments" );
                score.setText("Score : " + Integer.toString(item.getScore()));

                comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(QRCodeActivity.this, CommentsActivity.class);
                        intent.putExtra("QRCode name", codeName);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        QRCodesDB codesDB = new QRCodesDB(FirebaseFirestore.getInstance());
        codesDB.getQRCode(codeName, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                TextView comments = findViewById(R.id.comments_text);
                comments.setText("See " + Integer.toString(item.getComments().size()) + " comments");
            }
        });
    }
}
