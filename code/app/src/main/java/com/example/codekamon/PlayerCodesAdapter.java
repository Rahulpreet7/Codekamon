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
public class PlayerCodesAdapter extends ArrayAdapter<QRCode> {

    private Context mContext;
    private ArrayList<QRCode> qrCodeArrayList;

    public PlayerCodesAdapter(@NonNull Context context, ArrayList<QRCode> list) {
        super(context, 0, list);
        this.mContext = context;
        this.qrCodeArrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.custom_code_list, parent, false);

        QRCode currentQRCode = qrCodeArrayList.get(position);


        TextView codeName = (TextView) view.findViewById(R.id.codeName);
        TextView codeScore = (TextView) view.findViewById(R.id.codeScore);
        String codeNameText =currentQRCode.getName();
        String codeScoreText = String.valueOf(currentQRCode.getScore());

        codeName.setText(codeNameText);
        codeScore.setText(codeScoreText);


        return view;
    }
}
