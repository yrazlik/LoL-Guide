package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.responseclasses.LeagueInfoResponse;
import com.yrazlik.loltr.responseclasses.RankedStatsResponse;
import com.yrazlik.loltr.responseclasses.RecentMatchesResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfo;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summoner_container, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            recentMatchesResponse = (RecentMatchesResponse) extras.getSerializable(MatchHistoryFragment.EXTRA_RECENTMATCHES);
            summonerInfo = (SummonerInfo) extras.getSerializable(SummonerOverviewFragment.EXTRA_SUMMONER_INFO);
            rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS);
            leagueInfoResponse = (LeagueInfoResponse) extras.getSerializable(SummonerOverviewFragment.EXTRA_LEAGUEINFO);
        }

        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(new SummonerPagerAdapter(getChildFragmentManager()));

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabs.setIndicatorColor(getResources().getColor(R.color.button_blue));
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getActivity().getResources().getDisplayMetrics());
        tabs.setTextSize(textSize);

        tabs.setIndicatorHeight(8);
        tabs.setViewPager(pager);


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
            }else {
                return getResources().getString(R.string.match_history);
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                Bundle args = new Bundle();
                args.putSerializable(SummonerOverviewFragment.EXTRA_SUMMONER_INFO, summonerInfo);
                args.putSerializable(MatchHistoryFragment.EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_RANKEDSTATS, rankedStatsResponse);
                args.putSerializable(SummonerOverviewFragment.EXTRA_LEAGUEINFO, leagueInfoResponse);
                SummonerOverviewFragment summonerOverviewFragment = new SummonerOverviewFragment();
                summonerOverviewFragment.setArguments(args);
                return summonerOverviewFragment;
            }else {
                MatchHistoryFragment matchHistoryFragment = new MatchHistoryFragment();
                Bundle args = new Bundle();
                args.putSerializable(MatchHistoryFragment.EXTRA_RECENTMATCHES, recentMatchesResponse);
                args.putLong(MatchHistoryFragment.EXTRA_SUMMONERID, summonerInfo.getId());
                matchHistoryFragment.setArguments(args);
                return matchHistoryFragment;
            }
        }
    }

    @Override
    public void reportGoogleAnalytics() {

    }
}