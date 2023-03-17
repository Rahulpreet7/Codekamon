package com.example.codekamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Shows the page for viewing the details of a qr code.
 * Handles the logic of the page.
 */
public class QRCodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        getSupportActionBar().hide();

        TextView comments = findViewById(R.id.comments_text);
        TextView codename = findViewById(R.id.qr_code_name_text);

        Intent intent = getIntent();
        String name = intent.getStringExtra("QRCode name");
        codename.setText(name);
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRCodeActivity.this, CommentsActivity.class);
                intent.putExtra("QRCode name", name);
                startActivity(intent);
            }
        });
    }
}
