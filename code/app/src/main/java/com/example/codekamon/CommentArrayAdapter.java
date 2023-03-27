package com.example.codekamon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CommentArrayAdapter extends ArrayAdapter<Comment> {

    public CommentArrayAdapter(Context context, ArrayList<Comment> comments){
        super(context, 0, comments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.content_comment, parent,false);
        }else{
            view = convertView;
        }
        Comment comment = super.getItem(position);
        TextView playerComment = view.findViewById(R.id.player_comment_text);
        TextView playerThatCommented = view.findViewById((R.id.player_text));

        playerComment.setText(comment.getComment());
        playerThatCommented.setText(comment.getPlayerName());
        view.setClickable(false);
        return view;
    }
}
