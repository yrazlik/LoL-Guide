package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.StatisticsAdapter;
import com.yrazlik.loltr.data.AggregatedStatsDto;
import com.yrazlik.loltr.data.ChampionStatsDto;
import com.yrazlik.loltr.data.Statistics;
import com.yrazlik.loltr.responseclasses.RankedStatsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrazlik on 1/12/16.
 */
public class SummonerStatisticsFragment extends BaseFragment{

    private RelativeLayout noRankedRL, rankedStatisticsRL;
    private TextView title;
    private ListView statisticsLV;
    private StatisticsAdapter statisticsAdapter;

    private RankedStatsResponse rankedStatsResponse;
    private ChampionStatsDto averageStats;


    public static final String EXTRA_STATISTICS = "com.yrazlik.loltr.fragments.summonerstatisticsfragment.extrastatistics";
    public static final String EXTRA_AVERAGE_STATS = "com.yrazlik.loltr.fragments.summonerstatisticsfragment.extraaveragestats";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_summoner_statistics, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(EXTRA_STATISTICS);
            averageStats = (ChampionStatsDto)extras.getSerializable(EXTRA_AVERAGE_STATS);
        }

        noRankedRL = (RelativeLayout) v.findViewById(R.id.noRankedRL);
        rankedStatisticsRL = (RelativeLayout) v.findViewById(R.id.rankedStatisticsRL);
        title = (TextView) v.findViewById(R.id.title);
        statisticsLV = (ListView) v.findViewById(R.id.statisticsLV);

        if(rankedStatsResponse != null){
            noRankedRL.setVisibility(View.GONE);
            rankedStatisticsRL.setVisibility(View.VISIBLE);
            if(averageStats != null){
                populateStatistics();
            }else{
                rankedStatisticsRL.setVisibility(View.GONE);
                noRankedRL.setVisibility(View.VISIBLE);
            }
        }else{
            rankedStatisticsRL.setVisibility(View.GONE);
            noRankedRL.setVisibility(View.VISIBLE);
        }

        return v;
    }


    private void populateStatistics(){
        if(averageStats != null){
            List<Statistics> statistics = new ArrayList<>();
            AggregatedStatsDto stats = averageStats.getStats();
            if(stats != null){
                statistics.add(new Statistics(getResources().getString(R.string.games_played), stats.getTotalSessionsPlayed() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.games_won), stats.getTotalSessionsWon() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.kill_death), "" + "", true));
                statistics.add(new Statistics(getResources().getString(R.string.kills), stats.getTotalChampionKills() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.deaths), stats.getTotalDeathsPerSession() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.assists), stats.getTotalAssists() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.penta_kills), stats.getTotalPentaKills() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.quadra_kills), stats.getTotalQuadraKills() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.triple_kills), stats.getTotalTripleKills() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.double_kills), stats.getTotalDoubleKills() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.killing_sprees), stats.getKillingSpree() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.most_kills_per_session), stats.getMostChampionKillsPerSession() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.most_deaths), stats.getMaxNumDeaths() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.earnings), "" + "", true));
                statistics.add(new Statistics(getResources().getString(R.string.gold_earned), stats.getTotalGoldEarned() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.minions_killed), stats.getTotalMinionKills() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.neutral_minions_killed), stats.getTotalNeutralMinionsKilled() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.damagePart), "" + "", true));
                statistics.add(new Statistics(getResources().getString(R.string.damage_taken), stats.getTotalDamageTaken() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.damage_dealt), stats.getTotalDamageDealt() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.physical_damage_dealt), stats.getTotalPhysicalDamageDealt() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.magic_damage_dealt), stats.getTotalMagicDamageDealt() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.healing_done), stats.getTotalHeal() + "", false));
                statistics.add(new Statistics(getResources().getString(R.string.largest_critical_strike), stats.getMaxLargestCriticalStrike() + "", false));
                statisticsLV.setAdapter(new StatisticsAdapter(getActivity(), R.layout.list_row_statistics, statistics));

            }
        }
    }

    @Override
    public void reportGoogleAnalytics() {

    }
}
