package com.yrazlik.loltr.utils;

import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrazlik on 28/03/17.
 */

public class CacheObject {

    private static CacheObject mInstance;

    private ChampionListDto allChampionsData;
    private List<ChampionDto> weeklyFreeChampions;

    public static CacheObject getInstance() {
        if(mInstance == null) {
            mInstance = new CacheObject();
        }
        return mInstance;
    }

    private CacheObject() {
    }

    public ChampionListDto getAllChampionsData() {
        return allChampionsData;
    }

    public void setAllChampionsData(ChampionListDto allChampionsData) {
        this.allChampionsData = allChampionsData;
    }

    public List<ChampionDto> getWeeklyFreeChampions() {
        return weeklyFreeChampions;
    }

    public void setWeeklyFreeChampions(List<ChampionDto> weeklyFreeChampions) {
        this.weeklyFreeChampions = weeklyFreeChampions;
    }
}
