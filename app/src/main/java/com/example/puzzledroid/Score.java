package com.example.puzzledroid;

public class Score {
    private String score;
    private String date;

    public Score(String fscore, String fdate){
        score = fscore;
        date = fdate;
    }

    public String getScore() {
        return score;
    }

    public String getDate(){
        return date;
    }
}

