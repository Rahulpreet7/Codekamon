package com.example.codekamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        //deviceId -> Needs to be passed to other activities that need the users details
        String deviceId = intent.getStringExtra(SignUpActivity.DEVICE_ID);

        //delete comments below when merging to main
//        firestore = FirebaseFirestore.getInstance();
//        firestore.collection("Players").document(deviceId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                String username = task.getResult().get("Username").toString();
//                TextView helloWorld = findViewById(R.id.hello);
//                helloWorld.setText(username);
//            }
//        });

    }
}