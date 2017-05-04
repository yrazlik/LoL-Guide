package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.activities.MatchDetailActivity;
import com.yrazlik.loltr.adapters.MatchHistoryRVAdapter;
import com.yrazlik.loltr.adapters.VerticalSpaceItemDecoration;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.RecentMatchesResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.utils.AdUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrazlik on 1/5/16.
 */
public class MatchHistoryFragment extends BaseFragment implements ResponseListener{

    private long summonerId;
    private String region;
    private RecyclerView matchHistoryRV;
    private MatchHistoryRVAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Game> games;
    private RecentMatchesResponse recentMatchesResponse;

    public static final String EXTRA_SUMMONERID = "com.yrazlik.loltr.fragments.matchhistoryfragment.extrasummonerid";
    public static final String EXTRA_REGION = "com.yrazlik.loltr.fragments.matchhistoryfragment.extraregion";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_history, container, false);
        Bundle extras = getArguments();
        if (extras != null) {
            recentMatchesResponse = (RecentMatchesResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES);
            summonerId = extras.getLong(EXTRA_SUMMONERID);
            region = extras.getString(EXTRA_REGION);
        }
        if(recentMatchesResponse != null){
            games = recentMatchesResponse.getGames();
        }
        matchHistoryRV = (RecyclerView) v.findViewById(R.id.matchHistoryRV);
        matchHistoryRV.addItemDecoration(new VerticalSpaceItemDecoration(12));
        mLayoutManager = new LinearLayoutManager(getContext());
        matchHistoryRV.setLayoutManager(mLayoutManager);

        if(games == null || games.size() == 0) {
            ServiceRequest.getInstance(getActivity()).makeGetRecentMatchesRequest(
                    Commons.RECENT_MATCHES_REQUEST, region, summonerId + "", null, MatchHistoryFragment.this);
        }else{
            setAdapter();
        }

        reportGoogleAnalytics();
        return  v;
    }

    private void setAdapter() {
        if(adapter == null) {
            addAdsToNewsArray();
            adapter = new MatchHistoryRVAdapter(getContext(), games, R.layout.list_row_match_history_rv, recyclerViewClickListener);
            matchHistoryRV.setAdapter(adapter);
        } else {
            notifyDataSetChanged();
        }
    }

    private void notifyDataSetChanged() {
        if(adapter != null) {
            adapter.notifyDataSetChanged();;
        }
    }

    private MatchHistoryRVAdapter.RecyclerViewClickListener recyclerViewClickListener = new MatchHistoryRVAdapter.RecyclerViewClickListener() {
        @Override
        public void onRecyclerViewItemClicked(Game clickedItem, int position) {
            Intent i = new Intent(getContext(), MatchDetailActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(MatchDetailActivity.EXTRA_GAME, clickedItem);
            b.putLong(MatchDetailActivity.EXTRA_SUMMONER_ID, summonerId);
            b.putString(MatchDetailActivity.EXTRA_REGION, region);
            i.putExtras(b);
            startActivity(i);
        }
    };

    private void addAdsToNewsArray() {
        NativeAd nativeAd = AdUtils.getInstance().getCachedAd();
        if(nativeAd != null) {
            Game ad = new Game();
            ad.setAd(true);
            ad.setNativeAd(nativeAd);
            try {
                games.add(3, ad);
            } catch (Exception ignored) {}
            notifyDataSetChanged();
        }
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("MatchHistoryFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof RecentMatchesResponse){
            if(response != null) {
                RecentMatchesResponse recentMatchesResponse = (RecentMatchesResponse) response;
                games = recentMatchesResponse.getGames();
                setAdapter();
            }
        }
    }

    @Override
    public void onFailure(Object response) {
        if(response instanceof RecentMatchesResponse){
            Toast.makeText(getContext(), "Could not find player", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
