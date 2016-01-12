package com.yrazlik.loltr.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.RecentSearchesAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.RecentSearchItem;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.LeagueInfoResponse;
import com.yrazlik.loltr.responseclasses.RankedStatsResponse;
import com.yrazlik.loltr.responseclasses.RecentMatchesResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfo;
import com.yrazlik.loltr.service.ServiceRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrazlik on 1/4/16.
 */
public class SummonerSearchFragment extends BaseFragment implements ResponseListener{

    private RelativeLayout usernameRegionRL, recentSearchesRL;
    private EditText usernameET;
    private ImageButton searchButton;
    private String region = "";
    private String userId;
    private String userName;
    private ListView recentSearchesLV;
    private RecentSearchesAdapter recentSearchesAdapter;
    private ArrayList<RecentSearchItem> recentSearchesArrayList;
    private SummonerInfo summonerByNameResponse;
    private RankedStatsResponse rankedStatsResponse;
    private RecentMatchesResponse recentMatchesResponse;
    private LeagueInfoResponse leagueInfoResponse;

    private boolean recentMatchesResponseReceived, rankedStatsResponseReceived, leagueInfoResponseReceived;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summoner_search, container, false);

        recentMatchesResponseReceived = false;
        rankedStatsResponseReceived = false;
        leagueInfoResponseReceived = false;
        region = Commons.SELECTED_REGION;
        usernameRegionRL = (RelativeLayout)v.findViewById(R.id.usernameRegionRL);
        recentSearchesRL = (RelativeLayout)v.findViewById(R.id.recentSearchesRL);
        usernameET = (EditText)v.findViewById(R.id.usernameET);
        searchButton = (ImageButton)v.findViewById(R.id.buttonSave);
        recentSearchesLV = (ListView) v.findViewById(R.id.recentSearchesLV);
        recentSearchesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    RecentSearchItem item = recentSearchesArrayList.get(position);
                    if(item != null){
                        makeSearchRequest(item.getName());
                    }
                }catch (Exception ignored){}

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usernameET.getText() == null || usernameET.getText().toString() == null || usernameET.getText().toString().length() <= 0) {
                    Toast.makeText(getActivity(), R.string.pleaseEnterSummonerName, Toast.LENGTH_SHORT).show();
                } else if (region == null || region.length() <= 0) {
                    Toast.makeText(getActivity(), R.string.pleaseSelectYourRegion, Toast.LENGTH_SHORT).show();
                } else {
                   makeSearchRequest(usernameET.getText().toString());
                }
            }
        });

        recentSearchesArrayList = Commons.loadRecentSearchesArrayList(getContext());
        if(recentSearchesArrayList == null){
            recentSearchesArrayList = new ArrayList<>();
        }

        if(recentSearchesArrayList.size() > 0){
            recentSearchesRL.setVisibility(View.VISIBLE);
            recentSearchesAdapter = new RecentSearchesAdapter(getContext(), R.layout.list_row_recentsearches, recentSearchesArrayList);
            recentSearchesLV.setAdapter(recentSearchesAdapter);
        }else{
            recentSearchesRL.setVisibility(View.GONE);
        }

        return v;
    }

    private boolean isValid(String s){
        if(s == null || s.length() == 0){
            return false;
        }
        return true;
    }

    private void showLoading(){
        Dialog d = ServiceRequest.showLoading(getContext());
        if(d != null){
            d.show();
        }
    }

    private void makeSearchRequest(String itemName){
        showLoading();
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("api");
        pathParams.add("lol");
        pathParams.add(region);
        pathParams.add("v1.4");
        pathParams.add("summoner");
        pathParams.add("by-name");
        try {
            pathParams.add(URLEncoder.encode(itemName, "UTF-8").replace("+", "%20"));
        } catch (UnsupportedEncodingException e) {
            pathParams.add(usernameET.getText().toString());
        }
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("api_key", Commons.API_KEY);
        ServiceRequest.getInstance(getActivity()).makeSummonerByNameRequest(
                Commons.SUMMONER_BY_NAME_REQUEST, region,
                pathParams, queryParams, null, SummonerSearchFragment.this);



    }

    @Override
    public void reportGoogleAnalytics() {
        try {
            Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
            t.setScreenName("SummonerSearchFragment");
            t.send(new HitBuilders.ScreenViewBuilder().build());
        }catch (Exception ignored){}
    }

    private void saveSummonerInfoToSharedPrefs(SummonerInfo response){
        if(response != null){
            SharedPreferences prefs = getContext().getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(Commons.LOL_TR_SUMMONER_NAME, response.getName()).commit();
            prefs.edit().putString(Commons.LOL_TR_SUMMONER_ID, response.getId() + "").commit();
            prefs.edit().putString(Commons.LOL_TR_PROFILE_ICON_ID, response.getProfileIconId() + "").commit();
            prefs.edit().putString(Commons.LOL_TR_SUMMONER_LEVEL, response.getSummonerLevel() + "").commit();
        }
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof SummonerInfo){
            summonerByNameResponse = (SummonerInfo) response;
            Commons.summonerInfo = new SummonerInfo();
            Commons.summonerInfo.setName(summonerByNameResponse.getName());
            Commons.summonerInfo.setId(summonerByNameResponse.getId());
            Commons.summonerInfo.setProfileIconId(summonerByNameResponse.getProfileIconId());
            Commons.summonerInfo.setRevisionDate(summonerByNameResponse.getRevisionDate());
            Commons.summonerInfo.setSummonerLevel(summonerByNameResponse.getSummonerLevel());

            if(recentSearchesArrayList == null){
                recentSearchesArrayList = new ArrayList<>();
            }
            if(recentSearchesArrayList.size() >= 15){
                try {
                    recentSearchesArrayList.remove(recentSearchesArrayList.remove(recentSearchesArrayList.size() - 1));
                }catch (Exception ignored){}
            }
            RecentSearchItem recentSearchItem = new RecentSearchItem();
            recentSearchItem.setName(summonerByNameResponse.getName());
            recentSearchItem.setProfileIconId(summonerByNameResponse.getProfileIconId());
            recentSearchItem.setId(summonerByNameResponse.getId());
            recentSearchItem.setRegion(region);
            boolean contains = false;
            RecentSearchItem itemToRemove = null;
            for(RecentSearchItem item : recentSearchesArrayList){
                if(item.getId() == recentSearchItem.getId()){
                    contains = true;
                    itemToRemove = item;
                    break;
                }
            }
            if(!contains) {
                recentSearchesArrayList.add(0, recentSearchItem);
                Commons.saveRecentSearchesArray(recentSearchesArrayList, getContext());
            }else{
                recentSearchesArrayList.remove(itemToRemove);
                recentSearchesArrayList.add(0, recentSearchItem);
                Commons.saveRecentSearchesArray(recentSearchesArrayList, getContext());
            }


            ServiceRequest.getInstance(getActivity()).makeGetRecentMatchesRequest(
                    Commons.RECENT_MATCHES_REQUEST, region, summonerByNameResponse.getId() + "", null, SummonerSearchFragment.this);

            ServiceRequest.getInstance(getActivity()).makeGetRankedStatsRequest(
                    Commons.RANKED_STATS_REQUEST, region, summonerByNameResponse.getId() + "", null, SummonerSearchFragment.this);

            ServiceRequest.getInstance(getActivity()).makeGetLeagueInfoRequest(
                    Commons.LEAGUE_INFO_REQUEST, region, summonerByNameResponse.getId() + "", null, SummonerSearchFragment.this);

        }else if(response instanceof RecentMatchesResponse){
            recentMatchesResponse = (RecentMatchesResponse) response;
            recentMatchesResponseReceived = true;
            openSummonerContainerFragment();
        }else if(response instanceof RankedStatsResponse){
            rankedStatsResponse = (RankedStatsResponse) response;
            rankedStatsResponseReceived = true;
            openSummonerContainerFragment();
        }else if ((response instanceof LeagueInfoResponse)){
            leagueInfoResponse = (LeagueInfoResponse) response;
            openSummonerContainerFragment();
            leagueInfoResponseReceived = true;
            openSummonerContainerFragment();
        }
    }

    @Override
    public void onFailure(Object response) {
        if(response instanceof Integer){
            Integer requestId = (Integer) response;
            if(requestId == Commons.SUMMONER_BY_NAME_REQUEST) {
                Toast.makeText(getContext(), R.string.cannot_find_username, Toast.LENGTH_SHORT).show();
            }else if(requestId == Commons.RANKED_STATS_REQUEST) {
                rankedStatsResponse = null;
                rankedStatsResponseReceived = true;
                openSummonerContainerFragment();
            }else if(requestId == Commons.LEAGUE_INFO_REQUEST) {
                leagueInfoResponse = null;
                leagueInfoResponseReceived = true;
                openSummonerContainerFragment();
            }else if(requestId == Commons.RECENT_MATCHES_REQUEST){
                recentMatchesResponse = null;
                recentMatchesResponseReceived = true;
                openSummonerContainerFragment();
            }
        }
    }

    private void openSummonerContainerFragment(){
        if(leagueInfoResponseReceived && recentMatchesResponseReceived && rankedStatsResponseReceived) {
            ServiceRequest.hideLoading();
            FragmentManager fm = getFragmentManager();
            SummonerContainerFragment summonerContainerFragment = new SummonerContainerFragment();

            Bundle args = new Bundle();
            args.putSerializable(SummonerOverviewFragment.EXTRA_SUMMONER_INFO, summonerByNameResponse);
            args.putSerializable(MatchHistoryFragment.EXTRA_RECENTMATCHES, recentMatchesResponse);
            args.putSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS, rankedStatsResponse);
            args.putSerializable(SummonerOverviewFragment.EXTRA_LEAGUEINFO, leagueInfoResponse);
            FragmentTransaction ft = fm.beginTransaction();
            Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
            summonerContainerFragment.setArguments(args);
            ft.replace(R.id.content_frame, summonerContainerFragment).addToBackStack(Commons.SUMMONER_CONTAINER_FRAGMENT).commit();
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }
}
