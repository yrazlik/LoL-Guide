package com.yrazlik.loltr.responseclasses;

import java.util.HashMap;

/**
 * Created by yrazlik on 1/4/16.
 */
public class SummonerByNameResponse {

    private HashMap<String, SummonerInfoByName> response;

    public HashMap<String, SummonerInfoByName> getResponse() {
        return response;
    }

    public void setResponse(HashMap<String, SummonerInfoByName> response) {
        this.response = response;
    }
}
