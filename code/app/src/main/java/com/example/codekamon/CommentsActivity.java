package com.example.codekamon;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private CommentArrayAdapter commentAdapter;
    private LinearLayout addCommentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getSupportActionBar().hide();

        commentsToShow = new ArrayList<Comment>();
        commentsListView = findViewById(R.id.comments_list);
        addCommentLayout = findViewById(R.id.add_comment_layout);
        Button addButton = findViewById(R.id.add_comment_button);
        EditText commentEditText = findViewById(R.id.add_comment_edittext);

        Intent intent = getIntent();
        String name = intent.getStringExtra("QRCode name");


        PlayersDB playersDB = new PlayersDB(FirebaseFirestore.getInstance(), true);

        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        TextView noCommentsShow = findViewById(R.id.no_comments_text);

        QRCodesDB codesDB = new QRCodesDB(FirebaseFirestore.getInstance());
        codesDB.getQRCode(name, new OnCompleteListener<QRCode>() {
            @Override
            public void onComplete(QRCode code, boolean success) {
                if (success == true){
                    ArrayList<HashMap<String,String>> comments = code.getComments();
                    if (comments.size() > 0){
                        noCommentsShow.setVisibility(View.INVISIBLE);
                    }
                    for(HashMap<String,String> comment : comments){
                        Comment aComment = new Comment(comment.get("playerName"), comment.get("comment"));
                        commentsToShow.add(aComment);
                    }
                    commentAdapter = new CommentArrayAdapter(CommentsActivity.this, commentsToShow);
                    commentsListView.setAdapter(commentAdapter);
                    playersDB.getPlayer(deviceId, new OnCompleteListener<Player>() {
                        @Override
                        public void onComplete(Player player, boolean success) {
                            if (success == true) {
                                HashMap<String,String> map = player.getPlayerCodes();
                                if (!map.containsKey(name)){
                                    addCommentLayout.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    addButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Comment comment = new Comment(player.getUserName(), commentEditText.getText().toString());
                                            code.addComment(comment);
                                            commentEditText.setText("");
                                            codesDB.updateQRCode(code, new OnCompleteListener<QRCode>() {
                                                @Override
                                                public void onComplete(QRCode item, boolean success) {
                                                    return;
                                                }
                                            });
                                            commentsToShow.add(0, comment);
                                            commentAdapter.notifyDataSetChanged();
                                            noCommentsShow.setVisibility(View.INVISIBLE);
                                            commentsListView.setSelection(0);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });


    }
}
