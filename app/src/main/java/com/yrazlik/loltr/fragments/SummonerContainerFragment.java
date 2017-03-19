package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.ChampionStatsDto;
import com.yrazlik.loltr.responseclasses.LeagueInfoResponse;
import com.yrazlik.loltr.responseclasses.RankedStatsResponse;
import com.yrazlik.loltr.responseclasses.RecentMatchesResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfo;
import com.yrazlik.loltr.view.PagerSlidingTabStrip;


/**
 * Created by yrazlik on 1/12/16.
 */
public class SummonerContainerFragment extends BaseFragment{

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    private RecentMatchesResponse recentMatchesResponse;
    private SummonerInfo summonerInfo;
    private RankedStatsResponse rankedStatsResponse;
    private LeagueInfoResponse leagueInfoResponse;
    private ChampionStatsDto averageStats;

    private CardView loadingBg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_summoner_container, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            recentMatchesResponse = (RecentMatchesResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES);
            summonerInfo = (SummonerInfo) extras.getSerializable(SummonerOverviewFragment.EXTRA_SUMMONER_INFO);
            rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS);
            leagueInfoResponse = (LeagueInfoResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_LEAGUEINFO);
            averageStats = (ChampionStatsDto) extras.getSerializable(SummonerOverviewFragment.EXTRA_AVERAGESTATS);
        }

        loadingBg = (CardView) v.findViewById(R.id.loadingBg);
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pager.setAdapter(new SummonerPagerAdapter(getChildFragmentManager()));

                tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
                tabs.setIndicatorColor(getResources().getColor(R.color.white));
                tabs.setBackgroundColor(getResources().getColor(R.color.tab_color));
                tabs.setDividerColor(getResources().getColor(R.color.white));
                tabs.setTextColor(getResources().getColor(R.color.white));
                DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getActivity().getResources().getDisplayMetrics());
                tabs.setTextSize(textSize);
                tabs.setIndicatorHeight(8);
                tabs.setViewPager(pager);
                loadingBg.setVisibility(View.GONE);
            }
        }, 500);
        return v;
    }




    public class SummonerPagerAdapter extends FragmentPagerAdapter {

        public SummonerPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return getResources().getString(R.string.overview);
            }else if(position == 1){
                return getResources().getString(R.string.match_history);
            }else if(position == 2){
                return getResources().getString(R.string.champions);
            }else{
                return getResources().getString(R.string.statistics);
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                Bundle args = new Bundle();
                args.putSerializable(SummonerOverviewFragment.EXTRA_SUMMONER_INFO, summonerInfo);
                args.putSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS, rankedStatsResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_LEAGUEINFO, leagueInfoResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_AVERAGESTATS, averageStats);
                SummonerOverviewFragment summonerOverviewFragment = new SummonerOverviewFragment();
                summonerOverviewFragment.setArguments(args);
                return summonerOverviewFragment;
            }else if(position == 1){
                MatchHistoryFragment matchHistoryFragment = new MatchHistoryFragment();
                Bundle args = new Bundle();
                args.putSerializable(SummonerOverviewFragment.EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putLong(MatchHistoryFragment.EXTRA_SUMMONERID, summonerInfo.getId());
                matchHistoryFragment.setArguments(args);
                return matchHistoryFragment;
            }else if(position == 2){
                SummonerChampionsFragment summonerChampionsFragment = new SummonerChampionsFragment();
                Bundle args = new Bundle();
                args.putSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS, rankedStatsResponse);
                summonerChampionsFragment.setArguments(args);
                return summonerChampionsFragment;
            }else{
                SummonerStatisticsFragment statisticsFragment = new SummonerStatisticsFragment();
                Bundle args = new Bundle();
                args.putSerializable(SummonerStatisticsFragment.EXTRA_STATISTICS, rankedStatsResponse);
                args.putSerializable(SummonerStatisticsFragment.EXTRA_AVERAGE_STATS, averageStats);
                statisticsFragment.setArguments(args);
                return statisticsFragment;
            }
        }
    }

    @Override
    public void reportGoogleAnalytics() {

    }
}
