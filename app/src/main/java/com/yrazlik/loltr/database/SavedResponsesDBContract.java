package com.yrazlik.loltr.database;

import android.provider.BaseColumns;

/**
 * Created by yrazlik on 10/21/15.
 */
public final class SavedResponsesDBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public SavedResponsesDBContract() {}

    /* Inner class that defines the table contents */
    public static abstract class SavedShows implements BaseColumns {
        public static final String TABLE_NAME_SAVED_RESPONSES = "savedResponses";
        public static final String COLUMN_NAME_REQUEST_ID = "requestid";
        public static final String COLUMN_NAME_REQUEST_DATA = "requestdata";
    }




}