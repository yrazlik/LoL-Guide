package com.yrazlik.loltr.service;

import android.content.Context;

import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.listener.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrazlik on 16/03/17.
 */

public class ServiceHelper {

    private static ServiceHelper mInstance;
    private Context mContext;

    public static ServiceHelper getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new ServiceHelper(context);
        }
        return mInstance;
    }

    private ServiceHelper(Context context) {
        this.mContext = context;
    }

    public void makeWeeklyFreeChampsRequest(ResponseListener responseListener){
        ArrayList<String> pathParams = new ArrayList<String>();
        HashMap<String, String> queryParams = new HashMap<String, String>();
        pathParams.add("api");
        pathParams.add("lol");
        pathParams.add(Commons.SELECTED_REGION);
        pathParams.add("v1.2");
        pathParams.add("champion");
        queryParams.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams.put("api_key", Commons.API_KEY);
        queryParams.put("freeToPlay", "true");
        ServiceRequest.getInstance(mContext).makeGetRequest(
                Commons.WEEKLY_FREE_CHAMPIONS_REQUEST, pathParams,
                queryParams, null, false, responseListener);
    }

    public void makeGetAllChampionsRequest(ResponseListener responseListener) {
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams.put("champData", "altimages");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(mContext).makeGetRequest(
                Commons.ALL_CHAMPIONS_REQUEST,
                pathParams, queryParams, null, false, responseListener);
    }

    public void makeChampionOverviewRequest(int champId, ResponseListener responseListener) {
        ArrayList<String> pathParams = new ArrayList<>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        pathParams.add(String.valueOf(champId));
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("champData", "info,tags");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(mContext).makeGetRequest(Commons.CHAMPION_OVERVIEW_REQUEST, pathParams, queryParams, null, false, responseListener);
    }

    public void makeRecommendedItemsRequest(int champId, ResponseListener responseListener) {
        ArrayList<String> pathParams2 = new ArrayList<>();
        pathParams2.add("static-data");
        pathParams2.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams2.add("v1.2");
        pathParams2.add("champion");
        pathParams2.add(String.valueOf(champId));
        HashMap<String, String> queryParams2 = new HashMap<>();
        queryParams2.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams2.put("version", Commons.RECOMMENDED_ITEMS_VERSION);
        queryParams2.put("champData", "recommended");
        queryParams2.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(mContext).makeGetRequest(Commons.RECOMMENDED_ITEMS_REQUEST, pathParams2, queryParams2, null, false, responseListener);
    }

    public void makeChampionSpellsRequest(int champId, ResponseListener responseListener) {
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        pathParams.add(String.valueOf(champId));
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("champData", "passive,spells");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(mContext).makeGetRequest(Commons.CHAMPION_SPELLS_REQUEST, pathParams, queryParams, null, false, responseListener);
    }

    public void makeGetAllItemsRequest(ResponseListener responseListener) {
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("item");
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("itemListData", "all");
        queryParams.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance(mContext).makeGetRequest(Commons.ALL_ITEMS_REQUEST, pathParams, queryParams, null, false, responseListener);
    }

    public void makeGetAllRunesRequest(ResponseListener responseListener) {
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("rune");
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("runeListData", "image,sanitizedDescription");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(mContext).makeGetRequest(Commons.ALL_RUNES_REQUEST, pathParams, queryParams, null, false, responseListener);

    }

    public void makeChampionSkinsRequest(int champId, ResponseListener responseListener) {
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        pathParams.add(String.valueOf(champId));
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(mContext.getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("champData", "skins");
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(mContext).makeGetRequest(Commons.CHAMPION_SKINS_REQUEST, pathParams, queryParams, null, false, responseListener);

    }

    public void makeGetAllSpellsRequest(ResponseListener responseListener) {
        ArrayList<String> pathParams2 = new ArrayList<>();
        pathParams2.add("static-data");
        pathParams2.add(Commons.getInstance(mContext.getApplicationContext()).getRegion());
        pathParams2.add("v1.2");
        pathParams2.add("summoner-spell");
        HashMap<String, String> queryParams2 = new HashMap<String, String>();
        queryParams2.put("spellData", "image");
        queryParams2.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(mContext).makeGetRequest(Commons.SUMMONER_SPELLS_REQUEST, pathParams2, queryParams2, null, false, responseListener);

    }
}
