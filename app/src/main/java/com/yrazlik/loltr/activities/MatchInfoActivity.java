package com.yrazlik.loltr.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.MatchInfoAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Summoner;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.responseclasses.MatchInfoResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yrazlik on 3/10/15.
 */
public class MatchInfoActivity extends ActionBarActivity implements ResponseListener, AdapterView.OnItemClickListener{

    private MatchInfoResponse response;
    private Gson gson;
    private ListView team1LV, team2LV;
    private MatchInfoAdapter team1Adapter, team2Adapter;
    private ArrayList<Summoner> team1Summoners, team2Summoners;
    private ArrayList<Long> teamIds;
    private TextView matchTime;
    private long counter;
    private ImageView backButton;
    private ProgressBar loadingProgress;
    private ScrollView scrollContent;
    private String selectedRegion;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            setContentView(R.layout.activity_match_info);
        } else {
            setContentView(R.layout.activity_match_info_noad);
        }
        adView = (AdView)findViewById(R.id.adView);
        if(adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        matchTime = (TextView)findViewById(R.id.matchTime);
        gson = new Gson();
        response = (MatchInfoResponse) getIntent().getSerializableExtra("MATCH_INFO_RESPONSE");
        selectedRegion = getIntent().getStringExtra("SELECTED_REGION");
        loadingProgress = (ProgressBar)findViewById(R.id.loadingProgress);
        scrollContent = (ScrollView)findViewById(R.id.scrollContent);

        if(response != null){
            team1LV = (ListView) findViewById(R.id.team1LV);
            team2LV = (ListView) findViewById(R.id.team2LV);
            team1LV.setOnItemClickListener(this);
            team2LV.setOnItemClickListener(this);
            ArrayList<Summoner>allSummoners = response.getParticipants();
            teamIds = new ArrayList<Long>();
            for(Summoner s : allSummoners){
                if(!teamIds.contains(s.getTeamId())){
                    teamIds.add(s.getTeamId());
                }
            }

            if(teamIds.size() != 2){
                Toast.makeText(getApplicationContext(), R.string.anErrorOccured, Toast.LENGTH_SHORT).show();
                finish();
            }else {
                team1Summoners = new ArrayList<Summoner>();
                team2Summoners = new ArrayList<Summoner>();
                for (Summoner s : allSummoners) {
                    if(s.getTeamId() == teamIds.get(0)){
                        team1Summoners.add(s);
                    }else if(s.getTeamId() == teamIds.get(1)){
                        team2Summoners.add(s);
                    }
                }

                ArrayList<String> pathParams = new ArrayList<String>();
                pathParams.add("static-data");
                pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
                pathParams.add("v1.2");
                pathParams.add("champion");
                HashMap<String, String> queryParams = new HashMap<String, String>();
                queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
                queryParams.put("version", Commons.LATEST_VERSION);
                queryParams.put("champData", "altimages");
                queryParams.put("api_key", Commons.API_KEY);

                if(Commons.allChampions == null || Commons.allChampions.size() <= 0) {
                    ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_CHAMPIONS_REQUEST, pathParams, queryParams, null, this);
                }else{
                    setAdapters();
                }


            }

        }else{
            Toast.makeText(getApplicationContext(), R.string.anErrorOccured, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void startTimer(long secs) {
        Timer t = new Timer();
        counter = secs;
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counter++;
                        matchTime.setText(getTime(counter));
                    }
                });

            }
        }, 0, 1000);
    }

    private void setAdapters(){
      //  ArrayList<Summoner>team1SummonersWithNames = new ArrayList<Summoner>();
      //  ArrayList<Summoner>team2SummonersWithNames = new ArrayList<Summoner>();

        for(Summoner s : team1Summoners){
            for(Champion c : Commons.allChampions){
                if(c.getId() == s.getChampionId()){
                    s.setChampName(c.getChampionName());
                    s.setKey(c.getKey());
                    break;
                }
            }
        }

        for(Summoner s : team2Summoners){
            for(Champion c : Commons.allChampions){
                if(c.getId() == s.getChampionId()){
                    s.setChampName(c.getChampionName());
                    s.setKey(c.getKey());
                    break;
                }
            }
        }


        team1Adapter = new MatchInfoAdapter(MatchInfoActivity.this, R.layout.match_info_detail_listrow, team1Summoners);
        team2Adapter = new MatchInfoAdapter(MatchInfoActivity.this, R.layout.match_info_detail_listrow, team2Summoners);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px1 = (team1Summoners.size()*60+20) * (metrics.densityDpi / 160f);
        float px2 = (team2Summoners.size()*60+20) * (metrics.densityDpi / 160f);
        team1LV.getLayoutParams().height = Math.round(px1);
        team2LV.getLayoutParams().height = Math.round(px2);
        team1LV.setAdapter(team1Adapter);
        team2LV.setAdapter(team2Adapter);
        matchTime.setText(getTime(response.getGameLength()));
        try{
            loadingProgress.setVisibility(View.GONE);
            scrollContent.setVisibility(View.VISIBLE);
        }catch (Exception e){}

        startTimer(response.getGameLength());


    }

    private String getTime(long secs){

        if(secs < 0){
            secs = (-1)*secs;
        }

        String hours =  String.valueOf(secs / 3600);
        String minutes = String.valueOf((secs/60));
        String seconds = String.valueOf(secs % 60);


        if(minutes.length() == 1){
            minutes = "0" + minutes;
        }

        if(seconds.length() == 1){
            seconds = "0" + seconds;
        }

        if(hours.equals("0")){
            return minutes + ":" + seconds;
        }

        return hours + ":" + minutes + ":" + seconds;
    }

    @Override
    public void onSuccess(Object response) {

        if(response instanceof AllChampionsResponse){
            AllChampionsResponse resp = (AllChampionsResponse) response;
            Map<String, Map<String, String>> data = resp.getData();
            Commons.setAllChampions(resp);
            setAdapters();

        }

    }

    @Override
    public void onFailure(Object response) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == team1LV.getId() || parent.getId() == team2LV.getId()){

            Summoner s = (Summoner)parent.getAdapter().getItem(position);
            Intent i = new Intent(MatchInfoActivity.this, PlayerMatchInfoDetailActivity.class);
            i.putExtra("EXTRA_USERNAME", s.getSummonerName());
            i.putExtra("EXTRA_USERID", s.getSummonerId());
            i.putExtra("EXTRA_CHAMP_IMAGE_URL", "http://ddragon.leagueoflegends.com/cdn/" + Commons.LATEST_VERSION + "/img/champion/" + s.getKey() + ".png");
            i.putExtra("SELECTED_REGION", selectedRegion);
            startActivity(i);
        }
    }
}
