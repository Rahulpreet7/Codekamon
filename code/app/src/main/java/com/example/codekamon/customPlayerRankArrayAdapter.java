package com.example.codekamon;



import android.content.Context;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// Custom ArrayAdapter which helps list a listview of gas stations
public class customPlayerRankArrayAdapter extends ArrayAdapter<Player> {

    private Context mContext;
    private List<Player> playerArrayList = new ArrayList<>();

    public customPlayerRankArrayAdapter(@NonNull Context context, ArrayList<Player> list) {
        super(context, 0 , list);
        mContext = context;
        playerArrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_rank_list, parent,false);

        Player currentPlayer = playerArrayList.get(position);

        TextView userRank = (TextView) listItem.findViewById(R.id.userRank);
        TextView userName = (TextView) listItem.findViewById(R.id.userName);
        TextView userScore = (TextView) listItem.findViewById(R.id.userScore);




        String userNameText =currentPlayer.getUserName();
        String userScoreText = currentPlayer.getTotalScore().toString();


        userName.setText(userNameText);
        userScore.setText(userScoreText);


        return listItem;
    }
}



