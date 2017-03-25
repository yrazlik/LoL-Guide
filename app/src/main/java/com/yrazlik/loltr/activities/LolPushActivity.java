package com.yrazlik.loltr.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.LolNotification;
import com.yrazlik.loltr.MainActivity;

/**
 * Created by yrazlik on 25/03/17.
 */

public class LolPushActivity extends Activity{

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent pIntent = getIntent();

        if(pIntent != null) {

            Uri data = pIntent.getData();
            Bundle extras = pIntent.getExtras();

            if (LolApplication.appIsRunning) {
                Intent i = new Intent(LolPushActivity.this, MainActivity.class);
                i.setData(data);
                i.putExtras(extras);
                i.putExtra(LolNotification.PARAMETER_IS_PUSH, true);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(this, SplashActivity.class);
                i.setData(data);
                i.putExtras(extras);
                i.putExtra(LolNotification.PARAMETER_IS_PUSH, true);
                startActivity(i);
            }
            finish();
        }
    }
}
