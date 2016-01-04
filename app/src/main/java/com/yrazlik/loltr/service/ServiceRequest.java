package com.yrazlik.loltr.service;

import android.app.Dialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.requestclasses.Request;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.responseclasses.ChampionLegendResponse;
import com.yrazlik.loltr.responseclasses.ChampionOverviewResponse;
import com.yrazlik.loltr.responseclasses.ChampionRpIpCostsResponse;
import com.yrazlik.loltr.responseclasses.ChampionSkinsResponse;
import com.yrazlik.loltr.responseclasses.ChampionSpellsResponse;
import com.yrazlik.loltr.responseclasses.ChampionStrategyResponse;
import com.yrazlik.loltr.responseclasses.ItemDetailResponse;
import com.yrazlik.loltr.responseclasses.ItemsResponse;
import com.yrazlik.loltr.responseclasses.LeagueInfoResponse;
import com.yrazlik.loltr.responseclasses.LiveChannelsResponse;
import com.yrazlik.loltr.responseclasses.MatchInfoResponse;
import com.yrazlik.loltr.responseclasses.RecommendedItemsResponse;
import com.yrazlik.loltr.responseclasses.RuneResponse;
import com.yrazlik.loltr.responseclasses.StaticDataWithAltImagesResponse;
import com.yrazlik.loltr.responseclasses.StatsResponse;
import com.yrazlik.loltr.responseclasses.SummonerByNameResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfoResponse;
import com.yrazlik.loltr.responseclasses.WeeklyFreeChampionsResponse;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceRequest {

    public static final String TAG_GET_REQUEST = "get_request";

	private static ServiceRequest instance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
	private Context mContext;

	public static Dialog progressDialog;
	private HttpResponse response;

    public static ServiceRequest getInstance(Context context) {
        if (instance == null) {
            instance = new ServiceRequest(context);
        }
        return instance;
    }

    private ServiceRequest(Context context){
        this.mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());

    }

    public <T> void addToRequestQueue(com.android.volley.Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public Context getContext() {
        return mContext;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

	private String getServiceEndpointUrl(int requestId){
		switch(requestId){
			case Commons.WEEKLY_FREE_CHAMPIONS_REQUEST:
				return Commons.SERVICE_BASE_URL_SELECTED;
			case Commons.STATIC_DATA_WITH_ALT_IMAGES_REQUEST:
			case Commons.CHAMPION_OVERVIEW_REQUEST:
			case Commons.CHAMPION_SPELLS_REQUEST:
			case Commons.ALL_CHAMPIONS_REQUEST:
			case Commons.CHAMPION_LEGEND_REQUEST:
			case Commons.CHAMPION_STRATEGY_REQUEST:
			case Commons.RECOMMENDED_ITEMS_REQUEST:
			case Commons.ALL_ITEMS_REQUEST:
			case Commons.ITEM_DETAIL_REQUEST:
			case Commons.ALL_RUNES_REQUEST:
            case Commons.CHAMPION_SKINS_REQUEST:
				return Commons.STATIC_DATA_BASE_URL;
			case Commons.CHAMPION_RP_IP_COSTS_REQUEST:
				return Commons.URL_CHAMPION_PRICES;
			case Commons.CHAMPION_SPLASH_IMAGE_REQUEST:
				return Commons.CHAMPION_SPLASH_IMAGE_BASE_URL;
            case Commons.LIVE_CHANNELS_REQUEST:
                return Commons.LIVE_CHANNELS_URL;
            case Commons.MATCH_INFO_REQUEST:
                return Commons.SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED;
            case Commons.SUMMONER_INFO_REQUEST:
                return Commons.SERVICE_BASE_URL_FOR_MATCH_INFO;
            case Commons.LEAGUE_INFO_REQUEST:
                return Commons.SERVICE_BASE_URL_FOR_MATCH_INFO;
            case Commons.STATS_REQUEST:
                return Commons.SERVICE_BASE_URL_FOR_MATCH_INFO;
			default:
				return "";
		}
	}

    public void makeGetRequest(int requestID, ArrayList<String> pathParams, HashMap<String, String> queryParams, Object requestData,
                               final ResponseListener listener){
        final Request request = new Request(requestID, pathParams, queryParams);
        String urlString = getServiceEndpointUrl(request.getRequestID()) + request.getPathParametersString() + request.getQueryParametersString();
        StringRequest getReq = new StringRequest(com.android.volley.Request.Method.GET, urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                Object parsedResponse = parseResponse(request.getRequestID(), response);
                listener.onSuccess(parsedResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                 NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    String json = new String(response.data);
                    json = trimMessage(json, "message");
                    listener.onFailure(json);
                }

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    String json = new String(getContext().getResources().getString(R.string.networkError));
                    listener.onFailure(json);
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        }){
            @Override
            public void addMarker(String tag) {
                super.addMarker(tag);
            }
        };

        getReq.setShouldCache(true);
        addToRequestQueue(getReq, TAG_GET_REQUEST);
        Dialog progress = showLoading(getContext());
        if(progress != null){
            try {
                progress.show();
            }catch (Exception ignored){}
        }
    }

    public void makeSummonerByNameRequest(final int requestID, String region, ArrayList<String> pathParams, HashMap<String, String> queryParams, Object requestData,
                                          final ResponseListener listener){
        final Request request = new Request(requestID, pathParams, queryParams);
        String urlString = getSummonerApiUrlByRegion(region) + request.getPathParametersString() + request.getQueryParametersString();
        StringRequest getReq = new StringRequest(com.android.volley.Request.Method.GET, urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideLoading();
                Object parsedResponse = parseResponse(request.getRequestID(), response);
                listener.onSuccess(parsedResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
               /* NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    String json = new String(response.data);
                    json = trimMessage(json, "message");
                    listener.onFailure(json);
                }*/

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    String json = new String(getContext().getResources().getString(R.string.networkError));
                    listener.onFailure(json);
                } else if (error instanceof AuthFailureError) {
                    listener.onFailure(requestID);
                } else if (error instanceof ServerError) {
                    listener.onFailure(requestID);
                } else if (error instanceof NetworkError) {
                    listener.onFailure(requestID);
                } else if (error instanceof ParseError) {
                    listener.onFailure(requestID);
                }
            }
        }){
            @Override
            public void addMarker(String tag) {
                super.addMarker(tag);
            }
        };

        getReq.setShouldCache(true);
        addToRequestQueue(getReq, TAG_GET_REQUEST);
        Dialog progress = showLoading(getContext());
        if(progress != null){
            try {
                progress.show();
            }catch (Exception ignored){}
        }
    }

    private String getSummonerApiUrlByRegion(String region){
        return "https://" + region + ".api.pvp.net";
    }

    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }


    private Object parseResponse(int requestID, String response) {
        Gson gson = new Gson();
        switch (requestID) {
            case Commons.WEEKLY_FREE_CHAMPIONS_REQUEST:
                return gson.fromJson(response, WeeklyFreeChampionsResponse.class);
            case Commons.STATIC_DATA_WITH_ALT_IMAGES_REQUEST:
                return gson.fromJson(response, StaticDataWithAltImagesResponse.class);
            case Commons.CHAMPION_RP_IP_COSTS_REQUEST:
                return gson.fromJson(response, ChampionRpIpCostsResponse.class);
            case Commons.CHAMPION_OVERVIEW_REQUEST:
                return gson.fromJson(response, ChampionOverviewResponse.class);
            case Commons.CHAMPION_SPELLS_REQUEST:
                return gson.fromJson(response, ChampionSpellsResponse.class);
            case Commons.ALL_CHAMPIONS_REQUEST:
                return gson.fromJson(response, AllChampionsResponse.class);
            case Commons.CHAMPION_LEGEND_REQUEST:
                return gson.fromJson(response, ChampionLegendResponse.class);
            case Commons.CHAMPION_STRATEGY_REQUEST:
                return gson.fromJson(response, ChampionStrategyResponse.class);
            case Commons.RECOMMENDED_ITEMS_REQUEST:
                return gson.fromJson(response, RecommendedItemsResponse.class);
            case Commons.ALL_ITEMS_REQUEST:
                return gson.fromJson(response, ItemsResponse.class);
            case Commons.ITEM_DETAIL_REQUEST:
                return gson.fromJson(response, ItemDetailResponse.class);
            case Commons.ALL_RUNES_REQUEST:
                return gson.fromJson(response, RuneResponse.class);
            case Commons.LIVE_CHANNELS_REQUEST:
                return gson.fromJson(response, LiveChannelsResponse.class);
            case Commons.CHAMPION_SKINS_REQUEST:
                return gson.fromJson(response, ChampionSkinsResponse.class);
            case Commons.SUMMONER_INFO_REQUEST:
                return gson.fromJson(response, SummonerInfoResponse.class);
            case Commons.MATCH_INFO_REQUEST:
                return gson.fromJson(response, MatchInfoResponse.class);
            case Commons.LEAGUE_INFO_REQUEST:
                return gson.fromJson(response, LeagueInfoResponse.class);
            case Commons.STATS_REQUEST:
                return gson.fromJson(response, StatsResponse.class);
            case Commons.SUMMONER_BY_NAME_REQUEST:
                try {
                    JSONObject obj = new JSONObject(response);
                    obj.length();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return gson.fromJson(response, SummonerByNameResponse.class);
            default:
                return null;
        }
    }

    public static Dialog showLoading(Context context) {
        try {
            if(context != null) {
                if (progressDialog == null) {
                    progressDialog = new Dialog(context, R.style.customDialogTheme);
                    progressDialog.setContentView(R.layout.loading_view);
                }

                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                return progressDialog;
            }else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static Dialog hideLoading() {
        try {
            if (progressDialog != null) {
                progressDialog.hide();
            }

            return progressDialog;
        } catch (Exception e){
            return null;
        }
    }


}
