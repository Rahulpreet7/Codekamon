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
 * This class is used to create a the ranking list of leader board based on highest scores of different players.
 */
public class highestScoreRankArrayAdapter extends ArrayAdapter<Player> {

    private Context mContext;
    private ArrayList<Player> playerArrayList;

    /**
     * This is a constructor.
     *
     * @param context Context
     * @param list Array list of Players
     */
    public highestScoreRankArrayAdapter(@NonNull Context context, ArrayList<Player> list) {
        super(context, 0 , list);
        this.mContext = context;
        this.playerArrayList = list;
    }

    /**
     * Used to get the text view for the list view of ranking list.
     * @param position Position of the current player.
     * @param convertView View.
     * @param parent View.
     * @return view (a text view for the list view).
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.custom_rank_list, parent,false);

        Player currentPlayer = playerArrayList.get(position);

        TextView userRank = (TextView) view.findViewById(R.id.userRank);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        TextView userScore = (TextView) view.findViewById(R.id.userScore);

        String userRankText = String.valueOf(playerArrayList.indexOf(currentPlayer) + 1);
        currentPlayer.setUserRank(playerArrayList.indexOf(currentPlayer)+1);

        String userNameText =currentPlayer.getUserName();
        String userScoreText = currentPlayer.getHighestScore().toString();

        userRank.setText(userRankText);
        userName.setText(userNameText);
        userScore.setText(userScoreText);

        return view;
    }
}
