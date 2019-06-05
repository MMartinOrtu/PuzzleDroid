package com.example.puzzledroid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {
    TextView score1;
    TextView date1;
    BBDD_Helper helperDB;
    ListView list;
    Score score;
    RadioButton three;
    RadioButton four;
    RadioButton five;
    public int difficulty = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        helperDB = new BBDD_Helper(this);
        score1 = (TextView) findViewById(R.id.score1);
        date1 = (TextView) findViewById(R.id.date1);
        list = (ListView) findViewById(R.id.lw1);
        three = (RadioButton) findViewById(R.id.three);
        four = (RadioButton) findViewById(R.id.four);
        five = (RadioButton) findViewById(R.id.five);

        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);

        Bundle difficultyData = getIntent().getExtras();

        difficulty = difficultyData.getInt("difficulty");
        generateScore(difficulty);
    }

    private void generateScore(int difficulty){

        SQLiteDatabase db =  helperDB.getReadableDatabase();
        String[] projection = {
                Score_BBDD.COLUMN_2,
                Score_BBDD.COLUMN_3,
        };

        String selection = Score_BBDD.COLUMN_4 + "= ?";
        String[] selectionArgs = {String.valueOf(difficulty)};

        String sortOrder = Score_BBDD.COLUMN_2 + " ASC LIMIT 5";

        Cursor c = db.query(
                Score_BBDD.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        ArrayList<Score> scoreList = new ArrayList<>();

        int numRows = c.getCount();
        if (numRows == 0) {
            Toast.makeText(this, "There is no score yet", Toast.LENGTH_LONG).show();
        } else {
            while(c.moveToNext()){
                long minutes = (c.getLong(0)/1000) / 60;
                long seconds = (c.getLong(0)/1000) % 60;
                String scoreTime = minutes+":"+seconds;
                score = new Score(scoreTime, c.getString(1));
                scoreList.add(score);
                TwoColumns_ListAdapter listAdapter = new TwoColumns_ListAdapter(this, R.layout.list_adapter_view, scoreList);
                list.setAdapter(listAdapter);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.three: {
                generateScore(3);
                break;
            }
            case  R.id.four: {
                generateScore(4);
                break;
            }
            case  R.id.five: {
                generateScore(5);
                break;
            }
        }
    }

}
