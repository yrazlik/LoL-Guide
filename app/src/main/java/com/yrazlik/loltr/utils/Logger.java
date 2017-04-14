package com.yrazlik.loltr.utils;

import android.util.Log;

/**
 * Created by yrazlik on 14/04/17.
 */

public class Logger {

    public static final String TAG = "LoLApplication";

    public static void log(String message) {
        Log.d(TAG, message);
    }

}
