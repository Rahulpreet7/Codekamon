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
 * This class is an adapter class that makes an ArrayList of player's scanned QR codes.
 * It behaves like an ArrayAdapter of Player's Codes.
 */
public class PlayerCodesAdapter extends ArrayAdapter<QRCode> {

    private Context mContext;
    private ArrayList<QRCode> qrCodeArrayList;

    /**
     * Creates a PlayerCodesAdapter.
     * @param context contains a set of properties in the form of NamedValue objects. These characteristics provide information about the client, the environment, or the conditions of a request and are often cumbersome to send as arguments.
     * @param list the array list of player's scanned QR codes.
     */
    public PlayerCodesAdapter(@NonNull Context context, ArrayList<QRCode> list) {
        super(context, 0, list);
        this.mContext = context;
        this.qrCodeArrayList = list;
    }

    /**
     * Gets the view of a certain item in the player's QR codes list.
     * @param position the position of the item
     * @param convertView the Adapter uses the convertView as a way of recycling old View objects that are no longer being used
     * @param parent a special view that can contain other views
     * @return the view of the item
     */
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
