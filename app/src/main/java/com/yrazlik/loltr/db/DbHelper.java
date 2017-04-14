package com.yrazlik.loltr.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;
import com.yrazlik.loltr.utils.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yrazlik on 13/04/17.
 */

public class DbHelper {

    private static final String LOL_TR_SHARED_PREFS = "LOL_TR_SHARED_PREFS";
    private static final String WEEKLY_FREE_DATA_LAST_SAVED = "WEEKLY_FREE_DATA_LAST_SAVED";
    private static final String ALL_CHAMPIONS_DATA_LAST_SAVED = "ALL_CHAMPIONS_DATA_LAST_SAVED";
    private static final String IP_RP_PRICES_DATA_LAST_SAVED = "IP_RP_PRICES_DATA_LAST_SAVED";

    public long ipRpTimeout = 1 * 60 * 60 * 1000; // 1 hr
    public long wfTimeout = 1 * 60 * 60 * 1000; // 1 hr
    public long acTimeout = 1 * 24 * 60 * 60 * 1000; // 1 day
    public boolean cacheEnabled = true;

    private static DbHelper mInstance;

    public static DbHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DbHelper();
        }
        return mInstance;
    }

    private DbHelper() {

    }

    private SharedPreferences getSharedPrefs() {
        return LolApplication.getAppContext().getSharedPreferences(LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    private long getLastSaved(String sharedPrefKey) {
        SharedPreferences prefs = getSharedPrefs();
        if (prefs != null) {
            return prefs.getLong(sharedPrefKey, 0);
        }
        return 0;
    }

    private void saveLastSaved(String sharedPrefKey, long sharedPrefValue) {
        SharedPreferences prefs = getSharedPrefs();
        if (prefs != null) {
            try {
                prefs.edit().putLong(sharedPrefKey, sharedPrefValue).commit();
            } catch (Exception e) {
                Logger.log("Error writing last saved data to shared prefs");
            }
        }
    }

    public void saveWeeklyFreeChampionsData(List<ChampionDto> weeklyFreeChampions) {
        if (cacheEnabled) {
            try {
                new Delete().from(WeeklyFreeChampionsTable.class).execute();
                ActiveAndroid.beginTransaction();
                try {
                    for (int i = 0; i < weeklyFreeChampions.size(); i++) {
                        ChampionDto c = weeklyFreeChampions.get(i);
                        WeeklyFreeChampionsTable item = new WeeklyFreeChampionsTable(c.getId(), c.getImage().getFull(), c.getName(), c.getDateInterval(),
                                c.getChampionRp(), c.getChampionIp());
                        item.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                    saveLastSaved(WEEKLY_FREE_DATA_LAST_SAVED, Calendar.getInstance().getTimeInMillis());
                    Logger.log("Successfully saved weekly free champions data to db.");
                } finally {
                    ActiveAndroid.endTransaction();
                }
            } catch (Exception e) {
                Logger.log("Error saving weekly free champions data to db.");
            }
        }
    }

    public void saveAllChampionsData(ChampionListDto championListDto) {
        if (cacheEnabled) {
            try {
                new Delete().from(AllChampionsTable.class).execute();
                List<AllChampionsTable> allChampions = getAllChampionsList(championListDto);
                ActiveAndroid.beginTransaction();
                try {
                    for (int i = 0; i < allChampions.size(); i++) {
                        AllChampionsTable item = allChampions.get(i);
                        item.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                    saveLastSaved(ALL_CHAMPIONS_DATA_LAST_SAVED, Calendar.getInstance().getTimeInMillis());
                    Logger.log("Successfully saved all champions data to db.");
                } finally {
                    ActiveAndroid.endTransaction();
                }
            } catch (Exception e) {
                Logger.log("Error saving all champions data to db.");
            }
        }
    }

    public void saveRpIpCostsData(Map<String, HashMap> championCosts) {
        if (cacheEnabled) {
            try {
                new Delete().from(ChampionCostsTable.class).execute();
                List<ChampionCostsTable> costs = convertCostsMapToList(championCosts);
                ActiveAndroid.beginTransaction();
                try {
                    for (int i = 0; i < costs.size(); i++) {
                        ChampionCostsTable item = costs.get(i);
                        item.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                    saveLastSaved(IP_RP_PRICES_DATA_LAST_SAVED, Calendar.getInstance().getTimeInMillis());
                    Logger.log("Successfully ip rp costs data to db.");
                } finally {
                    ActiveAndroid.endTransaction();
                }
            } catch (Exception e) {
                Logger.log("Error saving ip rp costs to db.");
            }
        }
    }

    public List<ChampionDto> getWeeklyFreeChampionsData() {
        try {
            long lastSaved = getLastSaved(WEEKLY_FREE_DATA_LAST_SAVED);
            if (cacheExpired(lastSaved, wfTimeout)) {
                Logger.log("Weekly free champions cache expired.");
                return null;
            } else {
                List<WeeklyFreeChampionsTable> weeklyFreeChampionsData = new Select("*").from(WeeklyFreeChampionsTable.class).orderBy("name ASC").execute();
                if (weeklyFreeChampionsData != null && weeklyFreeChampionsData.size() > 0) {
                    List<ChampionDto> weeklyFreeChampions = new ArrayList<>();
                    for (int i = 0; i < weeklyFreeChampionsData.size(); i++) {
                        WeeklyFreeChampionsTable dbItem = weeklyFreeChampionsData.get(i);
                        weeklyFreeChampions.add(new ChampionDto(dbItem.champId, dbItem.name, dbItem.dateInterval, dbItem.imageUrl, dbItem.ipPrice, dbItem.rpPrice));
                    }
                    Logger.log("Weekly free champions data is received fom db.");
                    return weeklyFreeChampions;
                }
                Logger.log("Weekly free champions data from db is null");
                return null;
            }
        } catch (Exception e) {
            Logger.log("Weekly free champions unexpected db exception.");
            return null;
        }
    }

    public List<ChampionDto> getAllChampionsData() {
        try {
            long lastSaved = getLastSaved(ALL_CHAMPIONS_DATA_LAST_SAVED);
            if (cacheExpired(lastSaved, acTimeout)) {
                Logger.log("All champions cache expired.");
                return null;
            } else {
                List<AllChampionsTable> allChampsData = new Select("*").from(AllChampionsTable.class).orderBy("name ASC").execute();
                if (allChampsData != null && allChampsData.size() > 0) {
                    List<ChampionDto> allChampions = new ArrayList<>();
                    for (int i = 0; i < allChampsData.size(); i++) {
                        AllChampionsTable dbItem = allChampsData.get(i);
                        allChampions.add(new ChampionDto(dbItem.champId, dbItem.key, dbItem.name, dbItem.imageUrl, dbItem.title));
                    }
                    Logger.log("All champions data is received from db.");
                    return allChampions;
                }
                Logger.log("All champions data from db is null.");
                return null;
            }
        } catch (Exception e) {
            Logger.log("All champions unexpected db exception.");
            return null;
        }
    }

    public List<ChampionCostsTable> getRpIpCostsData() {
        try {
            long lastSaved = getLastSaved(IP_RP_PRICES_DATA_LAST_SAVED);
            if (cacheExpired(lastSaved, ipRpTimeout)) {
                Logger.log("IP RP cache expired.");
                return null;
            } else {
                List<ChampionCostsTable> costs = new Select("*").from(ChampionCostsTable.class).execute();
                if (costs != null && costs.size() > 0) {
                    Logger.log("IP RP data received from db.");
                    return costs;
                }
                Logger.log("IP RP data from db is null.");
                return null;
            }
        } catch (Exception e) {
            Logger.log("IP RP data unexpected db exception.");
            return null;
        }
    }


    private List<AllChampionsTable> getAllChampionsList(ChampionListDto championListDto) {
        try {
            Map<String, ChampionDto> champsData = championListDto.getData();
            if (champsData != null && champsData.size() > 0) {
                List<AllChampionsTable> allChampions = new ArrayList<>();

                for (Map.Entry<String, ChampionDto> entry : champsData.entrySet()) {
                    AllChampionsTable c = new AllChampionsTable(Commons.CHAMPION_IMAGE_BASE_URL + entry.getKey() + ".png",
                            entry.getValue().getName(), entry.getValue().getId(),
                            entry.getValue().getKey(), "\"" + entry.getValue().getTitle() + "\"");
                    allChampions.add(c);
                }

                return allChampions;
            }
        } catch (Exception e) {
            Logger.log("Error parsing all champions list");
        }
        return null;
    }

    public List<ChampionCostsTable> convertCostsMapToList(Map<String, HashMap> championCostsData) {
        if (championCostsData != null) {
            List<ChampionCostsTable> costs = new ArrayList<>();
            Map<String, HashMap> championCosts = championCostsData.get("costs");
            if (championCosts != null && championCosts.entrySet() != null) {
                try {
                    for (Map.Entry<String, HashMap> entry : championCosts.entrySet()) {
                        String champId = entry.getKey();
                        Map<String, String> values = entry.getValue();
                        costs.add(new ChampionCostsTable(champId, String.valueOf(values.get("ip_cost")), String.valueOf(values.get("rp_cost"))));
                    }
                    return costs;
                } catch (Exception e) {
                    Logger.log("Error converting costs map to list");
                }
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

    public void removeAllCaches() {
        try {
            new Delete().from(WeeklyFreeChampionsTable.class).execute();
            new Delete().from(AllChampionsTable.class).execute();
            new Delete().from(ChampionCostsTable.class).execute();
            saveLastSaved(WEEKLY_FREE_DATA_LAST_SAVED, 0);
            saveLastSaved(ALL_CHAMPIONS_DATA_LAST_SAVED, 0);
            saveLastSaved(IP_RP_PRICES_DATA_LAST_SAVED, 0);
            Logger.log("Removed all caches");
        } catch (Exception e) {
            Logger.log("Error removing all caches");
        }
    }
}
