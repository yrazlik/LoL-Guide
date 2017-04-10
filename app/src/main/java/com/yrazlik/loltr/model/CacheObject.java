package com.yrazlik.loltr.model;

/**
 * Created by yrazlik on 28/03/17.
 */

public class CacheObject {

    private static CacheObject mInstance;

    private ChampionListDto allChampionsData;

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
}
