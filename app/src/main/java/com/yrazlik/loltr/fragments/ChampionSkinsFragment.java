package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.activities.FullScreenSkinActivity;
import com.yrazlik.loltr.adapters.ChampionSkinsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Skin;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ChampionSkinsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrazlik on 2/1/15.
 */
public class ChampionSkinsFragment extends BaseFragment implements ResponseListener, AdapterView.OnItemClickListener{

    ListView skinsList;
    ChampionSkinsAdapter adapter;
    int champId;
    String key;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_champion_skins, container, false);
        getExtras();
        skinsList = (ListView)v.findViewById(R.id.skinsList);
        skinsList.setOnItemClickListener(this);
        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add("static-data");
        pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        pathParams.add(String.valueOf(champId));
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
        queryParams.put("champData", "skins");
        queryParams.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance().makeGetRequest(Commons.CHAMPION_SKINS_REQUEST, pathParams, queryParams, null, this);


        return v;
    }

    private void getExtras() {
        Bundle args = getArguments();
        if(args != null){
            champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
        }
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof ChampionSkinsResponse) {
            ChampionSkinsResponse resp = (ChampionSkinsResponse) response;
            ArrayList<Skin> skins = resp.getSkins();
            key = resp.getKey();
            adapter = null;
            adapter = new ChampionSkinsAdapter(getContext(), R.layout.listrow_champion_skins, skins, key);
            skinsList.setAdapter(adapter);
        }
    }

    @Override
    public void onFailure(Object response) {
        try {
            String errorMessage = (String) response;
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }catch (Exception e){

        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Skin s = (Skin)skinsList.getItemAtPosition(position);
        Intent i = new Intent(getActivity(), FullScreenSkinActivity.class);
        i.putExtra("EXTRA_SKIN_KEY", key);
        i.putExtra("EXTRA_SKIN_POSITION", position);
        startActivity(i);

    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("ChampionSkinsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
