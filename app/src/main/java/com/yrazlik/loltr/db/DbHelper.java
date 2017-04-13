package com.yrazlik.loltr.db;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
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

    public void saveAllChampionsData(ChampionListDto championListDto) {
        List<AllChampionsDbItem> allChampions = getAllChampionsList(championListDto);
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < allChampions.size(); i++) {
                AllChampionsDbItem item = allChampions.get(i);
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public List<ChampionDto> getAllChampionsData() {
        List<AllChampionsDbItem> allChampsData = new Select("*").from(AllChampionsDbItem.class).orderBy("name ASC").execute();
        List<ChampionDto> allChampions = new ArrayList<>();
        if(allChampsData != null && allChampsData.size() > 0) {
            long lastSaved = allChampsData.get(0).lastSaved;
            if(cacheExpired(lastSaved, ALL_CHAMPIONS_MAX_CACHE_TIME)) {
                return null;
            } else {
                for (int i = 0; i < allChampsData.size(); i++) {
                    AllChampionsDbItem dbItem = allChampsData.get(i);
                    allChampions.add(new ChampionDto(dbItem.champId, dbItem.key, dbItem.name, dbItem.imageUrl, dbItem.title));
                }
                return allChampions;
            }
        }
        return null;
    }


    private List<AllChampionsDbItem> getAllChampionsList (ChampionListDto championListDto){
        Map<String, ChampionDto> champsData = championListDto.getData();
        if(champsData != null && champsData.size() > 0) {
            try {
                List<AllChampionsDbItem> allChampions = new ArrayList<>();

                for (Map.Entry<String, ChampionDto> entry : champsData.entrySet()) {
                    AllChampionsDbItem c = new AllChampionsDbItem(Commons.CHAMPION_IMAGE_BASE_URL + entry.getKey() + ".png",
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
