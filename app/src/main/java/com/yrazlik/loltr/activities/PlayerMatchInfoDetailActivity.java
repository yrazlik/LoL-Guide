package com.yrazlik.loltr.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.LeagueDto;
import com.yrazlik.loltr.data.PlayerStatsSummaryDto;
import com.yrazlik.loltr.data.SummonerDto;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.data.Entries;
import com.yrazlik.loltr.responseclasses.LeagueInfoResponse;
import com.yrazlik.loltr.responseclasses.StatsResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfoResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yrazlik on 3/12/15.
 */
public class PlayerMatchInfoDetailActivity extends ActionBarActivity implements ResponseListener {

    private String champImageUrl;
    private String userName;
    private long userId;
    private ImageView champImageIV;
    private RobotoTextView userNameTV, slashTV, userLevelTV, leagueTV, leagueNameTV, totalRankedWinCountTV, totalWonMatchCountTV, totalRankedLoseCountTV;
    private ImageView backButton, leagueBadge;
    private AdView adView;
    private RobotoTextView leagueInfoTV, matchHistoryTV;

    private String selectedRegion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            setContentView(R.layout.activity_player_match_info_detail);
        } else {
            setContentView(R.layout.activity_player_match_info_detail_noad);
        }
        adView = (AdView) findViewById(R.id.adView);
        if (adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        getExtras();
        leagueInfoTV = (RobotoTextView) findViewById(R.id.leagueInfoTV);
        matchHistoryTV = (RobotoTextView) findViewById(R.id.matchHistoryTV);
        Commons.underline(leagueInfoTV);
        Commons.underline(matchHistoryTV);
        champImageIV = (ImageView) findViewById(R.id.champImage);
        userNameTV = (RobotoTextView) findViewById(R.id.userName);
        backButton = (ImageView) findViewById(R.id.backButton);
        userLevelTV = (RobotoTextView) findViewById(R.id.userLevel);
        leagueBadge = (ImageView) findViewById(R.id.leagueBadge);
        leagueTV = (RobotoTextView) findViewById(R.id.leagueTV);
        leagueNameTV = (RobotoTextView) findViewById(R.id.leagueNameTV);
        totalRankedWinCountTV = (RobotoTextView) findViewById(R.id.totalRankedWinCountTV);
        totalRankedLoseCountTV = (RobotoTextView) findViewById(R.id.totalRankedLoseCountTV);
        totalWonMatchCountTV = (RobotoTextView) findViewById(R.id.totalWonMatchCountTV);
        slashTV = (RobotoTextView) findViewById(R.id.slashTV);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LolImageLoader.getInstance().loadImage(champImageUrl, champImageIV);
        userNameTV.setText(userName);


        ArrayList<String> pathParams = new ArrayList<String>();
        pathParams.add(selectedRegion);
        pathParams.add("v1.4");
        pathParams.add("summoner");
        pathParams.add("by-name");
        pathParams.add(userName);
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.SUMMONER_INFO_REQUEST, pathParams, queryParams, null, PlayerMatchInfoDetailActivity.this);

        ArrayList<String> pathParams2 = new ArrayList<String>();
        pathParams2.add(selectedRegion);
        pathParams2.add("v2.5");
        pathParams2.add("league");
        pathParams2.add("by-summoner");
        pathParams2.add(String.valueOf(userId));
        HashMap<String, String> queryParams2 = new HashMap<String, String>();
        queryParams2.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.LEAGUE_INFO_REQUEST, pathParams2, queryParams2, null, PlayerMatchInfoDetailActivity.this);

        ArrayList<String> pathParams3 = new ArrayList<String>();
        pathParams3.add(selectedRegion);
        pathParams3.add("v1.3");
        pathParams3.add("stats");
        pathParams3.add("by-summoner");
        pathParams3.add(String.valueOf(userId));
        pathParams3.add("summary");
        HashMap<String, String> queryParams3 = new HashMap<String, String>();
        queryParams3.put("api_key", Commons.API_KEY);

        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.STATS_REQUEST, pathParams3, queryParams3, null, PlayerMatchInfoDetailActivity.this);

    }


    private void getExtras() {
        userName = getIntent().getStringExtra("EXTRA_USERNAME");
        userName = userName.replaceAll("\\s", "");
        champImageUrl = getIntent().getStringExtra("EXTRA_CHAMP_IMAGE_URL");
        userId = getIntent().getLongExtra("EXTRA_USERID", 0);
        selectedRegion = getIntent().getStringExtra("SELECTED_REGION");
    }

    @Override
    public void onSuccess(Object response) {

        if (response instanceof SummonerInfoResponse) {
            SummonerInfoResponse resp = (SummonerInfoResponse) response;
            for (Map.Entry<String, SummonerDto> entry : resp.entrySet()) {
                if (entry.getValue() != null) {
                    userLevelTV.setText(String.valueOf(entry.getValue().getSummonerLevel()) + " " + getResources().getString(R.string.lvl));
                    userLevelTV.setVisibility(View.VISIBLE);
                }
                break;
            }

        } else if (response instanceof LeagueInfoResponse) {
            LeagueInfoResponse resp = (LeagueInfoResponse) response;
            for (Map.Entry<String, ArrayList<LeagueDto>> entry : resp.entrySet()) {
                if (entry.getValue() != null) {
                    try {
                        String leagueName = entry.getValue().get(0).getName();
                        String league = entry.getValue().get(0).getTier();
                        leagueTV.setText(getResources().getString(R.string.league) + ": " + league);
                        leagueNameTV.setText(leagueName);
                        leagueTV.setVisibility(View.VISIBLE);
                        leagueNameTV.setVisibility(View.VISIBLE);
                        leagueBadge.setBackgroundResource(getLeagueBadgeImage(league));
                        for (Entries e : entry.getValue().get(0).getEntries()) {
                            if (e.getPlayerOrTeamId().equals(String.valueOf(userId))) {
                                leagueTV.setText(getResources().getString(R.string.league) + ": " + league + " " + e.getDivision());
                                totalRankedWinCountTV.setText(e.getWins() + "");
                                slashTV.setText(" / ");
                                totalRankedLoseCountTV.setText(e.getLosses() + "");

                                break;
                            }
                        }
                    } catch (Exception e) {
                    }

                }
                break;
            }

        } else if (response instanceof StatsResponse) {

            StatsResponse resp = (StatsResponse) response;
            int wins = 0;
            if (resp != null && resp.getPlayerStatSummaries() != null) {
                for (PlayerStatsSummaryDto dto : resp.getPlayerStatSummaries()) {
                    if (dto.getPlayerStatSummaryType().equalsIgnoreCase("Unranked")) {
                        wins = dto.getWins();
                        break;
                    }
                }
            }
            totalWonMatchCountTV.setText(getString(R.string.wonMatchCount) + " " + wins + "");

        }

    }

    private int getLeagueBadgeImage(String league) {
        if (league.equalsIgnoreCase("BRONZE")) {
            return R.drawable.bronze_badge;
        } else if (league.equalsIgnoreCase("SILVER")) {
            return R.drawable.silver_badge;
        } else if (league.equalsIgnoreCase("GOLD")) {
            return R.drawable.gold_badge;
        } else if (league.equalsIgnoreCase("PLATINUM")) {
            return R.drawable.plat_badge;
        } else if (league.equalsIgnoreCase("DIAMOND")) {
            return R.drawable.diamond_badge;
        } else if (league.equalsIgnoreCase("CHALLENGER")) {
            return R.drawable.challenger_badge;
        } else if (league.equalsIgnoreCase("MASTER")) {
            return R.drawable.master_badge;
        } else {
            return R.drawable.unranked_badge;
        }
    }

    @Override
    public void onFailure(final Object response) {

        if(response != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (response instanceof LeagueInfoResponse || (response instanceof Integer && ((int)response) == Commons.LEAGUE_INFO_REQUEST)) {
                        leagueTV.setText(getResources().getString(R.string.unranked));
                        leagueTV.setVisibility(View.VISIBLE);
                        leagueBadge.setBackgroundResource(R.drawable.unranked_badge);
                        totalRankedWinCountTV.setText("0");
                        slashTV.setText(" / ");
                        totalRankedLoseCountTV.setText("0");
                        leagueNameTV.setText(getString(R.string.no_team));
                    }
                }
            });
        }

    }

    @Override
    public Context getContext() {
        return PlayerMatchInfoDetailActivity.this;
    }
}
