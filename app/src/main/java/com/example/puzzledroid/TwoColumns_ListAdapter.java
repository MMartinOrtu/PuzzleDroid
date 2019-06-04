package com.example.puzzledroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TwoColumns_ListAdapter extends ArrayAdapter<Score> {

    private LayoutInflater mInflater;
    private ArrayList<Score> scores;
    private int mViewResourceId;

    public TwoColumns_ListAdapter(Context context, int textViewResourceId, ArrayList<Score> scores){
        super(context, textViewResourceId, scores);
        this.scores = scores;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parents){
        convertView = mInflater.inflate(mViewResourceId, null);
        Score score = scores.get(position);
        if (score != null){
            TextView score1 = (TextView) convertView.findViewById(R.id.score1);
            TextView date1 = (TextView) convertView.findViewById(R.id.date1);

            if(score1 != null){
                score1.setText((score.getScore()));
            }
            if(date1 != null){
                date1.setText((score.getDate().toString()));
            }

        }
        return convertView;
    }

}
