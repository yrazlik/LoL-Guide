package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.SummonerByNameResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrazlik on 1/4/16.
 */
public class ProfileFragment extends BaseFragment implements ResponseListener{

    private String[] regions = {"TR", "EUW", "NA", "EUN", "OCE", "BR", "LAN", "LAS", "RU", "KR"};
    private RelativeLayout usernameRegionRL;
    private ArrayAdapter<String> regionSpinnerAdapter;
    private Spinner regionSpinner;
    private EditText usernameET;
    private Button saveButton;
    private String region = "";
    private String userId;
    private String userName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameRegionRL = (RelativeLayout)v.findViewById(R.id.usernameRegionRL);
        usernameET = (EditText)v.findViewById(R.id.usernameET);
        regionSpinner = (Spinner)v.findViewById(R.id.regionSpinner);
        saveButton = (Button)v.findViewById(R.id.buttonSave);
        regionSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, regions);
        regionSpinner.setAdapter(regionSpinnerAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                region = regionSpinner.getSelectedItem().toString().toLowerCase();

                if(usernameET.getText() == null || usernameET.getText().toString() == null || usernameET.getText().toString().length() <= 0){
                    Toast.makeText(getActivity(), R.string.pleaseEnterSummonerName, Toast.LENGTH_SHORT).show();
                } else if(region == null || region.length() <= 0){
                    Toast.makeText(getActivity(), R.string.pleaseSelectYourRegion, Toast.LENGTH_SHORT).show();
                } else{
                    ArrayList<String> pathParams = new ArrayList<String>();
                    pathParams.add("api");
                    pathParams.add("lol");
                    pathParams.add(region);
                    pathParams.add("v1.4");
                    pathParams.add("summoner");
                    pathParams.add("by-name");
                    pathParams.add(usernameET.getText().toString());
                    HashMap<String, String> queryParams = new HashMap<String, String>();
                    queryParams.put("api_key", Commons.API_KEY);
                    ServiceRequest.getInstance(getActivity()).makeSummonerByNameRequest(
                            Commons.SUMMONER_BY_NAME_REQUEST, region,
                            pathParams, queryParams, null, ProfileFragment.this);
                }
            }
        });

        SharedPreferences prefs = getContext().getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
        String summonerName = prefs.getString(Commons.LOL_TR_SUMMONER_NAME, null);
        String summonerLevel = prefs.getString(Commons.LOL_TR_SUMMONER_LEVEL, null);
        String summonerId = prefs.getString(Commons.LOL_TR_SUMMONER_ID, null);
        String summonerProfileIconId = prefs.getString(Commons.LOL_TR_PROFILE_ICON_ID, null);

        if(isValid(summonerName) && isValid(summonerLevel) && isValid(summonerId) && isValid(summonerProfileIconId)){
            usernameRegionRL.setVisibility(View.GONE);
        }else{
            usernameRegionRL.setVisibility(View.VISIBLE);
        }

        return v;
    }

    private boolean isValid(String s){
        if(s == null || s.length() == 0){
            return false;
        }
        return true;
    }

    @Override
    public void reportGoogleAnalytics() {

    }

    private void saveSummonerInfoToSharedPrefs(SummonerByNameResponse response){
    /*    if(response != null){
            SharedPreferences prefs = getContext().getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(Commons.LOL_TR_SUMMONER_NAME, response.getName()).commit();
            prefs.edit().putString(Commons.LOL_TR_SUMMONER_ID, response.getId() + "").commit();
            prefs.edit().putString(Commons.LOL_TR_PROFILE_ICON_ID, response.getProfileIconId() + "").commit();
            prefs.edit().putString(Commons.LOL_TR_SUMMONER_LEVEL, response.getSummonerLevel() + "").commit();
        }*/
    }

    @Override
    public void onSuccess(Object response) {
     /*   if(response instanceof SummonerByNameResponse){
            SummonerByNameResponse summonerByNameResponse = (SummonerByNameResponse) response;
            Commons.summonerInfo = new SummonerByNameResponse();
            Commons.summonerInfo.setName(summonerByNameResponse.getName());
            Commons.summonerInfo.setId(summonerByNameResponse.getId());
            Commons.summonerInfo.setProfileIconId(summonerByNameResponse.getProfileIconId());
            Commons.summonerInfo.setRevisionDate(summonerByNameResponse.getRevisionDate());
            Commons.summonerInfo.setSummonerLevel(summonerByNameResponse.getSummonerLevel());
            saveSummonerInfoToSharedPrefs(summonerByNameResponse);
        }*/
    }

    @Override
    public void onFailure(Object response) {
        if(response instanceof Integer){
            Integer requestId = (Integer) response;
            if(requestId == Commons.SUMMONER_BY_NAME_REQUEST) {
                Toast.makeText(getContext(), R.string.cannot_find_username, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
