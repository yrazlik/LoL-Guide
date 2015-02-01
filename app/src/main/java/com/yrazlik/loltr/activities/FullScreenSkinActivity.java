package com.yrazlik.loltr.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;

/**
 * Created by yrazlik on 2/1/15.
 */
public class FullScreenSkinActivity extends Activity {

    String key;
    int position;
    AQuery aQuery;
    WebView webView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_skin);
        getExtras();
        webView = (WebView)findViewById(R.id.webViewFullScreenSkin);
        progressBar = (ProgressBar)findViewById(R.id.progressImage);
        aQuery = new AQuery(webView);
        aQuery.progress(progressBar).webImage(Commons.URL_CHAMPION_SKIN_BASE + key + "_" + position + ".jpg", true, true, Color.TRANSPARENT);
    }

    private void getExtras() {
        key = getIntent().getStringExtra("EXTRA_SKIN_KEY");
        position = getIntent().getIntExtra("EXTRA_SKIN_POSITION", 0);
    }
}
