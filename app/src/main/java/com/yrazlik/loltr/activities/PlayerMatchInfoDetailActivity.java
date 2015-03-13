package com.yrazlik.loltr.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 3/12/15.
 */
public class PlayerMatchInfoDetailActivity extends ActionBarActivity{

    private AQuery aq;
    private String champImageUrl;
    private String userName;
    private long userId;
    private ImageView champImageIV;
    private TextView userNameTV;
    private ProgressBar progress;
    private ImageView backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_match_info_detail);
        getExtras();
        champImageIV = (ImageView)findViewById(R.id.champImage);
        userNameTV = (TextView)findViewById(R.id.userName);
        progress = (ProgressBar)findViewById(R.id.progress);
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aq = new AQuery(champImageIV);
        aq.progress(progress).image(champImageUrl, true, true);
        userNameTV.setText(userName);
    }


    private void getExtras(){
        userName = getIntent().getStringExtra("EXTRA_USERNAME");
        champImageUrl = getIntent().getStringExtra("EXTRA_CHAMP_IMAGE_URL");
        userId = getIntent().getLongExtra("EXTRA_USERID", 0);
    }
}
