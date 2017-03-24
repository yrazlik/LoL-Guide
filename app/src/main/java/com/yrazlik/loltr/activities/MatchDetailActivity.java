package com.yrazlik.loltr.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.StatisticsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.data.Player;
import com.yrazlik.loltr.data.Statistics;
import com.yrazlik.loltr.data.Stats;
import com.yrazlik.loltr.data.SummonerNames;
import com.yrazlik.loltr.data.SummonerSpell;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.SummonerNamesResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.FadeInNetworkImageView;
import com.yrazlik.loltr.view.RobotoTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by yrazlik on 1/6/16.
 */
public class MatchDetailActivity extends Activity implements ResponseListener{

    public static final String EXTRA_GAME = "com.yrazlik.loltr.activities.EXTRA_GAME";
    public static final String EXTRA_SUMMONER_ID = "com.yrazlik.loltr.activities.EXTRA_SUMMONER_ID";
    public static final String EXTRA_REGION = "com.yrazlik.loltr.activities.EXTRA_REGION";

    private DisplayImageOptions mOptions;

    private CardView loadingView;
    private ScrollView parent;
    private RelativeLayout winLoseLabel;
    private ImageView champIV;
    private RobotoTextView levelTV;
    private RelativeLayout textContainer;
    private RelativeLayout matchInfoContainer;
    private RobotoTextView matchTypeTV;
    private RobotoTextView matchModeTV;
    private RobotoTextView kdaTV;
    private RelativeLayout goldContainer;
    private ImageView goldIV;
    private RobotoTextView goldTV;
    private RelativeLayout timeContainer;
    private RobotoTextView matchTimeTV;
    private RobotoTextView matchDateTV;
    private View separator;
    private RelativeLayout spellsContainer;
    private ImageView spell1;
    private ImageView spell2;
    private RelativeLayout itemsContainer;
    private HorizontalScrollView itemsSV;
    private RelativeLayout itemsRL;
    private ImageView item0;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ListView statisticsLV;
    private StatisticsAdapter statisticsAdapter;

    private int summonerNamesRequestCount = 0;
    private LinearLayout teamsContainer;
    private LinearLayout team1LL, team2LL;
    private RelativeLayout statisticsContainer;
    private RobotoTextView statisticsTV, team1TV, team2TV;
    private List<SummonerNames> summonerNames;
    private String region;

    private Game game;
    private long summonerId;
    private String summonerIds = "";

    public void setGame(Game game) {
        this.game = game;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_match_detail);

        mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).showImageOnLoading(R.drawable.question_mark).showImageForEmptyUri(R.drawable.block)
                .showImageOnFail(R.drawable.question_mark).cacheOnDisk(true).cacheInMemory(true)
                .build();

        if(getIntent() != null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                game = (Game) extras.getSerializable(EXTRA_GAME);
                summonerId = extras.getLong(EXTRA_SUMMONER_ID);
                region = extras.getString(EXTRA_REGION);
            }

        }
        if(game != null){
            List<Player> players = game.getFellowPlayers();
            if(players != null && players.size() > 0) {
                players.add(new Player(game.getChampionId(), summonerId, game.getTeamId()));
                for (Player p : players) {
                    summonerIds += p.getSummonerId() + ",";
                }
                if (summonerIds.length() > 0) {
                    summonerIds = summonerIds.substring(0, summonerIds.length() - 1);
                }
                makeGetPlayerNamesRequest(summonerIds);
            }else{
                initUI();
            }
        }
        reportGoogleAnalytics();
    }

    private void makeGetPlayerNamesRequest(String summonerIds){
       ServiceRequest.getInstance(this).makeGetSummonerIdsRequest(Commons.SUMMONER_NAMES_REQUEST, region, summonerIds, null, this);
    }

    private void initUI() {
        loadingView = (CardView) findViewById(R.id.loadingView);
        parent = (ScrollView) findViewById(R.id.parent);
        winLoseLabel = (RelativeLayout) findViewById(R.id.winLoseLabel);
        champIV = (ImageView) findViewById(R.id.champIV);
        levelTV = (RobotoTextView) findViewById(R.id.levelTV);
        textContainer = (RelativeLayout) findViewById(R.id.textContainer);
        matchInfoContainer = (RelativeLayout) findViewById(R.id.matchInfoContainer);
        matchTypeTV = (RobotoTextView) findViewById(R.id.matchTypeTV);
        matchModeTV = (RobotoTextView) findViewById(R.id.matchModeTV);
        kdaTV = (RobotoTextView) findViewById(R.id.kdaTV);
        goldContainer = (RelativeLayout) findViewById(R.id.goldContainer);
        goldIV = (ImageView) findViewById(R.id.goldIV);
        goldTV = (RobotoTextView) findViewById(R.id.goldTV);
        timeContainer = (RelativeLayout) findViewById(R.id.timeContainer);
        matchTimeTV = (RobotoTextView) findViewById(R.id.matchTimeTV);
        matchDateTV = (RobotoTextView) findViewById(R.id.matchDateTV);
        separator = (View) findViewById(R.id.separator);
        spellsContainer = (RelativeLayout) findViewById(R.id.spellsContainer);
        spell1 = (ImageView) findViewById(R.id.spell1);
        spell2 = (ImageView) findViewById(R.id.spell2);
        itemsContainer = (RelativeLayout) findViewById(R.id.itemsContainer);
        itemsSV = (HorizontalScrollView) findViewById(R.id.itemsSV);
        itemsRL = (RelativeLayout) findViewById(R.id.itemsRL);
        item0 = (ImageView) findViewById(R.id.item0);
        item1 = (ImageView) findViewById(R.id.item1);
        item2 = (ImageView) findViewById(R.id.item2);
        item3 = (ImageView) findViewById(R.id.item3);
        item4 = (ImageView) findViewById(R.id.item4);
        item5 = (ImageView) findViewById(R.id.item5);
        item6 = (ImageView) findViewById(R.id.item6);
        statisticsLV = (ListView) findViewById(R.id.statisticsLV);

        teamsContainer = (LinearLayout) findViewById(R.id.teamsContainer);
        team1LL = (LinearLayout) findViewById(R.id.team1LL);
        team2LL = (LinearLayout) findViewById(R.id.team2LL);
        statisticsContainer = (RelativeLayout) findViewById(R.id.statisticsContainer);
        statisticsTV = (RobotoTextView) findViewById(R.id.statisticsTV);
        team1TV = (RobotoTextView) findViewById(R.id.team1TV);
        team2TV = (RobotoTextView) findViewById(R.id.team2TV);
        statisticsTV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        team1TV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        team2TV.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        if (game != null) {
            if (game != null) {

                String gameMode = game.getGameMode();
                String gameType = game.getGameType();
                String subType = game.getSubType();

                String gameModeText = getGameModeText(gameMode);
                String gameTypeText = getGameTypeText(gameType, subType);

                matchModeTV.setText(gameModeText);
                matchTypeTV.setText(gameTypeText);

                int summonerSpell1 = game.getSpell1();
                int summonerSpell2 = game.getSpell2();

                if (Commons.allSpells != null) {
                    String spell1Name = null, spell2Name = null;
                    for (SummonerSpell sp : Commons.allSpells) {
                        if (sp.getId() == summonerSpell1) {
                            if (sp.getImage() != null) {
                                spell1Name = sp.getImage().getFull();
                            }
                        }

                        if (sp.getId() == summonerSpell2) {
                            if (sp.getImage() != null) {
                                spell2Name = sp.getImage().getFull();
                            }
                        }
                    }

                    LolImageLoader.getInstance().loadImage(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell1Name, spell1);
                    LolImageLoader.getInstance().loadImage(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell2Name, spell2);
                }

                long createDate = game.getCreateDate();

                Calendar c = Calendar.getInstance();
                c.setTime(new Date(createDate));
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                matchDateTV.setText(dateFormat.format(c.getTime()));

                int championID = game.getChampionId();
                String champImageUrl = null;
                if (Commons.allChampions != null && Commons.allChampions.size() > 0) {
                    for (Champion champ : Commons.allChampions) {
                        if (champ.getId() == championID) {
                            champImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            break;
                        }
                    }
                }


                LolImageLoader.getInstance().loadImage(champImageUrl, champIV);


                Stats stats = game.getStats();
                if (stats != null) {
                    if (stats.isWin()) {
                        winLoseLabel.setBackgroundColor(this.getResources().getColor(R.color.discount_green));
                    } else {
                        winLoseLabel.setBackgroundColor(this.getResources().getColor(R.color.gg_red));
                    }

                    levelTV.setText(stats.getLevel() + "");
                    kdaTV.setText(stats.getChampionsKilled() + " / " + stats.getNumDeaths() + " / " + stats.getAssists());
                    goldTV.setText(format(stats.getGoldEarned()));
                    String timeString = convertSecondsToHoursMinutes(stats.getTimePlayed());
                    if (timeString != null) {
                        matchTimeTV.setText(timeString);
                    }

                    int summonerItem0 = stats.getItem0();
                    int summonerItem1 = stats.getItem1();
                    int summonerItem2 = stats.getItem2();
                    int summonerItem3 = stats.getItem3();
                    int summonerItem4 = stats.getItem4();
                    int summonerItem5 = stats.getItem5();
                    int summonerItem6 = stats.getItem6();

                    if (summonerItem0 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + summonerItem0 + ".png", item0, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", item0, mOptions);
                    }
                    if (summonerItem1 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + summonerItem1 + ".png", item1, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", item1, mOptions);
                    }
                    if (summonerItem2 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + summonerItem2 + ".png", item2, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", item2, mOptions);
                    }
                    if (summonerItem3 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + summonerItem3 + ".png", item3, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", item3, mOptions);
                    }
                    if (summonerItem4 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + summonerItem4 + ".png", item4, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", item4, mOptions);
                    }
                    if (summonerItem5 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + summonerItem5 + ".png", item5, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", item5, mOptions);
                    }
                    if (summonerItem6 != 0) {
                        LolImageLoader.getInstance().loadImage(Commons.ITEM_IMAGES_BASE_URL + summonerItem6 + ".png", item6, mOptions);
                    } else {
                        LolImageLoader.getInstance().loadImage("", item6, mOptions);
                    }

                    ArrayList<Statistics> statistics = new ArrayList<>();

                    int totalDamageDealt = stats.getTotalDamageDealt();
                    statistics.add(new Statistics(getResources().getString(R.string.totalDamageDealt), totalDamageDealt + ""));
                    int totalDamageTaken = stats.getTotalDamageTaken();
                    statistics.add(new Statistics(getResources().getString(R.string.totalDamageTaken), totalDamageTaken + ""));
                    int goldEarned = stats.getGoldEarned();
                    statistics.add(new Statistics(getResources().getString(R.string.goldEarned), goldEarned + ""));
                    int totalHeal = stats.getTotalHeal();
                    statistics.add(new Statistics(getResources().getString(R.string.totalHeal), totalHeal + ""));
                    int lagestKillingSpree = stats.getLargestKillingSpree();
                    statistics.add(new Statistics(getResources().getString(R.string.lagestKillingSpree), lagestKillingSpree + ""));
                    int largestMultiKill = stats.getLargestMultiKill();
                    statistics.add(new Statistics(getResources().getString(R.string.largestMultiKill), largestMultiKill + ""));
                    int magicDamageDealtPlayer = stats.getMagicDamageDealtPlayer();
                    statistics.add(new Statistics(getResources().getString(R.string.magicDamageDealtPlayer), magicDamageDealtPlayer + ""));
                    int physicalDamageDealtPlayer = stats.getPhysicalDamageDealtPlayer();
                    statistics.add(new Statistics(getResources().getString(R.string.physicalDamageDealtPlayer), physicalDamageDealtPlayer + ""));
                    int minionsKilled = stats.getMinionsKilled();
                    statistics.add(new Statistics(getResources().getString(R.string.minionsKilled), minionsKilled + ""));
                    int neutralMinionsKilled = stats.getNeutralMinionsKilled();
                    statistics.add(new Statistics(getResources().getString(R.string.neutralMinionsKilled), neutralMinionsKilled + ""));
                    int magicDamageTaken = stats.getMagicDamageTaken();
                    statistics.add(new Statistics(getResources().getString(R.string.magicDamageTaken), magicDamageTaken + ""));
                    int physicalDamageTaken = stats.getPhysicalDamageTaken();
                    statistics.add(new Statistics(getResources().getString(R.string.physicalDamageTaken), physicalDamageTaken + ""));
                    int totalTimeCrowdControlDealt = stats.getTotalTimeCrowdControlDealt();
                    statistics.add(new Statistics(getResources().getString(R.string.totalTimeCrowdControlDealt), totalTimeCrowdControlDealt + ""));
                    int turretsKilled = stats.getTurretsKilled();
                    statistics.add(new Statistics(getResources().getString(R.string.turretsKilled), turretsKilled + ""));
                    int wardPlaced = stats.getWardPlaced();
                    statistics.add(new Statistics(getResources().getString(R.string.wardPlaced), wardPlaced + ""));
                    int wardKilled = stats.getWardKilled();
                    statistics.add(new Statistics(getResources().getString(R.string.wardKilled), wardKilled + ""));
                    int ipEarned = game.getIpEarned();
                    statistics.add(new Statistics(getResources().getString(R.string.ipEarned), ipEarned + ""));

                    statisticsAdapter = new StatisticsAdapter(this, R.layout.list_row_statistics, statistics);
                    statisticsLV.setAdapter(statisticsAdapter);

                }
            }
            List<Player> players = game.getFellowPlayers();
            if(players != null){
                teamsContainer.setVisibility(View.VISIBLE);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                ArrayList<Integer> teamIds = new ArrayList<>();
                for(Player p : players){
                    if(!teamIds.contains(p.getTeamId())){
                        teamIds.add(p.getTeamId());
                    }
                }

                if(teamIds.size() != 2){
                    teamsContainer.setVisibility(View.GONE);
                }else {
                    for (Player p : players) {
                        if(p.getSummonerId() == 0){
                            createPlayerLayoutAndAdd(summonerId, p.getChampionId(), inflater, p.getTeamId(), teamIds);
                        }else {
                            createPlayerLayoutAndAdd(p.getSummonerId(), p.getChampionId(), inflater, p.getTeamId(), teamIds);
                        }
                    }
                }
            }else{
                teamsContainer.setVisibility(View.GONE);
            }
        }

        loadingView.setVisibility(View.GONE);
        parent.setVisibility(View.VISIBLE);
    }

    private void createPlayerLayoutAndAdd(long playerId, int championID, LayoutInflater inflater, int teamID, ArrayList<Integer> teamIds){
        RelativeLayout summonerLayout = (RelativeLayout) inflater.inflate(R.layout.view_team_player, null);
        ImageView champIV = (ImageView) summonerLayout.findViewById(R.id.champIV);
        RobotoTextView summonerNameTV = (RobotoTextView) summonerLayout.findViewById(R.id.summonerNameTV);

        String champImageUrl = null;
        if(Commons.allChampions != null && Commons.allChampions.size() > 0){
            for(Champion champ : Commons.allChampions){
                if(champ.getId() == championID){
                    champImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                    break;
                }
            }
        }

        if(summonerNames != null && summonerNames.size() > 0){
            String summonerName = "???";
            for(SummonerNames sn : summonerNames){
                if(sn != null){
                    if(playerId == sn.getId()){
                        summonerName = sn.getName();
                        break;
                    }
                }
            }
            if(summonerName != null){
                summonerNameTV.setText(summonerName);
            }
        }

        LolImageLoader.getInstance().loadImage(champImageUrl, champIV);

        if (teamID == teamIds.get(0)){
            team1LL.addView(summonerLayout);
        } else if (teamID == teamIds.get(1)){
            team2LL.addView(summonerLayout);
        }

        parent.post(new Runnable() {
            @Override
            public void run() {
                parent.scrollTo(0, 0);
            }
        });

    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = ((truncated / 10d) != (truncated / 10));
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public String convertSecondsToHoursMinutes(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString = null;
        try {
            if (hours == 0) {
                timeString = String.format("%02d:%02d", minutes, seconds);
            } else {
                timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
        } catch (Exception e) {
            timeString = null;
        }
        return timeString;
    }

    private String getGameModeText(String gameMode) {
        String gameTypeText = "";
        if (gameMode != null && (gameMode.length() > 0)) {
            if (gameMode.equalsIgnoreCase("classic")) {
                gameTypeText += this.getResources().getString(R.string.summoners_rift);
            } else if (gameMode.equalsIgnoreCase("odin")) {
                gameTypeText += this.getResources().getString(R.string.dominion);
            } else if (gameMode.equalsIgnoreCase("aram")) {
                gameTypeText += this.getResources().getString(R.string.aram);
            } else if (gameMode.equalsIgnoreCase("tutorial")) {
                gameTypeText += this.getResources().getString(R.string.tutorial);
            } else if (gameMode.equalsIgnoreCase("oneforall")) {
                gameTypeText += this.getResources().getString(R.string.oneforall);
            } else if (gameMode.equalsIgnoreCase("ascension")) {
                gameTypeText += this.getResources().getString(R.string.ascension);
            } else if (gameMode.equalsIgnoreCase("firstblood")) {
                gameTypeText += this.getResources().getString(R.string.firstblood);
            } else if (gameMode.equalsIgnoreCase("kingporo")) {
                gameTypeText += this.getResources().getString(R.string.kingporo);
            }
        }
        return gameTypeText;
    }

    private String getGameTypeText(String gameType, String subType) {
        String gameTypeText = "";
        if (gameType != null && gameType.length() > 0) {
            if (gameType.equalsIgnoreCase("custom_game")) {
                gameTypeText += this.getResources().getString(R.string.custom_game);
            } else if (gameType.equalsIgnoreCase("matched_game")) {
                if (subType != null) {
                    if (subType.equalsIgnoreCase("none")) {
                        gameTypeText += this.getResources().getString(R.string.none);
                    } else if (subType.contains("normal") || subType.contains("NORMAL")) {
                        gameTypeText += this.getResources().getString(R.string.normal);
                    } else if (subType.contains("odin") || subType.contains("ODIN")) {
                        gameTypeText += this.getResources().getString(R.string.odin);
                    } else if (subType.contains("aram") || subType.contains("ARAM")) {
                        gameTypeText += this.getResources().getString(R.string.aram_aram);
                    } else if (subType.equalsIgnoreCase("bot")) {
                        gameTypeText += this.getResources().getString(R.string.bot);
                    } else if (subType.contains("oneforall") || subType.contains("ONEFORALL")) {
                        gameTypeText += this.getResources().getString(R.string.oneforall);
                    } else if (subType.contains("firstblood") || subType.contains("FIRSTBLOOD")) {
                        gameTypeText += this.getResources().getString(R.string.firstblood);
                    } else if (subType.equalsIgnoreCase("SR_6x6")) {
                        gameTypeText += this.getResources().getString(R.string.sr6);
                    } else if (subType.equalsIgnoreCase("CAP_5x5")) {
                        gameTypeText += this.getResources().getString(R.string.cap5);
                    } else if (subType.equalsIgnoreCase("urf")) {
                        gameTypeText += this.getResources().getString(R.string.urf);
                    } else if (subType.equalsIgnoreCase("nightmare_bot")) {
                        gameTypeText += this.getResources().getString(R.string.nightmare);
                    } else if (subType.equalsIgnoreCase("ascension")) {
                        gameTypeText += this.getResources().getString(R.string.ascension);
                    } else if (subType.equalsIgnoreCase("hexakill")) {
                        gameTypeText += this.getResources().getString(R.string.sr6);
                    } else if (subType.equalsIgnoreCase("king_poro")) {
                        gameTypeText += this.getResources().getString(R.string.kingporo);
                    } else if (subType.equalsIgnoreCase("counter_pick")) {
                        gameTypeText += this.getResources().getString(R.string.counter_pick);
                    } else if (subType.equalsIgnoreCase("bilgewater")) {
                        gameTypeText += this.getResources().getString(R.string.bilgewater);
                    } else if (subType.contains("ranked") || subType.contains("RANKED")) {
                        gameTypeText += this.getResources().getString(R.string.ranked);
                    } else {
                        gameTypeText += this.getResources().getString(R.string.unknown);
                    }
                }
            } else if (gameType.equalsIgnoreCase("tutorial_game")) {
                gameTypeText += getResources().getString(R.string.tutorial);
            }
        }
        return gameTypeText;
    }

    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getApplication()).getTracker();
        t.setScreenName("MatchDetailFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof SummonerNamesResponse){
            if(response != null) {
                SummonerNamesResponse summonerNamesResponse = (SummonerNamesResponse) response;
                if(summonerNamesResponse != null) {
                    summonerNames = summonerNamesResponse.getSummonerNames();
                    initUI();
                }
            }
        }
    }

    @Override
    public void onFailure(Object response) {
        if(response instanceof Integer){
            Integer requestID = (Integer) response;
            if(requestID == Commons.SUMMONER_NAMES_REQUEST) {
                if (summonerNamesRequestCount < 3) {
                    summonerNamesRequestCount++;
                    makeGetPlayerNamesRequest(summonerIds);
                }
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.slide_top_out);
    }
}
