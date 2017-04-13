package com.yrazlik.loltr.db;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by yrazlik on 13/04/17.
 */

public class DbHelper {

    private static final long WEEKLY_FREE_MAX_CACHE_TIME = 1 * 60 * 60 * 1000; // 1 hr
    private static final long ALL_CHAMPIONS_MAX_CACHE_TIME = 1 * 24 * 60 * 60 * 1000; // 1 day

    private static DbHelper mInstance;

    public static DbHelper getInstance() {
        if(mInstance == null) {
            mInstance = new DbHelper();
        }
        return mInstance;
    }

    private DbHelper() {

    }

    public void saveWeeklyFreeChampionsData(List<ChampionDto> weeklyFreeChampions) {
        new Delete().from(WeeklyFreeChampionsTable.class).execute();
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < weeklyFreeChampions.size(); i++) {
                ChampionDto c = weeklyFreeChampions.get(i);
                WeeklyFreeChampionsTable item = new WeeklyFreeChampionsTable(c.getId(), c.getImage().getFull(), c.getName(), c.getDateInterval(),
                        c.getChampionRp(), c.getChampionIp(), Calendar.getInstance().getTimeInMillis());
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public void saveAllChampionsData(ChampionListDto championListDto) {
        new Delete().from(AllChampionsTable.class).execute();
        List<AllChampionsTable> allChampions = getAllChampionsList(championListDto);
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < allChampions.size(); i++) {
                AllChampionsTable item = allChampions.get(i);
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public List<ChampionDto> getAllChampionsData() {
        List<AllChampionsTable> allChampsData = new Select("*").from(AllChampionsTable.class).orderBy("name ASC").execute();
        List<ChampionDto> allChampions = new ArrayList<>();
        if(allChampsData != null && allChampsData.size() > 0) {
            long lastSaved = allChampsData.get(0).lastSaved;
            if(cacheExpired(lastSaved, ALL_CHAMPIONS_MAX_CACHE_TIME)) {
                return null;
            } else {
                for (int i = 0; i < allChampsData.size(); i++) {
                    AllChampionsTable dbItem = allChampsData.get(i);
                    allChampions.add(new ChampionDto(dbItem.champId, dbItem.key, dbItem.name, dbItem.imageUrl, dbItem.title));
                }
                return allChampions;
            }
        }
        return null;
    }

    public List<ChampionDto> getWeeklyFreeChampionsData() {
        List<WeeklyFreeChampionsTable> weeklyFreeChampionsData = new Select("*").from(WeeklyFreeChampionsTable.class).orderBy("name ASC").execute();
        List<ChampionDto> weeklyFreeChampions = new ArrayList<>();
        if(weeklyFreeChampionsData != null && weeklyFreeChampionsData.size() > 0) {
            long lastSaved = weeklyFreeChampionsData.get(0).lastSaved;
            if(cacheExpired(lastSaved, WEEKLY_FREE_MAX_CACHE_TIME)) {
                return null;
            } else {
                for (int i = 0; i < weeklyFreeChampionsData.size(); i++) {
                    WeeklyFreeChampionsTable dbItem = weeklyFreeChampionsData.get(i);
                    weeklyFreeChampions.add(new ChampionDto(dbItem.champId, dbItem.name, dbItem.dateInterval, dbItem.imageUrl, dbItem.ipPrice, dbItem.rpPrice));
                }
                return weeklyFreeChampions;
            }
        }
        return null;
    }


    private List<AllChampionsTable> getAllChampionsList (ChampionListDto championListDto){
        Map<String, ChampionDto> champsData = championListDto.getData();
        if(champsData != null && champsData.size() > 0) {
            try {
                List<AllChampionsTable> allChampions = new ArrayList<>();

                for (Map.Entry<String, ChampionDto> entry : champsData.entrySet()) {
                    AllChampionsTable c = new AllChampionsTable(Commons.CHAMPION_IMAGE_BASE_URL + entry.getKey() + ".png",
                            entry.getValue().getName(), entry.getValue().getId(),
                            entry.getValue().getKey(), "\"" + entry.getValue().getTitle() + "\"", Calendar.getInstance().getTimeInMillis());
                    allChampions.add(c);
                }

                return allChampions;
            } catch (Exception e) {
                Log.d("DB", "Error parsing all champions list");
            }
        }
        return null;
    }

    private boolean cacheExpired(long lastSaved, long maxCacheTime) {
        long now = Calendar.getInstance().getTimeInMillis();

        if (now - lastSaved > maxCacheTime) {
            return true;
        }
        return false;
    }
}
