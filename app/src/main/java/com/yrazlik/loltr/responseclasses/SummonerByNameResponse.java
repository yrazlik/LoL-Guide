package com.yrazlik.loltr.responseclasses;

import java.util.Map;

/**
 * Created by yrazlik on 1/4/16.
 */
public class SummonerByNameResponse {

    private Map<String, SummonerInfo> summonerInfo;

    public Map<String, SummonerInfo> getSummonerInfo() {
        return summonerInfo;
    }

    public void setSummonerInfo(Map<String, SummonerInfo> summonerInfo) {
        this.summonerInfo = summonerInfo;
    }
}
