package com.yrazlik.loltr.fragments;

import android.content.Context;
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
import com.yrazlik.loltr.adapters.MatchHistoryAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.RecentMatchesResponse;
import com.yrazlik.loltr.service.ServiceRequest;

/**
 * Created by yrazlik on 1/5/16.
 */
public class MatchHistoryFragment extends BaseFragment implements ResponseListener{

    private String summonerId = "3980715";
    private String region = "tr";
    private ListView matchHistoryLV;
    private MatchHistoryAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_history, container, false);
        matchHistoryLV = (ListView) v.findViewById(R.id.matchHistoryLV);
        matchHistoryLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game game = adapter.getItem(position);
            }
        });

        ServiceRequest.getInstance(getActivity()).makeGetRecentMatchesRequest(
                Commons.RECENT_MATCHES_REQUEST, region, summonerId, null, MatchHistoryFragment.this);

        return  v;
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
            RecentMatchesResponse recentMatchesResponse = (RecentMatchesResponse) response;
            adapter = new MatchHistoryAdapter(getContext(), R.layout.list_row_match_history, recentMatchesResponse.getGames(), recentMatchesResponse.getSummonerId());
            matchHistoryLV.setAdapter(adapter);
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
