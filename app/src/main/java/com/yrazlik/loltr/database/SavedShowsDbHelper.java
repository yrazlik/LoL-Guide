package com.yrazlik.loltr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yrazlik on 10/21/15.
 */
public class SavedShowsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";


    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_SAVED_SHOWS =
            "CREATE TABLE " + SavedResponsesDBContract.SavedShows.TABLE_NAME_SAVED_RESPONSES + " (" +
                    SavedResponsesDBContract.SavedShows.COLUMN_NAME_REQUEST_ID + INTEGER_TYPE +  " PRIMARY KEY"  + COMMA_SEP +
                    SavedResponsesDBContract.SavedShows.COLUMN_NAME_REQUEST_DATA + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SavedResponsesDBContract.SavedShows.TABLE_NAME_SAVED_RESPONSES;



    public SavedShowsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SAVED_SHOWS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}