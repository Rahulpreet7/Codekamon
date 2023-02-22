package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    public static final String DEVICE_ID = "com.example.codekamon.DEVICE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        String androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("Players");
        colRef.document(androidId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("!", "Document exists!");
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra(DEVICE_ID, androidId);
                        startActivity(intent);
                    } else {
                        Log.d("!", "Document does not exist!");
                        setContentView(R.layout.activity_sign_up);
                        Button button = (Button) findViewById(R.id.sign_up_button);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText username = findViewById(R.id.pick_username_edit_text);
                                EditText email = findViewById(R.id.email_edit_text);

                                colRef.whereEqualTo("Username", username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (!task.getResult().isEmpty()){
                                            Toast.makeText(SignUpActivity.this, "Username is taken", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            HashMap<String,String> data = new HashMap<>();
                                            data.put("Username", username.getText().toString());
                                            data.put("Email", email.getText().toString());
                                            colRef.document(androidId).set(data);
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            intent.putExtra(DEVICE_ID, androidId);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            }
                        });
                    }
                } else {
                    Log.d("!", "Failed with: ", task.getException());
                }
            }
        });
    }
}