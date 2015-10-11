package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.activities.MatchInfoActivity;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.SummonerDto;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.MatchInfoResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfoResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yrazlik on 3/4/15.
 */
public class MatchInfoFragment extends BaseFragment implements ResponseListener{

    private Spinner regionSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String[] regions = {"TR1", "EUW1", "NA1", "EUN1", "OC1",};// "BR1", "LA1", "LA2", "RU", "KR", "PBE1"};
    private Button searchButton;
    private EditText summonerNameET;
    private String selectedRegion = "tr";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_info, container, false);
        regionSpinner = (Spinner)v.findViewById(R.id.regionSpinner);
        searchButton = (Button)v.findViewById(R.id.searchButton);
        summonerNameET = (EditText)v.findViewById(R.id.summonerNameET);
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regions);
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

                    ServiceRequest.getInstance().makeGetRequest(Commons.SUMMONER_INFO_REQUEST, pathParams, queryParams, null, MatchInfoFragment.this);
                }
            }
        });

        return v;
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

                ServiceRequest.getInstance().makeGetRequest(Commons.MATCH_INFO_REQUEST, pathParams, queryParams, null, MatchInfoFragment.this);
            }else {
                Toast.makeText(getContext(), R.string.anErrorOccured, Toast.LENGTH_SHORT).show();
            }


        }else if(response instanceof MatchInfoResponse){
            MatchInfoResponse resp = (MatchInfoResponse) response;
            Intent i = new Intent(getContext(), MatchInfoActivity.class);
            i.putExtra("MATCH_INFO_RESPONSE", resp);
            i.putExtra("SELECTED_REGION", selectedRegion);
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
