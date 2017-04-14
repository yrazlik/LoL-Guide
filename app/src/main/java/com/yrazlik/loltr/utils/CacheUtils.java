package com.yrazlik.loltr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.db.DbHelper;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;
import com.yrazlik.loltr.model.ImageDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yrazlik on 11/04/17.
 */

public class CacheUtils {

    private static final String LOL_TR_SHARED_PREFS = "LOL_TR_SHARED_PREFS";

    private static final long ALL_CHAMPIONS_MAX_CACHE_TIME = 1 * 24 * 60 * 60 * 1000; // 1 day
    private static final long WEEKLY_FREE_MAX_CACHE_TIME = 1 * 60 * 60 * 1000; // 1 hr

    private static final String ALL_CHAMPS_LAST_SAVED = "ALL_CHAMPS_LAST_SAVE";
    private static final String ALL_CHAMPS_DATA = "ALL_CHAMPS_DATA";
    private static final String WEEKLY_FREE_CHAMPS_LAST_SAVED = "WEEKLY_FREE_CHAMPS_LAST_SAVED";
    private static final String WEEKLY_FREE_CHAMPS_DATA = "WEEKLY_FREE_CHAMPS_DATA";
    private static final String RP_IP_COSTS_LAST_SAVED = "RP_IP_COSTS_LAST_SAVED";
    private static final String RP_IP_COSTS_DATA = "RP_IP_COSTS_DATA";

    private static CacheUtils mInstance;

    public static CacheUtils getInstance() {
        if (mInstance == null) {
            mInstance = new CacheUtils();
        }
        return mInstance;
    }

    private SharedPreferences getSharedPrefs() {
        SharedPreferences prefs = LolApplication.getAppContext().getSharedPreferences(LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
        return prefs;
    }
}
