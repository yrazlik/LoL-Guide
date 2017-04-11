package com.yrazlik.loltr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yrazlik on 11/04/17.
 */

public class CacheUtils {

    private static final String LOL_TR_SHARED_PREFS = "LOL_TR_SHARED_PREFS";

    private static final long ALL_CHAMPIONS_MAX_CACHE_TIME = 1 * 24 * 60 * 60 * 1000; // 1 day
    private static final long WEEKLY_FREE_MAX_CACHE_TIME = 2 * 60 * 60 * 1000; // 2 hrs

    private static final String ALL_CHAMPS_LAST_SAVED = "ALL_CHAMPS_LAST_SAVE";
    private static final String ALL_CHAMPS_DATA = "ALL_CHAMPS_DATA";
    private static final String WEEKLY_FREE_CHAMPS_LAST_SAVED = "WEEKLY_FREE_CHAMPS_LAST_SAVED";
    private static final String WEEKLY_FREE_CHAMPS_DATA = "WEEKLY_FREE_CHAMPS_DATA";
    private static final String RP_IP_COSTS_LAST_SAVED = "RP_IP_COSTS_LAST_SAVED";
    private static final String RP_IP_COSTS_DATA = "RP_IP_COSTS_DATA";

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
            getSharedPrefs().edit().putString(ALL_CHAMPS_DATA, null).commit();
            getSharedPrefs().edit().putLong(ALL_CHAMPS_LAST_SAVED, Calendar.getInstance().getTimeInMillis()).commit();
        }
    }

    public ChampionListDto retrieveAllChampionsData() {
        long lastSaveDate = getSharedPrefs().getLong(ALL_CHAMPS_LAST_SAVED, 0);
        long now = Calendar.getInstance().getTimeInMillis();

        if(now - lastSaveDate > ALL_CHAMPIONS_MAX_CACHE_TIME) {
            return null;
        }

        try {
            return  new Gson().fromJson(getSharedPrefs().getString(ALL_CHAMPS_DATA, null), ChampionListDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveWeeklyFreeChampionsData(List<ChampionDto> weeklyFreeChampions) {
        try {
            getSharedPrefs().edit().putString(WEEKLY_FREE_CHAMPS_DATA, new Gson().toJson(weeklyFreeChampions)).commit();
            getSharedPrefs().edit().putLong(WEEKLY_FREE_CHAMPS_LAST_SAVED, Calendar.getInstance().getTimeInMillis()).commit();
        } catch (Exception e) {
            getSharedPrefs().edit().putString(WEEKLY_FREE_CHAMPS_DATA, null).commit();
            getSharedPrefs().edit().putLong(WEEKLY_FREE_CHAMPS_LAST_SAVED, Calendar.getInstance().getTimeInMillis()).commit();
        }
    }

    public List<ChampionDto> retrieveWeeklyFreeChampionsData() {
        long lastSaveDate = getSharedPrefs().getLong(WEEKLY_FREE_CHAMPS_LAST_SAVED, 0);
        long now = Calendar.getInstance().getTimeInMillis();

        if(now - lastSaveDate > WEEKLY_FREE_MAX_CACHE_TIME) {
            return null;
        }

        try {
            return  new Gson().fromJson(getSharedPrefs().getString(WEEKLY_FREE_CHAMPS_DATA, null), new TypeToken<List<ChampionDto>>(){}.getType());
        } catch (Exception e) {
            return null;
        }
    }

    public void saveRpIpCostsData(Map<String, HashMap> championCosts) {
        try {
            getSharedPrefs().edit().putString(RP_IP_COSTS_DATA, new Gson().toJson(championCosts)).commit();
            getSharedPrefs().edit().putLong(RP_IP_COSTS_LAST_SAVED, Calendar.getInstance().getTimeInMillis()).commit();
        } catch (Exception e) {
            getSharedPrefs().edit().putString(RP_IP_COSTS_DATA, null).commit();
            getSharedPrefs().edit().putLong(RP_IP_COSTS_LAST_SAVED, Calendar.getInstance().getTimeInMillis()).commit();
        }
    }

    public Map<String, HashMap> retrieveRpIpCostsData() {
        long lastSaveDate = getSharedPrefs().getLong(RP_IP_COSTS_LAST_SAVED, 0);
        long now = Calendar.getInstance().getTimeInMillis();

        if(now - lastSaveDate > ALL_CHAMPIONS_MAX_CACHE_TIME) {
            return null;
        }

        try {
            return  new Gson().fromJson(getSharedPrefs().getString(WEEKLY_FREE_CHAMPS_DATA, null), new TypeToken<Map<String, HashMap>>(){}.getType());
        } catch (Exception e) {
            return null;
        }
    }


}
