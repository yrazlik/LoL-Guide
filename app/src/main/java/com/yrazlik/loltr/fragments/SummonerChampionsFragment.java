package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.SummonerChampionsAdapter;
import com.yrazlik.loltr.data.ChampionStatsDto;
import com.yrazlik.loltr.responseclasses.RankedStatsResponse;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by yrazlik on 1/12/16.
 */
public class SummonerChampionsFragment extends BaseFragment{

    private TextView noWatchListTV;
    private ExpandableStickyListHeadersListView champList;
    private SummonerChampionsAdapter adapter;

    private RankedStatsResponse rankedStatsResponse;
    private List<ChampionStatsDto> champions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summoner_champions, container, false);
        noWatchListTV = (TextView)v.findViewById(R.id.noWatchListTV);
        champList = (ExpandableStickyListHeadersListView)v.findViewById(R.id.champList);

        Bundle extras = getArguments();
        if (extras != null) {
            rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS);
            if(rankedStatsResponse != null){
                champions = rankedStatsResponse.getChampions();
            }
        }

        if(champions != null && champions.size() > 0){
            ArrayList<Long> headerIds = new ArrayList<Long>();
            for(int i = 0; i < champions.size(); i++){
                ChampionStatsDto dto = champions.get(i);
                if(dto != null && !headerIds.contains(dto.getId())){
                    try {
                        headerIds.add(Long.parseLong(dto.getId() + ""));
                    }catch (Exception ignored){}
                }
            }
            noWatchListTV.setVisibility(View.GONE);
            champList.setVisibility(View.VISIBLE);
            adapter = new SummonerChampionsAdapter(getActivity(), R.layout.list_row_summoner_champions, champions);
            champList.setAdapter(adapter);

            for(Long id : headerIds){
                if(id != null) {
                    champList.collapse(id);
                }
            }


            champList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
                @Override
                public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                    if (champList.isHeaderCollapsed(headerId)) {
                        champList.expand(headerId);
                        header.findViewById(R.id.downArrow).setBackgroundResource(R.drawable.arrow_up);
                    } else {
                        champList.collapse(headerId);
                        header.findViewById(R.id.downArrow).setBackgroundResource(R.drawable.arrow_down);
                    }
                }
            });
        }else{
            noWatchListTV.setVisibility(View.VISIBLE);
            champList.setVisibility(View.GONE);
        }


        return v;
    }

    @Override
    public void reportGoogleAnalytics() {

    }
}
