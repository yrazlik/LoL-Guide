package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.yrazlik.loltr.activities.FullScreenImageActivity;
import com.yrazlik.loltr.activities.FullScreenSkinActivity;
import com.yrazlik.loltr.adapters.ChampionSkinsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Skin;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ChampionSkinsResponse;
import com.yrazlik.loltr.service.ServiceHelper;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrazlik on 2/1/15.
 */
public class ChampionSkinsFragment extends BaseFragment implements ResponseListener, AdapterView.OnItemClickListener{

    private ListView skinsList;
    private ChampionSkinsAdapter adapter;
    private ArrayList<Skin> skins;
    private int champId;
    private String key;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            showProgressWithWhiteBG();
            rootView = inflater.inflate(R.layout.fragment_champion_skins, container, false);
            getExtras();
            skinsList = (ListView) rootView.findViewById(R.id.skinsList);
            skinsList.setOnItemClickListener(this);
        }

        setSkinsAdapter();

        return rootView;
    }

    private void getExtras() {
        Bundle args = getArguments();
        if(args != null){
            champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
        }
    }

    private void setSkinsAdapter() {
        if(skins == null || skins.size() == 0) {
            ServiceHelper.getInstance(getContext()).makeChampionSkinsRequest(champId, this);
        } else {
            if(adapter == null || adapter.getCount() == 0) {
                dismissProgress();
                adapter = new ChampionSkinsAdapter(getContext(), R.layout.listrow_champion_skins, skins, key);
                skinsList.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSuccess(Object response) {
        dismissProgress();
        if(response instanceof ChampionSkinsResponse) {
            ChampionSkinsResponse resp = (ChampionSkinsResponse) response;
            skins = resp.getSkins();
            key = resp.getKey();
            setSkinsAdapter();
        }
    }

    @Override
    public void onFailure(final Object response) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showRetryView();
                    String errorMessage = (String) response;
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){}


    }

    @Override
    protected void retry() {
        super.retry();
        setSkinsAdapter();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent i = new Intent(getActivity(), FullScreenImageActivity.class);
            i.putExtra(FullScreenImageActivity.EXTRA_IMAGE_URL, ((ChampionSkinsAdapter) parent.getAdapter()).getImageUrl(position));
            showInterstitial();
            startActivity(i);
        }catch (Exception ignored) {}
    }

    private void showInterstitial(){
        try {
            if (((LolApplication) (getActivity().getApplication())).shouldShowInterstitial()) {
                ((LolApplication) (getActivity().getApplication())).showInterstitial();
            }
        }catch (Exception ignored){}
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
