package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.data.Stats;
import com.yrazlik.loltr.data.SummonerSpell;
import com.yrazlik.loltr.service.ServiceRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by yrazlik on 1/6/16.
 */
public class MatchDetailFragment extends BaseFragment{

    private RelativeLayout winLoseLabel;
    private NetworkImageView champIV;
    private TextView levelTV;
    private RelativeLayout textContainer;
    private RelativeLayout matchInfoContainer;
    private TextView matchTypeTV;
    private TextView matchModeTV;
    private TextView kdaTV;
    private RelativeLayout goldContainer;
    private ImageView goldIV;
    private TextView goldTV;
    private RelativeLayout timeContainer;
    private TextView matchTimeTV;
    private TextView matchDateTV;
    private View separator;
    private RelativeLayout spellsContainer;
    private NetworkImageView spell1;
    private NetworkImageView spell2;
    private RelativeLayout itemsContainer;
    private HorizontalScrollView itemsSV;
    private RelativeLayout itemsRL;
    private NetworkImageView item0;
    private NetworkImageView item1;
    private NetworkImageView item2;
    private NetworkImageView item3;
    private NetworkImageView item4;
    private NetworkImageView item5;
    private NetworkImageView item6;

    private LinearLayout teamsContainer;
    private LinearLayout team1LL, team2LL;
    private RelativeLayout statisticsContainer;
    private TextView statisticsTV;
    private LinearLayout statisticsLL;

    private Game game;

    public void setGame(Game game){
        this.game = game;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match_detail, container, false);
        initUI(v);


        reportGoogleAnalytics();
        return v;
    }

    private void initUI(View convertView){
        winLoseLabel = (RelativeLayout) convertView.findViewById(R.id.winLoseLabel);
        champIV = (NetworkImageView) convertView.findViewById(R.id.champIV);
        levelTV = (TextView) convertView.findViewById(R.id.levelTV);
        textContainer = (RelativeLayout) convertView.findViewById(R.id.textContainer);
        matchInfoContainer = (RelativeLayout) convertView.findViewById(R.id.matchInfoContainer);
        matchTypeTV = (TextView) convertView.findViewById(R.id.matchTypeTV);
        matchModeTV = (TextView) convertView.findViewById(R.id.matchModeTV);
        kdaTV = (TextView) convertView.findViewById(R.id.kdaTV);
        goldContainer = (RelativeLayout) convertView.findViewById(R.id.goldContainer);
        goldIV = (ImageView) convertView.findViewById(R.id.goldIV);
        goldTV = (TextView) convertView.findViewById(R.id.goldTV);
        timeContainer = (RelativeLayout) convertView.findViewById(R.id.timeContainer);
        matchTimeTV = (TextView) convertView.findViewById(R.id.matchTimeTV);
        matchDateTV = (TextView) convertView.findViewById(R.id.matchDateTV);
        separator = (View) convertView.findViewById(R.id.separator);
        spellsContainer = (RelativeLayout) convertView.findViewById(R.id.spellsContainer);
        spell1 = (NetworkImageView) convertView.findViewById(R.id.spell1);
        spell2 = (NetworkImageView) convertView.findViewById(R.id.spell2);
        itemsContainer = (RelativeLayout) convertView.findViewById(R.id.itemsContainer);
        itemsSV = (HorizontalScrollView) convertView.findViewById(R.id.itemsSV);
        itemsRL = (RelativeLayout) convertView.findViewById(R.id.itemsRL);
        item0 = (NetworkImageView) convertView.findViewById(R.id.item0);
        item1 = (NetworkImageView) convertView.findViewById(R.id.item1);
        item2 = (NetworkImageView) convertView.findViewById(R.id.item2);
        item3 = (NetworkImageView) convertView.findViewById(R.id.item3);
        item4 = (NetworkImageView) convertView.findViewById(R.id.item4);
        item5 = (NetworkImageView) convertView.findViewById(R.id.item5);
        item6 = (NetworkImageView) convertView.findViewById(R.id.item6);

        teamsContainer = (LinearLayout) convertView.findViewById(R.id.teamsContainer);
        team1LL = (LinearLayout) convertView.findViewById(R.id.team1LL);
        team2LL = (LinearLayout) convertView.findViewById(R.id.team2LL);
        statisticsContainer = (RelativeLayout) convertView.findViewById(R.id.statisticsContainer);
        statisticsTV = (TextView) convertView.findViewById(R.id.statisticsTV);

        if(game != null){
            if(game != null){

                String gameMode = game.getGameMode();
                String gameType = game.getGameType();
                String subType = game.getSubType();

                String gameModeText = getGameModeText(gameMode);
                String gameTypeText = getGameTypeText(gameType, subType);

                matchModeTV.setText(gameModeText);
                matchTypeTV.setText(gameTypeText);

                int summonerSpell1 = game.getSpell1();
                int summonerSpell2 = game.getSpell2();

                if(Commons.allSpells != null) {
                    String spell1Name = null, spell2Name = null;
                    for(SummonerSpell sp : Commons.allSpells){
                        if(sp.getId() == summonerSpell1){
                            if(sp.getImage() != null){
                                spell1Name = sp.getImage().getFull();
                            }
                        }

                        if(sp.getId() == summonerSpell2){
                            if(sp.getImage() != null){
                                spell2Name = sp.getImage().getFull();
                            }
                        }
                    }

                    if (spell1Name != null) {
                        spell1.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell1Name, ServiceRequest.getInstance(getActivity()).getImageLoader());
                    }else{
                        spell1.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        spell1.setBackgroundResource(R.drawable.question_mark);
                    }

                    if (spell2Name != null) {
                        spell2.setImageUrl(Commons.SUMMONER_SPELL_IMAGE_BASE_URL + spell2Name, ServiceRequest.getInstance(getActivity()).getImageLoader());
                    }else{
                        spell2.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        spell2.setBackgroundResource(R.drawable.question_mark);
                    }
                }

                long createDate = game.getCreateDate();

                Calendar c = Calendar.getInstance();
                c.setTime(new Date(createDate));
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                matchDateTV.setText(dateFormat.format(c.getTime()));

                int championID = game.getChampionId();
                String champImageUrl = null;
                if(Commons.allChampions != null && Commons.allChampions.size() > 0){
                    for(Champion champ : Commons.allChampions){
                        if(champ.getId() == championID){
                            champImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                            break;
                        }
                    }
                }

                if(champImageUrl != null) {
                    champIV.setImageUrl(champImageUrl, ServiceRequest.getInstance(getActivity()).getImageLoader());
                }else{
                    champIV.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                    champIV.setBackgroundResource(R.drawable.question_mark);
                }

                Stats stats = game.getStats();
                if(stats != null){
                    if(stats.isWin()){
                        winLoseLabel.setBackgroundColor(getActivity().getResources().getColor(R.color.discount_green));
                    }else{
                        winLoseLabel.setBackgroundColor(getActivity().getResources().getColor(R.color.gg_red));
                    }

                    levelTV.setText(stats.getLevel() + "");
                    kdaTV.setText(stats.getChampionsKilled() + " / " + stats.getAssists() + " / " + stats.getNumDeaths());
                    goldTV.setText(format(stats.getGoldEarned()));
                    String timeString = convertSecondsToHoursMinutes(stats.getTimePlayed());
                    if(timeString != null){
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
                        item0.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem0 + ".png", ServiceRequest.getInstance(getActivity()).getImageLoader());
                    } else {
                        item0.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        item0.setBackgroundResource(R.drawable.nothing);
                    }
                    if (summonerItem1 != 0) {
                        item1.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem1 + ".png", ServiceRequest.getInstance(getActivity()).getImageLoader());
                    } else {
                        item1.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        item1.setBackgroundResource(R.drawable.nothing);
                    }
                    if (summonerItem2 != 0) {
                        item2.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem2 + ".png", ServiceRequest.getInstance(getActivity()).getImageLoader());
                    } else {
                        item2.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        item2.setBackgroundResource(R.drawable.nothing);
                    }
                    if (summonerItem3 != 0) {
                        item3.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem3 + ".png", ServiceRequest.getInstance(getActivity()).getImageLoader());
                    } else {
                        item3.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        item3.setBackgroundResource(R.drawable.nothing);
                    }
                    if (summonerItem4 != 0) {
                        item4.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem4 + ".png", ServiceRequest.getInstance(getActivity()).getImageLoader());
                    } else {
                        item4.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        item4.setBackgroundResource(R.drawable.nothing);
                    }
                    if (summonerItem5 != 0) {
                        item5.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem5 + ".png", ServiceRequest.getInstance(getActivity()).getImageLoader());
                    } else {
                        item5.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        item5.setBackgroundResource(R.drawable.nothing);
                    }
                    if (summonerItem6 != 0) {
                        item6.setImageUrl(Commons.ITEM_IMAGES_BASE_URL + summonerItem6 + ".png", ServiceRequest.getInstance(getActivity()).getImageLoader());
                    } else {
                        item6.setImageUrl(null, ServiceRequest.getInstance(getActivity()).getImageLoader());
                        item6.setBackgroundResource(R.drawable.nothing);
                    }
                }
            }
        }
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

    public String convertSecondsToHoursMinutes(int totalSecs){
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString = null;
        try {
            if(hours == 0) {
                timeString = String.format("%02d:%02d", minutes, seconds);
            }else{
                timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }
        }catch (Exception e){
            timeString = null;
        }
        return timeString;
    }

    private String getGameModeText(String gameMode) {
        String gameTypeText = "";
        if (gameMode != null && (gameMode.length() > 0)) {
            if (gameMode.equalsIgnoreCase("classic")) {
                gameTypeText += getActivity().getResources().getString(R.string.summoners_rift);
            } else if (gameMode.equalsIgnoreCase("odin")) {
                gameTypeText += getActivity().getResources().getString(R.string.dominion);
            } else if (gameMode.equalsIgnoreCase("aram")) {
                gameTypeText += getActivity().getResources().getString(R.string.aram);
            } else if (gameMode.equalsIgnoreCase("tutorial")) {
                gameTypeText += getActivity().getResources().getString(R.string.tutorial);
            } else if (gameMode.equalsIgnoreCase("oneforall")) {
                gameTypeText += getActivity().getResources().getString(R.string.oneforall);
            } else if (gameMode.equalsIgnoreCase("ascension")) {
                gameTypeText += getActivity().getResources().getString(R.string.ascension);
            } else if (gameMode.equalsIgnoreCase("firstblood")) {
                gameTypeText += getActivity().getResources().getString(R.string.firstblood);
            } else if (gameMode.equalsIgnoreCase("kingporo")) {
                gameTypeText += getActivity().getResources().getString(R.string.kingporo);
            }
        }
        return gameTypeText;
    }

    private String getGameTypeText(String gameType, String subType){
        String gameTypeText = "";
        if(gameType != null && gameType.length() > 0){
            if(gameType.equalsIgnoreCase("custom_game")){
                gameTypeText += getActivity().getResources().getString(R.string.custom_game);
            }else if(gameType.equalsIgnoreCase("matched_game")){
                if(subType != null) {
                    if (subType.equalsIgnoreCase("none")) {
                        gameTypeText += getActivity().getResources().getString(R.string.none);
                    }else if (subType.contains("normal") || subType.contains("NORMAL")) {
                        gameTypeText += getActivity().getResources().getString(R.string.normal);
                    }else if(subType.contains("odin") || subType.contains("ODIN")){
                        gameTypeText += getActivity().getResources().getString(R.string.odin);
                    }else if(subType.contains("aram") || subType.contains("ARAM")){
                        gameTypeText += getActivity().getResources().getString(R.string.aram_aram);
                    }else if(subType.equalsIgnoreCase("bot")){
                        gameTypeText += getActivity().getResources().getString(R.string.bot);
                    }else if(subType.contains("oneforall") || subType.contains("ONEFORALL")){
                        gameTypeText += getActivity().getResources().getString(R.string.oneforall);
                    }else if(subType.contains("firstblood") || subType.contains("FIRSTBLOOD")){
                        gameTypeText += getActivity().getResources().getString(R.string.firstblood);
                    }else if(subType.equalsIgnoreCase("SR_6x6")){
                        gameTypeText += getActivity().getResources().getString(R.string.sr6);
                    }else if(subType.equalsIgnoreCase("CAP_5x5")){
                        gameTypeText += getActivity().getResources().getString(R.string.cap5);
                    }else if(subType.equalsIgnoreCase("urf")){
                        gameTypeText += getActivity().getResources().getString(R.string.urf);
                    }else if(subType.equalsIgnoreCase("nightmare_bot")){
                        gameTypeText += getActivity().getResources().getString(R.string.nightmare);
                    }else if(subType.equalsIgnoreCase("ascension")){
                        gameTypeText += getActivity().getResources().getString(R.string.ascension);
                    }else if(subType.equalsIgnoreCase("hexakill")){
                        gameTypeText += getActivity().getResources().getString(R.string.sr6);
                    }else if(subType.equalsIgnoreCase("king_poro")){
                        gameTypeText += getActivity().getResources().getString(R.string.kingporo);
                    }else if(subType.equalsIgnoreCase("counter_pick")){
                        gameTypeText += getActivity().getResources().getString(R.string.counter_pick);
                    }else if(subType.equalsIgnoreCase("bilgewater")){
                        gameTypeText += getActivity().getResources().getString(R.string.bilgewater);
                    }else if (subType.contains("ranked") || subType.contains("RANKED")) {
                        gameTypeText += getActivity().getResources().getString(R.string.ranked);
                    }else{
                        gameTypeText += getActivity().getResources().getString(R.string.unknown);
                    }
                }
            }else if(gameType.equalsIgnoreCase("tutorial_game")){
                gameTypeText += getActivity().getResources().getString(R.string.tutorial);
            }
        }
        return gameTypeText;
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("MatchDetailFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
