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

/**
 * This class is an adapter class that makes an ArrayList of Comments
 * behave like an ArrayAdapter of Comments.
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {

    /**
     * Creates a CommentArrayAdapter
     * @param context contains a set of properties in the form of NamedValue objects. These characteristics provide information about the client,
     *               the environment, or the conditions of a request and are often cumbersome to send as arguments.
     * @param comments The arraylist of comments
     */
    public CommentArrayAdapter(Context context, ArrayList<Comment> comments){
        super(context, 0, comments);
    }

    /**
     * Gets the view of a certain item in the comments list.
     *
     * @param position The position of the item
     * @return The view of the item
     */
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
