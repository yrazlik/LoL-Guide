package com.yrazlik.loltr.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.AggregatedStatsDto;
import com.yrazlik.loltr.data.ChampGameAnalysis;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.ChampionStatsDto;
import com.yrazlik.loltr.data.Entries;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.data.LeagueDto;
import com.yrazlik.loltr.data.Stats;
import com.yrazlik.loltr.responseclasses.LeagueInfoResponse;
import com.yrazlik.loltr.responseclasses.RankedStatsResponse;
import com.yrazlik.loltr.responseclasses.RecentMatchesResponse;
import com.yrazlik.loltr.responseclasses.SummonerInfo;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.FadeInNetworkImageView;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by yrazlik on 1/11/16.
 */
public class SummonerOverviewFragment extends BaseFragment {

    ScrollView parent;
    //header part
    private CircularImageView profileIconIV;
    private RobotoTextView summonerNameTV;
    private RobotoTextView regionLevelTV;

    //averages part
    private CardView kdaRL;
    private RobotoTextView averageTV;
    private LinearLayout averagesLL;
    private RelativeLayout kdaContainer;
    private RobotoTextView kdaStatsTV;
    private RobotoTextView kdaStatsStringTV;
    private RelativeLayout minionsContainer;
    private RobotoTextView minionsStatsTV;
    private RobotoTextView minionsStatsStringTV;
    private RelativeLayout winRateContainer;
    private RobotoTextView winRateStatsTV;
    private RobotoTextView winRateStatsStringTV;

    //most played part
    private LinearLayout mostPlayedLL;
    private RobotoTextView mostPlayedTV;
    private CircularImageView mostPlayedIV1;
    private RobotoTextView mostPlayedTV1;
    private RobotoTextView kdaTV1;
    private RobotoTextView winRateTV1;
    private CircularImageView mostPlayedIV2;
    private RobotoTextView mostPlayedTV2;
    private RobotoTextView kdaTV2;
    private RobotoTextView winRateTV2;
    private CircularImageView mostPlayedIV3;
    private RobotoTextView mostPlayedTV3;
    private RobotoTextView kdaTV3;
    private RobotoTextView winRateTV3;

    //ranked part
    private CardView rankedRL;
    private RobotoTextView rankedTV;
    private ImageView rankedLeagueIV;
    private RobotoTextView soloDuoTV;
    private RobotoTextView leagueNameTV;
    private RobotoTextView winLoseLPTV;
    private ImageView ranked5v5IV;
    private RobotoTextView team5v5rankTV;
    private RobotoTextView team5v5lpTV;
    private RobotoTextView team5v5nameTV;
    private ImageView ranked3v3IV;
    private RobotoTextView team3v3rankTV;
    private RobotoTextView team3v3lpTV;
    private RobotoTextView team3v3nameTV;

    public static final String EXTRA_RECENTMATCHES = "com.yrazlik.loltr.fragments.SummonerOverviewFragment.EXTRA_RECENTMATCHES";
    public static final String EXTRA_SUMMONER_INFO = "com.yrazlik.loltr.fragments.SummonerOverviewFragment.EXTRA_SUMMONER_INFO";
    public static final String EXTRA_RANKEDSTATS = "com.yrazlik.loltr.fragments.SummonerOverviewFragment.EXTRA_RANKEDSTATS";
    public static final String EXTRA_LEAGUEINFO = "com.yrazlik.loltr.fragments.SummonerOverviewFragment.EXTRA_LEAGUEINFO";
    public static final String EXTRA_AVERAGESTATS = "com.yrazlik.loltr.fragments.SummonerOverviewFragment.EXTRA_AVERAGESTATS";
    public static final String EXTRA_REGION = "com.yrazlik.loltr.fragments.SummonerOverviewFragment.EXTRA_REGION";

    private RecentMatchesResponse recentMatchesResponse;
    private SummonerInfo summonerInfo;
    private RankedStatsResponse rankedStatsResponse;
    private LeagueInfoResponse leagueInfoResponse;
    private ChampionStatsDto averageStats;
    private String kdaString = "?/?/?", minionsString = "???", winRateString = "?%";
    private String region = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_summoner_overview, container, false);

            Bundle extras = getArguments();
            if (extras != null) {
                recentMatchesResponse = (RecentMatchesResponse) extras.getSerializable(EXTRA_RECENTMATCHES);
                summonerInfo = (SummonerInfo) extras.getSerializable(EXTRA_SUMMONER_INFO);
                rankedStatsResponse = (RankedStatsResponse) extras.getSerializable(EXTRA_RANKEDSTATS);
                leagueInfoResponse = (LeagueInfoResponse) extras.getSerializable(EXTRA_LEAGUEINFO);
                averageStats = (ChampionStatsDto) extras.getSerializable(EXTRA_AVERAGESTATS);
                region = extras.getString(EXTRA_REGION, "");
            }

            initUI(rootView);
        }

        return rootView;
    }

    private void initUI(View v) {

        //header part
        parent = (ScrollView) v.findViewById(R.id.parent);
        profileIconIV = (CircularImageView) v.findViewById(R.id.profileIconIV);
        summonerNameTV = (RobotoTextView) v.findViewById(R.id.summonerNameTV);
        regionLevelTV = (RobotoTextView) v.findViewById(R.id.regionLevelTV);

        //averages part
        kdaRL = (CardView) v.findViewById(R.id.kdaRL);
        kdaRL.setVisibility(View.VISIBLE);
        averageTV = (RobotoTextView) v.findViewById(R.id.averageTV);
        averagesLL = (LinearLayout) v.findViewById(R.id.averagesLL);
        kdaContainer = (RelativeLayout) v.findViewById(R.id.kdaContainer);
        kdaStatsTV = (RobotoTextView) v.findViewById(R.id.kdaStatsTV);
        kdaStatsStringTV = (RobotoTextView) v.findViewById(R.id.kdaStatsStringTV);
        minionsContainer = (RelativeLayout) v.findViewById(R.id.minionsContainer);
        minionsStatsTV = (RobotoTextView) v.findViewById(R.id.minionsStatsTV);
        minionsStatsStringTV = (RobotoTextView) v.findViewById(R.id.minionsStatsStringTV);
        winRateContainer = (RelativeLayout) v.findViewById(R.id.winRateContainer);
        winRateStatsTV = (RobotoTextView) v.findViewById(R.id.winRateStatsTV);
        winRateStatsStringTV = (RobotoTextView) v.findViewById(R.id.winRateStatsStringTV);

        Commons.underline(averageTV);

        //most played part
        mostPlayedLL = (LinearLayout) v.findViewById(R.id.mostPlayedLL);
        mostPlayedTV = (RobotoTextView) v.findViewById(R.id.mostPlayedTV);
        mostPlayedIV1 = (CircularImageView) v.findViewById(R.id.mostPlayedIV1);
        mostPlayedTV1 = (RobotoTextView) v.findViewById(R.id.mostPlayedTV1);
        kdaTV1 = (RobotoTextView) v.findViewById(R.id.kdaTV1);
        winRateTV1 = (RobotoTextView) v.findViewById(R.id.winRateTV1);
        mostPlayedIV2 = (CircularImageView) v.findViewById(R.id.mostPlayedIV2);
        mostPlayedTV2 = (RobotoTextView) v.findViewById(R.id.mostPlayedTV2);
        kdaTV2 = (RobotoTextView) v.findViewById(R.id.kdaTV2);
        winRateTV2 = (RobotoTextView) v.findViewById(R.id.winRateTV2);
        mostPlayedIV3 = (CircularImageView) v.findViewById(R.id.mostPlayedIV3);
        mostPlayedTV3 = (RobotoTextView) v.findViewById(R.id.mostPlayedTV3);
        kdaTV3 = (RobotoTextView) v.findViewById(R.id.kdaTV3);
        winRateTV3 = (RobotoTextView) v.findViewById(R.id.winRateTV3);

        Commons.underline(mostPlayedTV);

        //ranked part
        rankedRL = (CardView) v.findViewById(R.id.rankedRL);
        rankedTV = (RobotoTextView) v.findViewById(R.id.rankedTV);
        rankedLeagueIV = (ImageView) v.findViewById(R.id.rankedLeagueIV);
        soloDuoTV = (RobotoTextView) v.findViewById(R.id.soloDuoTV);
        leagueNameTV = (RobotoTextView) v.findViewById(R.id.leagueNameTV);
        winLoseLPTV = (RobotoTextView) v.findViewById(R.id.winLoseLPTV);
        ranked5v5IV = (ImageView) v.findViewById(R.id.ranked5v5IV);
        team5v5rankTV = (RobotoTextView) v.findViewById(R.id.team5v5rankTV);
        team5v5lpTV = (RobotoTextView) v.findViewById(R.id.team5v5lpTV);
        team5v5nameTV = (RobotoTextView) v.findViewById(R.id.team5v5nameTV);
        ranked3v3IV = (ImageView) v.findViewById(R.id.ranked3v3IV);
        team3v3rankTV = (RobotoTextView) v.findViewById(R.id.team3v3rankTV);
        team3v3lpTV = (RobotoTextView) v.findViewById(R.id.team3v3lpTV);
        team3v3nameTV = (RobotoTextView) v.findViewById(R.id.team3v3nameTV);

        Commons.underline(rankedTV);

        //populate header part
        if (summonerInfo != null) {
            LolImageLoader.getInstance().loadImage(Commons.PROFILE_ICON_BASE_URL + summonerInfo.getProfileIconId() + ".png", profileIconIV);
            summonerNameTV.setText(summonerInfo.getName());
            regionLevelTV.setText(getResources().getString(R.string.region) + " " + region.toUpperCase() + ", " + getString(R.string.lvl) + ": " + summonerInfo.getSummonerLevel());
        }

        if (rankedStatsResponse == null) {
            //populate averages part
            calculateKDAMinionsAndWinRateStringsForUnranked();
            kdaStatsTV.setText(kdaString);
            minionsStatsTV.setText(minionsString);
            winRateStatsTV.setText(winRateString);

            //populate most played part
            populateMostPlayedPartForUnranked();
        } else {
            //populate averages part
            calculateKDAMinionsAndWinRateStringsForRanked();
            kdaStatsTV.setText(kdaString);
            minionsStatsTV.setText(minionsString);
            winRateStatsTV.setText(winRateString);

            //populate most played part
            populateMostPlayedPartForRanked();

            populateRankedPart();

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
        }else if(league.equalsIgnoreCase("MASTER")){
            return R.drawable.master_badge;
        } else {
            return R.drawable.unranked_badge;
        }
    }

    private void populateRankedPart() {
        if (leagueInfoResponse != null) {
            for (Map.Entry<String, ArrayList<LeagueDto>> entry : leagueInfoResponse.entrySet()) {
                if (entry.getValue() != null) {
                    try {
                        boolean receivedSoloInfo = false, received3v3Info = false, received5v5Info = false;

                        for (LeagueDto dto : entry.getValue()) {
                            String queue = dto.getQueue();
                            if (queue != null && queue.equalsIgnoreCase("RANKED_SOLO_5x5")) {

                                String league = dto.getName();
                                String tier = dto.getTier();
                                rankedLeagueIV.setBackgroundResource(getLeagueBadgeImage(tier));

                                if (!receivedSoloInfo) {
                                    receivedSoloInfo = true;
                                    ArrayList<Entries> entries = dto.getEntries();
                                    if (entries != null && entries.size() > 0) {
                                        Entries en = entries.get(0);
                                        if (en != null) {
                                            try {
                                                String division = en.getDivision();
                                                if (tier != null && division != null && league != null) {
                                                    leagueNameTV.setText(tier + " " + division + ", " + league);
                                                } else if (tier != null && division != null) {
                                                    leagueNameTV.setText(tier + " " + division);
                                                }
                                            } catch (Exception ignored) {
                                            }
                                            try {
                                                winLoseLPTV.setText("W: " + en.getWins() + ", L: " + en.getLosses() + ", LP: " + en.getLeaguePoints());
                                            } catch (Exception ignored) {
                                            }
                                        }
                                    }
                                }
                            } else if (queue != null && queue.equalsIgnoreCase("RANKED_TEAM_3x3")) {
                                if (!received3v3Info) {
                                    received3v3Info = true;
                                    String league = dto.getName();
                                    String tier = dto.getTier();
                                    ranked3v3IV.setBackgroundResource(getLeagueBadgeImage(tier));

                                    ArrayList<Entries> entries = dto.getEntries();
                                    if (entries != null && entries.size() > 0) {
                                        Entries en = entries.get(0);
                                        if (en != null) {
                                            try {
                                                String division = en.getDivision();
                                                if (tier != null && division != null && league != null) {
                                                    team3v3rankTV.setText(tier + " " + division + ", " + league);
                                                } else if (tier != null && division != null) {
                                                    team3v3rankTV.setText(tier + " " + division);
                                                }
                                            } catch (Exception ignored) {
                                            }
                                            try {
                                                team3v3lpTV.setText(en.getLeaguePoints() + " LP");
                                            } catch (Exception ignored) {
                                            }

                                            try {
                                                team3v3nameTV.setText(en.getPlayerOrTeamName());
                                            } catch (Exception ignored) {
                                            }
                                        }
                                    }
                                }
                            } else if (queue != null && queue.equalsIgnoreCase("RANKED_TEAM_5x5")) {
                                if (!received5v5Info) {
                                    received5v5Info = true;
                                    String league = dto.getName();
                                    String tier = dto.getTier();
                                    ranked5v5IV.setBackgroundResource(getLeagueBadgeImage(tier));

                                    ArrayList<Entries> entries = dto.getEntries();
                                    if (entries != null && entries.size() > 0) {
                                        Entries en = entries.get(0);
                                        if (en != null) {
                                            try {
                                                String division = en.getDivision();
                                                if (tier != null && division != null && league != null) {
                                                    team5v5rankTV.setText(tier + " " + division + ", " + league);
                                                } else if (tier != null && division != null) {
                                                    team5v5rankTV.setText(tier + " " + division);
                                                }
                                            } catch (Exception ignored) {
                                            }
                                            try {
                                                team5v5lpTV.setText(en.getLeaguePoints() + " LP");
                                            } catch (Exception ignored) {
                                            }

                                            try {
                                                team5v5nameTV.setText(en.getPlayerOrTeamName());
                                            } catch (Exception ignored) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ignored) {
                    }

                }
                break;
            }
        }
    }

    private void calculateKDAMinionsAndWinRateStringsForRanked() {
        if (averageStats != null && averageStats.getStats() != null) {
            AggregatedStatsDto stats = averageStats.getStats();
            kdaString = ((int) (Math.round((double) stats.getTotalChampionKills() / (double) (stats.getTotalSessionsPlayed())))) + "/" + ((int) (Math.round((double) stats.getTotalDeathsPerSession() / (double) (stats.getTotalSessionsPlayed())))) + "/" + ((int) (Math.round((double) stats.getTotalAssists() / (double) (stats.getTotalSessionsPlayed())))) + "";
            minionsString = (int) (Math.round((double) stats.getTotalMinionKills() / (double) stats.getTotalSessionsPlayed())) + "";
            winRateString = ((int) ((double) stats.getTotalSessionsWon() / (double) (stats.getTotalSessionsPlayed()) * 100)) + "%";
        } else {
            if (rankedStatsResponse != null) {
                int totalPlayed = 0, totalWon = 0, totalKill = 0, totalDeath = 0, totalAssist = 0, minionCount = 0;
                if (rankedStatsResponse.getChampions() != null) {
                    for (ChampionStatsDto dto : rankedStatsResponse.getChampions()) {
                        AggregatedStatsDto stats = dto.getStats();
                        if (stats != null) {
                            totalWon += stats.getTotalSessionsWon();
                            totalKill += stats.getTotalChampionKills();
                            totalDeath += stats.getTotalDeathsPerSession();
                            totalAssist += stats.getTotalAssists();
                            totalPlayed += stats.getTotalSessionsPlayed();
                            minionCount += stats.getTotalMinionKills();
                        }
                    }
                    kdaString = (int) (Math.round((double) totalKill / (double) totalPlayed)) + "/" + (int) (Math.round((double) totalDeath / (double) totalPlayed)) + "/" + (int) (Math.round((double) totalAssist / (double) totalPlayed));
                    minionsString = (int) (Math.round((double) minionCount / (double) totalPlayed)) + "";
                    winRateString = ((int) ((double) totalWon / (double) (totalPlayed) * 100)) + "%";
                }

            }
        }
    }



    private void populateMostPlayedPartForRanked() {
        if (rankedStatsResponse != null) {
            List<ChampionStatsDto> champions = rankedStatsResponse.getChampions();
            if (champions != null && champions.size() > 0) {
                String champ1ImageUrl = "", champ2ImageUrl = "", champ3ImageUrl = "";
                Integer champ1Id = null, champ2Id = null, champ3Id = null;

                if (champions != null) {
                    if (champions.size() >= 3) {
                        champ1Id = champions.get(0).getId();
                        champ2Id = champions.get(1).getId();
                        champ3Id = champions.get(2).getId();
                    } else if (champions.size() == 2) {
                        champ1Id = champions.get(0).getId();
                        champ2Id = champions.get(1).getId();
                    } else if (champions.size() == 1) {
                        champ1Id = champions.get(0).getId();
                    }
                }

                String champ1Name = "???", champ2Name = "???", champ3Name = "???";
                if (Commons.allChampions != null && Commons.allChampions.size() > 0) {
                    for (Champion champ : Commons.allChampions) {
                        if (champ1Id != null && champ.getId() == champ1Id) {
                            champ1ImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            champ1Name = champ.getChampionName();
                        } else if ((champ2Id != null) && (champ.getId() == champ2Id)) {
                            champ2ImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            champ2Name = champ.getChampionName();
                        } else if ((champ3Id != null) && (champ.getId() == champ3Id)) {
                            champ3ImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            champ3Name = champ.getChampionName();
                        }
                    }
                }

                try {
                    LolImageLoader.getInstance().loadImage(champ1ImageUrl, mostPlayedIV1);
                    mostPlayedTV1.setText(champ1Name);
                    kdaTV1.setText((int)Math.round((double)(champions.get(0).getStats().getTotalChampionKills()/(double)(champions.get(0).getStats().getTotalSessionsPlayed())))
                            + "/" + ((int)Math.round((double)(champions.get(0).getStats().getTotalDeathsPerSession()/(double)(champions.get(0).getStats().getTotalSessionsPlayed())))) +
                            "/" + ((int)Math.round((double)(champions.get(0).getStats().getTotalAssists()/(double)(champions.get(0).getStats().getTotalSessionsPlayed())))));
                    winRateTV1.setText("W: " + champions.get(0).getStats().getTotalSessionsWon() + " (" + (int) Math.round(((double) champions.get(0).getStats().getTotalSessionsWon() / (double) champions.get(0).getStats().getTotalSessionsPlayed()) * 100) + "%)");
                } catch (Exception ignored) {
                }

                try {
                    LolImageLoader.getInstance().loadImage(champ2ImageUrl, mostPlayedIV2);
                    mostPlayedTV2.setText(champ2Name);
                    kdaTV2.setText(((int)Math.round((double)(champions.get(1).getStats().getTotalChampionKills()/(double)(champions.get(1).getStats().getTotalSessionsPlayed())))
                            + "/" + ((int)Math.round((double)(champions.get(1).getStats().getTotalDeathsPerSession()/(double)(champions.get(1).getStats().getTotalSessionsPlayed())))) +
                            "/" + ((int)Math.round((double)(champions.get(1).getStats().getTotalAssists()/(double)(champions.get(1).getStats().getTotalSessionsPlayed()))))));
                    winRateTV2.setText("W: " + champions.get(1).getStats().getTotalSessionsWon() + " (" + (int) Math.round(((double) champions.get(1).getStats().getTotalSessionsWon() / (double) champions.get(1).getStats().getTotalSessionsPlayed()) * 100) + "%)");
                } catch (Exception ignored) {
                }

                try {
                    LolImageLoader.getInstance().loadImage(champ3ImageUrl, mostPlayedIV3);
                    mostPlayedTV3.setText(champ3Name);
                    kdaTV3.setText(((int)Math.round((double)(champions.get(2).getStats().getTotalChampionKills()/(double)(champions.get(2).getStats().getTotalSessionsPlayed())))
                            + "/" + ((int)Math.round((double)(champions.get(2).getStats().getTotalDeathsPerSession()/(double)(champions.get(2).getStats().getTotalSessionsPlayed())))) +
                            "/" + ((int)Math.round((double)(champions.get(2).getStats().getTotalAssists()/(double)(champions.get(2).getStats().getTotalSessionsPlayed()))))));
                    winRateTV3.setText("W: " + champions.get(2).getStats().getTotalSessionsWon() + " (" + (int) Math.round(((double) champions.get(2).getStats().getTotalSessionsWon() / (double) champions.get(2).getStats().getTotalSessionsPlayed()) * 100) + "%)");
                } catch (Exception ignored) {
                }
            }
        }

    }

    private void calculateKDAMinionsAndWinRateStringsForUnranked() {
        int minionsKilled = 0, winCount = 0, loseCount = 0, killCount = 0, deathCount = 0, assistCount = 0;
        if (recentMatchesResponse != null) {
            List<Game> games = recentMatchesResponse.getGames();
            if (games != null && games.size() > 0) {
                for (Game game : games) {
                    Stats stats = game.getStats();
                    if (stats != null) {
                        minionsKilled += stats.getMinionsKilled();
                        if (stats.isWin()) {
                            winCount++;
                        } else {
                            loseCount++;
                        }
                        killCount += stats.getChampionsKilled();
                        deathCount += stats.getNumDeaths();
                        assistCount += stats.getAssists();
                    }
                }

                if (winCount + loseCount != 0) {
                    minionsKilled = minionsKilled / (winCount + loseCount);
                    minionsString = String.valueOf(minionsKilled);
                    killCount = killCount / (winCount + loseCount);
                    deathCount = deathCount / (winCount + loseCount);
                    assistCount = assistCount / (winCount + loseCount);
                    kdaString = killCount + "/" + deathCount + "/" + assistCount;
                    winRateString = ((int) ((double) winCount / (double) (winCount + loseCount) * 100)) + "%";
                }

            } else {
                kdaRL.setVisibility(View.GONE);
            }
        } else {
            kdaRL.setVisibility(View.GONE);
        }
    }

    private void populateMostPlayedPartForUnranked() {
        if (recentMatchesResponse != null) {
            ArrayList<ChampGameAnalysis> champGameAnalysises = new ArrayList<>();
            List<Game> games = recentMatchesResponse.getGames();
            if (games != null && games.size() > 0) {
                for (Game game : games) {
                    ChampGameAnalysis gameAnalysis = new ChampGameAnalysis();
                    ChampGameAnalysis existingAnalysis = null;
                    for (ChampGameAnalysis analysis : champGameAnalysises) {
                        if (analysis.getChampId() == game.getChampionId()) {
                            existingAnalysis = analysis;
                            break;
                        }
                    }

                    Stats stats = game.getStats();
                    if (stats != null) {
                        if (existingAnalysis != null) {
                            existingAnalysis.setKillCount(existingAnalysis.getKillCount() + stats.getChampionsKilled());
                            existingAnalysis.setDeathCount(existingAnalysis.getDeathCount() + stats.getNumDeaths());
                            existingAnalysis.setAssistCount(existingAnalysis.getAssistCount() + stats.getAssists());
                            if (stats.isWin()) {
                                existingAnalysis.setWinCount(existingAnalysis.getWinCount() + 1);
                            } else {
                                existingAnalysis.setLoseCount(existingAnalysis.getLoseCount() + 1);
                            }
                            existingAnalysis.setTotalPlayCount(existingAnalysis.getWinCount() + existingAnalysis.getLoseCount());
                        } else {
                            ChampGameAnalysis analysis = new ChampGameAnalysis();
                            analysis.setDeathCount(stats.getNumDeaths());
                            if (stats.isWin()) {
                                analysis.setWinCount(analysis.getWinCount() + 1);
                            } else {
                                analysis.setLoseCount(analysis.getLoseCount() + 1);
                            }
                            analysis.setChampId(game.getChampionId());
                            analysis.setKillCount(stats.getChampionsKilled());
                            analysis.setDeathCount(stats.getNumDeaths());
                            analysis.setAssistCount(stats.getAssists());
                            analysis.setTotalPlayCount(analysis.getWinCount() + analysis.getLoseCount());
                            champGameAnalysises.add(analysis);
                        }
                    }
                }
            }

            boolean sortByNumOfPicks = true;
            try {
                Collections.sort(champGameAnalysises, new Comparator<ChampGameAnalysis>() {
                    @Override
                    public int compare(ChampGameAnalysis c1, ChampGameAnalysis c2) {
                        return c1.getTotalPlayCount() - (c2.getTotalPlayCount());
                    }
                });
            } catch (Exception e) {
                sortByNumOfPicks = false;
            }

            try {
                Collections.reverse(champGameAnalysises);
            } catch (Exception e) {
                sortByNumOfPicks = false;
            }

            if (sortByNumOfPicks) {
                String champ1ImageUrl = "", champ2ImageUrl = "", champ3ImageUrl = "";
                Integer champ1Id = null, champ2Id = null, champ3Id = null;
                if (champGameAnalysises != null) {
                    if (champGameAnalysises.size() >= 3) {
                        champ1Id = champGameAnalysises.get(0).getChampId();
                        champ2Id = champGameAnalysises.get(1).getChampId();
                        champ3Id = champGameAnalysises.get(2).getChampId();
                    } else if (champGameAnalysises.size() == 2) {
                        champ1Id = champGameAnalysises.get(0).getChampId();
                        champ2Id = champGameAnalysises.get(1).getChampId();
                    } else if (champGameAnalysises.size() == 1) {
                        champ1Id = champGameAnalysises.get(0).getChampId();
                    }
                }

                String champ1Name = "???", champ2Name = "???", champ3Name = "???";
                if (Commons.allChampions != null && Commons.allChampions.size() > 0) {
                    for (Champion champ : Commons.allChampions) {
                        if (champ1Id != null && champ.getId() == champ1Id) {
                            champ1ImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            champ1Name = champ.getChampionName();
                        } else if ((champ2Id != null) && (champ.getId() == champ2Id)) {
                            champ2ImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            champ2Name = champ.getChampionName();
                        } else if ((champ3Id != null) && (champ.getId() == champ3Id)) {
                            champ3ImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            champ3Name = champ.getChampionName();
                        }
                    }
                }

                try {
                    LolImageLoader.getInstance().loadImage(champ1ImageUrl, mostPlayedIV1);
                    mostPlayedTV1.setText(champ1Name);
                    kdaTV1.setText((int) Math.round((((double) (champGameAnalysises.get(0).getKillCount())) / (double) (champGameAnalysises.get(0).getTotalPlayCount())))
                            + "/" + (int) Math.round((((double) (champGameAnalysises.get(0).getDeathCount())) / (double) (champGameAnalysises.get(0).getTotalPlayCount()))) +
                            "/" + (int) Math.round((((double) (champGameAnalysises.get(0).getAssistCount())) / (double) (champGameAnalysises.get(0).getTotalPlayCount()))));
                    winRateTV1.setText("W: " + champGameAnalysises.get(0).getWinCount() + " (" + (int) Math.round(((double) champGameAnalysises.get(0).getWinCount() / (double) champGameAnalysises.get(0).getTotalPlayCount()) * 100) + "%)");
                } catch (Exception ignored) {
                }

                try {
                    LolImageLoader.getInstance().loadImage(champ2ImageUrl, mostPlayedIV2);
                    mostPlayedTV2.setText(champ2Name);
                    kdaTV2.setText((int) Math.round((((double) (champGameAnalysises.get(1).getKillCount())) / (double) (champGameAnalysises.get(1).getTotalPlayCount())))
                            + "/" + (int) Math.round((((double) (champGameAnalysises.get(1).getDeathCount())) / (double) (champGameAnalysises.get(1).getTotalPlayCount()))) +
                            "/" + (int) Math.round((((double) (champGameAnalysises.get(1).getAssistCount())) / (double) (champGameAnalysises.get(1).getTotalPlayCount()))));
                    winRateTV2.setText("W: " + champGameAnalysises.get(1).getWinCount() + " (" + (int) Math.round(((double) champGameAnalysises.get(1).getWinCount() / (double) champGameAnalysises.get(1).getTotalPlayCount()) * 100) + "%)");
                } catch (Exception ignored) {
                }

                try {
                    LolImageLoader.getInstance().loadImage(champ3ImageUrl, mostPlayedIV3);
                    mostPlayedTV3.setText(champ3Name);
                    kdaTV3.setText((int) Math.round((((double) (champGameAnalysises.get(2).getKillCount())) / (double) (champGameAnalysises.get(2).getTotalPlayCount())))
                            + "/" + (int) Math.round((((double) (champGameAnalysises.get(2).getDeathCount())) / (double) (champGameAnalysises.get(2).getTotalPlayCount()))) +
                            "/" + (int) Math.round((((double) (champGameAnalysises.get(2).getAssistCount())) / (double) (champGameAnalysises.get(2).getTotalPlayCount()))));
                    winRateTV3.setText("W: " + champGameAnalysises.get(2).getWinCount() + " (" + (int) Math.round(((double) champGameAnalysises.get(2).getWinCount() / (double) champGameAnalysises.get(2).getTotalPlayCount()) * 100) + "%)");
                } catch (Exception ignored) {
                }


            } else {

            }
        }
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("SummonerOverviewFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
