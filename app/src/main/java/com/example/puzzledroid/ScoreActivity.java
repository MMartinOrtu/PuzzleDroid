package com.example.puzzledroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    TextView score1;
    TextView date1;
    BBDD_Helper helperDB;
    ListView list;
    Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        helperDB = new BBDD_Helper(this);
        score1 = (TextView) findViewById(R.id.score1);
        date1 = (TextView) findViewById(R.id.date1);
        list = (ListView) findViewById(R.id.lw1);

        SQLiteDatabase db =  helperDB.getReadableDatabase();
        String[] projection = {
                Score_BBDD.COLUMN_2,
                Score_BBDD.COLUMN_3
        };

        String sortOrder = Score_BBDD.COLUMN_2 + " ASC LIMIT 5";

        Cursor c = db.query(
                Score_BBDD.TABLE_NAME,
                projection,
                null,
                null,
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


}
