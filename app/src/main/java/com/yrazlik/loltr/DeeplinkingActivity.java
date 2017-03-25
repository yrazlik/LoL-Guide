package com.yrazlik.loltr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.yrazlik.loltr.activities.SplashActivity;

/**
 * Created by yrazlik on 25/03/17.
 */

public class DeeplinkingActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri data = null;

        if(intent != null){
            data = intent.getData();
        }

        if (LolApplication.appIsRunning) {
            Intent i = new Intent(DeeplinkingActivity.this, MainActivity.class);

            if(data != null){
                i.setData(data);
            }

            startActivity(i);

        } else {
            LolApplication.appIsRunning = true;
            Intent i = new Intent(DeeplinkingActivity.this, SplashActivity.class);

            if(data != null){
                i.setData(data);
            }

            startActivity(i);
        }

        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
