package com.example.codekamon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentsActivity extends AppCompatActivity {
    private ArrayList<Comment> commentsToShow;
    private ListView commentsListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getSupportActionBar().hide();

        commentsToShow = new ArrayList<Comment>();
        Intent intent = getIntent();
        String name = intent.getStringExtra("QRCode name");

        TextView noCommentsShow = findViewById(R.id.no_comments_text);

        QRCodesDB codesDB = new QRCodesDB(FirebaseFirestore.getInstance());
        codesDB.getQRCode(name, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode item, boolean success) {
                if (success == true){
                    noCommentsShow.setVisibility(View.INVISIBLE);
                    ArrayList<HashMap<String,String>> comments = item.getComments();
                    for(HashMap<String,String> comment : comments){
                        Comment aComment = new Comment(comment.get("playerName"), comment.get("comment");
                        commentsToShow.add(aComment);
                    }

                }
            }
        });
    }
}
