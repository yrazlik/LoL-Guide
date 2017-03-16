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
                queryParams, null, responseListener);
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
                pathParams, queryParams, null, responseListener);
    }

}
