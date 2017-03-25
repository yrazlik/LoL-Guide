package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.activities.MatchInfoActivity;
import com.yrazlik.loltr.adapters.SimpleSpinnerAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.SummonerDto;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.MatchInfoResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfoResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.RobotoButton;
import com.yrazlik.loltr.view.RobotoEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yrazlik on 3/4/15.
 */
public class MatchInfoFragment extends BaseFragment implements ResponseListener{

    private AppCompatSpinner regionSpinner;
    private SimpleSpinnerAdapter spinnerAdapter;
    private String[] regions = Commons.regions;
    private RobotoButton searchButton;
    private RobotoEditText summonerNameET;
    private String selectedRegion = "tr";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_info, container, false);
        regionSpinner = (AppCompatSpinner) v.findViewById(R.id.regionSpinner);
        searchButton = (RobotoButton) v.findViewById(R.id.searchButton);
        summonerNameET = (RobotoEditText) v.findViewById(R.id.summonerNameET);
        spinnerAdapter = new SimpleSpinnerAdapter(getContext(), new ArrayList<>(Arrays.asList(regions)));
        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Commons.SERVICE_BASE_URL_FOR_MATCH_INFO = Commons.SERVICE_BASE_URL;
                        Commons.SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED = Commons.SPECTATOR_SERVICE_BASE_URL_TR;
                        selectedRegion = "tr";
                        break;
                    case 1:
                        Commons.SERVICE_BASE_URL_FOR_MATCH_INFO = Commons.SERVICE_BASE_URL_EUW;
                        Commons.SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED = Commons.SPECTATOR_SERVICE_BASE_URL_EUW;
                        selectedRegion = "euw";
                        break;
                    case 2:
                        Commons.SERVICE_BASE_URL_FOR_MATCH_INFO = Commons.SERVICE_BASE_URL_NA;
                        Commons.SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED = Commons.SPECTATOR_SERVICE_BASE_URL_NA;
                        selectedRegion = "na";
                        break;
                    case 3:
                        Commons.SERVICE_BASE_URL_FOR_MATCH_INFO = Commons.SERVICE_BASE_URL_EUNE;
                        Commons.SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED = Commons.SPECTATOR_SERVICE_BASE_URL_EUNE;
                        selectedRegion = "eune";
                        break;
                    case 4:
                        Commons.SERVICE_BASE_URL_FOR_MATCH_INFO = Commons.SERVICE_BASE_URL_OCE;
                        Commons.SPECTATOR_SERVICE_BASE_URL_CURRENT_SELECTED = Commons.SPECTATOR_SERVICE_BASE_URL_OC;
                        selectedRegion = "oce";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        regionSpinner.setAdapter(spinnerAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(summonerNameET.getText() == null || summonerNameET.getText().toString().equals("")){
                    Toast.makeText(getContext(), R.string.pleaseEnterSummonerName, Toast.LENGTH_SHORT).show();;
                }else{
                    String summonerName = summonerNameET.getText().toString();
                    ArrayList<String> pathParams = new ArrayList<String>();
                    pathParams.add(selectedRegion);
                    pathParams.add("v1.4");
                    pathParams.add("summoner");
                    pathParams.add("by-name");
                    summonerName = summonerName.replaceAll("\\s","");
                    pathParams.add(summonerName);
                    HashMap<String, String> queryParams = new HashMap<String, String>();
                    queryParams.put("api_key", Commons.API_KEY);

                    ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.SUMMONER_INFO_REQUEST, pathParams, queryParams, null, MatchInfoFragment.this);
                }
            }
        });

        return v;
    }

    private void showInterstitial(){
        try {
            if (((LolApplication) (getActivity().getApplication())).shouldShowInterstitial()) {
                ((LolApplication) (getActivity().getApplication())).showInterstitial();
            }
        }catch (Exception ignored){}
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof SummonerInfoResponse){
            SummonerInfoResponse resp = (SummonerInfoResponse)response;

            String summonerId = null;

            for (Map.Entry<String, SummonerDto> entry : resp.entrySet())
            {
                if(entry.getValue() != null) {
                    summonerId = String.valueOf(entry.getValue().getId());
                }
                break;
            }

            if(summonerId != null && !summonerId.equals("")){
                ArrayList<String> pathParams = new ArrayList<String>();
                pathParams.add(String.valueOf(summonerId));
                HashMap<String, String> queryParams = new HashMap<String, String>();
                queryParams.put("api_key", Commons.API_KEY);

                ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.MATCH_INFO_REQUEST, pathParams, queryParams, null, MatchInfoFragment.this);
            }else {
                Toast.makeText(getContext(), R.string.anErrorOccured, Toast.LENGTH_SHORT).show();
            }


        }else if(response instanceof MatchInfoResponse){
            MatchInfoResponse resp = (MatchInfoResponse) response;
            Intent i = new Intent(getContext(), MatchInfoActivity.class);
            i.putExtra("MATCH_INFO_RESPONSE", resp);
            i.putExtra("SELECTED_REGION", selectedRegion);
            showInterstitial();
            startActivity(i);
        }

    }

    @Override
    public void onFailure(Object response) {
        Toast.makeText(getContext(), R.string.playerNotCurrentlyPlaying, Toast.LENGTH_SHORT).show();
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

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("MatchInfoFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
