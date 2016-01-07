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
import com.yrazlik.loltr.activities.MatchDetailActivity;
import com.yrazlik.loltr.adapters.MatchHistoryAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.RecentMatchesResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.List;

/**
 * Created by yrazlik on 1/5/16.
 */
public class MatchHistoryFragment extends BaseFragment implements ResponseListener{

    private long summonerId;
    private String region = Commons.SELECTED_REGION;
    private ListView matchHistoryLV;
    private MatchHistoryAdapter adapter;
    private List<Game> games;

    public void setSummonerId(long summonerId){
        this.summonerId = summonerId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_history, container, false);
        matchHistoryLV = (ListView) v.findViewById(R.id.matchHistoryLV);
        matchHistoryLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Game game = adapter.getItem(position);
              /*  MatchDetailFragment matchDetailFragment = new MatchDetailFragment();
                matchDetailFragment.setGame(game);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_BOTTOM_WITH_POPSTACK);
                ft.replace(R.id.content_frame, matchDetailFragment).addToBackStack(Commons.MATCH_DETAIL_FRAGMENT).commit();*/

                Intent i = new Intent(getContext(), MatchDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(MatchDetailActivity.EXTRA_GAME, game);
                b.putLong(MatchDetailActivity.EXTRA_SUMMONER_ID, summonerId);
                b.putString(MatchDetailActivity.EXTRA_REGION, region);
                i.putExtras(b);
                startActivity(i);
            }
        });

        if(games == null || games.size() == 0) {
            ServiceRequest.getInstance(getActivity()).makeGetRecentMatchesRequest(
                    Commons.RECENT_MATCHES_REQUEST, region, summonerId + "", null, MatchHistoryFragment.this);
        }else{
            adapter = new MatchHistoryAdapter(getContext(), R.layout.list_row_match_history, games, summonerId);
            matchHistoryLV.setAdapter(adapter);
        }

        reportGoogleAnalytics();
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
            if(response != null) {
                RecentMatchesResponse recentMatchesResponse = (RecentMatchesResponse) response;
                games = recentMatchesResponse.getGames();
                adapter = new MatchHistoryAdapter(getContext(), R.layout.list_row_match_history, recentMatchesResponse.getGames(), recentMatchesResponse.getSummonerId());
                matchHistoryLV.setAdapter(adapter);
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
