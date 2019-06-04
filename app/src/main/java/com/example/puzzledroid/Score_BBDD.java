package com.example.puzzledroid;

public class Score_BBDD {

    Score_BBDD( ){}

   public static final String TABLE_NAME = "score";
   public static final String COLUMN_1 = "id";
   public static final String COLUMN_2 = "score";
   public static final String COLUMN_3 = "date";


   public static final String CREATE_ENTRIES =
            "CREATE TABLE " + Score_BBDD.TABLE_NAME + " (" +
                    Score_BBDD.COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    Score_BBDD.COLUMN_2 + " TEXT, " +
                    Score_BBDD.COLUMN_3 + " TEXT);";

   public static final String SQL_DELETE_ENTRIES =
           "DROP TABLE IF EXISTS " + Score_BBDD.TABLE_NAME;

}
