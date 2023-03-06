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

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class handles the logic of the sign up screen.
 */
public class SignUpActivity extends AppCompatActivity  {

    public static String TAG = "SignUpActivity";
    public static final String DEVICE_ID = "com.example.codekamon.DEVICE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        String androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        PlayersDB playersDB = new PlayersDB();
        CollectionReference playersRef = playersDB.getCollectionReference();

        playersDB.getPlayer(this, new com.example.codekamon.OnCompleteListener<Player>() {
            @Override
            public void onComplete(Player item, boolean success) {
                if (success == true){
                    Log.d(TAG, "Player found.");
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d(TAG, "Failed to get Player from the database.");

                    setContentView(R.layout.activity_sign_up);
                    Button button = (Button) findViewById(R.id.sign_up_button);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText username = findViewById(R.id.pick_username_edit_text);
                            EditText email = findViewById(R.id.email_edit_text);
                            Player player = new Player(username.getText().toString(),email.getText().toString(), androidId);

                            playersRef.whereEqualTo("Username", username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (!task.getResult().isEmpty()){
                                        Toast.makeText(SignUpActivity.this, "Username is taken", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        playersDB.addPlayer(player);
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        intent.putExtra("PLAYER",player);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }
}