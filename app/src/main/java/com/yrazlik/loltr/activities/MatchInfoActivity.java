package com.yrazlik.loltr.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.MatchInfoAdapter;
import com.yrazlik.loltr.data.Summoner;
import com.yrazlik.loltr.responseclasses.MatchInfoResponse;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yrazlik on 3/10/15.
 */
public class MatchInfoActivity extends ActionBarActivity{

    private MatchInfoResponse response;
    private Gson gson;
    private ListView team1LV, team2LV;
    private MatchInfoAdapter team1Adapter, team2Adapter;
    private ArrayList<Summoner> team1Summoners, team2Summoners;
    private ArrayList<Long> teamIds;
    private TextView matchTime;
    private long counter;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
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

        if(response != null){
            team1LV = (ListView) findViewById(R.id.team1LV);
            team2LV = (ListView) findViewById(R.id.team2LV);
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

                team1Adapter = new MatchInfoAdapter(MatchInfoActivity.this, R.layout.match_info_detail_listrow, team1Summoners);
                team2Adapter = new MatchInfoAdapter(MatchInfoActivity.this, R.layout.match_info_detail_listrow, team2Summoners);

               // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 60*team1Summoners.size());
              //  team1LV.setLayoutParams(params);
                Resources resources = getResources();
                DisplayMetrics metrics = resources.getDisplayMetrics();
                float px1 = (team1Summoners.size()*60+20) * (metrics.densityDpi / 160f);
                float px2 = (team2Summoners.size()*60+20) * (metrics.densityDpi / 160f);
                team1LV.getLayoutParams().height = Math.round(px1);
                team2LV.getLayoutParams().height = Math.round(px2);
                team1LV.setAdapter(team1Adapter);
                team2LV.setAdapter(team2Adapter);
                matchTime.setText(getTime(response.getGameLength()));
                startTimer(response.getGameLength());
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

    private String getTime(long secs){

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

}
