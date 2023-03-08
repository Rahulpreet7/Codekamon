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

// Custom ArrayAdapter which helps list a listview of gas stations
public class totalScoreRankArrayAdapter extends ArrayAdapter<Player> {

    private Context mContext;
    private ArrayList<Player> playerArrayList;

    public totalScoreRankArrayAdapter(@NonNull Context context, ArrayList<Player> list) {
        super(context, 0 , list);
        this.mContext = context;
        this.playerArrayList = list;
    }

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
        String userScoreText = currentPlayer.getTotalScore().toString();

        userRank.setText(userRankText);
        userName.setText(userNameText);
        userScore.setText(userScoreText);


        return view;
    }
}



