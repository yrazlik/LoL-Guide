package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
public class MatchInfoFragment extends Fragment implements ResponseListener{

    private Spinner regionSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String[] regions = {"TR1"};//, "NA1", "BR1", "LA1", "LA2", "OC1", "EUN1", "RU", "EUW1", "KR", "PBE1"};
    private Button searchButton;
    private EditText summonerNameET;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_info, container, false);
        regionSpinner = (Spinner)v.findViewById(R.id.regionSpinner);
        searchButton = (Button)v.findViewById(R.id.searchButton);
        summonerNameET = (EditText)v.findViewById(R.id.summonerNameET);
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regions);
        regionSpinner.setAdapter(spinnerAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(summonerNameET.getText() == null || summonerNameET.getText().toString().equals("")){
                    Toast.makeText(getContext(), R.string.pleaseEnterSummonerName, Toast.LENGTH_SHORT).show();;
                }else{
                    String summonerName = summonerNameET.getText().toString();
                    ArrayList<String> pathParams = new ArrayList<String>();
                    pathParams.add("na");
                    pathParams.add("v1.4");
                    pathParams.add("summoner");
                    pathParams.add("by-name");
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
            startActivity(i);
        }

    }

    @Override
    public void onFailure(Object response) {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
