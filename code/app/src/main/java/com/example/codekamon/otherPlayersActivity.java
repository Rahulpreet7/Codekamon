package com.example.codekamon;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class otherPlayersActivity extends AppCompatActivity {

    private Button confirmButton;
    private Button backButton;
    private EditText findPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        confirmButton = findViewById(R.id.confirm);
        backButton = findViewById(R.id.back_button);
        findPlayer = findViewById(R.id.findPlayerText);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = findPlayer.getText().toString();
                FirebaseFirestore db;
                db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("Players");
                collectionReference
                        .whereEqualTo("Username", username)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Player player = document.toObject(Player.class);
                                        PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance());
                                        playersDB.getPlayer(document, new com.example.codekamon.OnCompleteListener<Player>() {
                                            @Override
                                            public void onComplete(Player item, boolean success) {
                                                Intent intent = new Intent(otherPlayersActivity.this, OtherUserProfile.class);
                                                intent.putExtra("PLAYER", item);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                    Toast.makeText(otherPlayersActivity.this, "No Player with this username'", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
