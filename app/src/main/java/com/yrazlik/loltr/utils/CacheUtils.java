package com.yrazlik.loltr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.model.ChampionListDto;

import java.util.Calendar;

/**
 * Created by yrazlik on 11/04/17.
 */

public class CacheUtils {

    private static final String LOL_TR_SHARED_PREFS = "LOL_TR_SHARED_PREFS";

    private static final long MAX_CACHE_TIME = 1 * 24 * 60 * 60 * 1000; //1 day

    private static final String ALL_CHAMPS_LAST_SAVED = "ALL_CHAMPS_LAST_SAVE";
    private static final String ALL_CHAMPS_DATA = "ALL_CHAMPS_DATA";

    private static CacheUtils mInstance;

    public static CacheUtils getInstance() {
        if(mInstance == null) {
            mInstance = new CacheUtils();
        }
        return mInstance;
    }

    private SharedPreferences getSharedPrefs() {
        SharedPreferences prefs = LolApplication.getAppContext().getSharedPreferences(LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
        return prefs;
    }

    public void saveAllChampionsData(ChampionListDto championListDto) {
        try {
            getSharedPrefs().edit().putString(ALL_CHAMPS_DATA, new Gson().toJson(championListDto)).commit();
            getSharedPrefs().edit().putLong(ALL_CHAMPS_LAST_SAVED, Calendar.getInstance().getTimeInMillis()).commit();
        } catch (Exception e) {
            getSharedPrefs().edit().putString(ALL_CHAMPS_DATA, "").commit();
            getSharedPrefs().edit().putLong(ALL_CHAMPS_LAST_SAVED, Calendar.getInstance().getTimeInMillis()).commit();
        }
    }

    public ChampionListDto retrieveAllChampionsData() {
        long lastSaveDate = getSharedPrefs().getLong(ALL_CHAMPS_LAST_SAVED, 0);
        long now = Calendar.getInstance().getTimeInMillis();

        if(now - lastSaveDate > MAX_CACHE_TIME) {
            return null;
        }

        try {
            return  new Gson().fromJson(getSharedPrefs().getString(ALL_CHAMPS_DATA, ""), ChampionListDto.class);
        } catch (Exception e) {
            return null;
        }
    }



}
