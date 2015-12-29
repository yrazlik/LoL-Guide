package com.yrazlik.loltr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 12/29/15.
 */
public class FullScreenVideoActivity extends Activity{

    public static final String HTML = "com.yrazlik.loltr.activities.fullscreenvideoactivity.html";

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        Intent i = getIntent();
        if(i != null){
            String url = i.getStringExtra(HTML);
            if(url != null && url.length() > 0){

                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

                String html = "<html><body><iframe width=\"" + ((int)(dpWidth - 20)) + "\" height=\"" + ((int)(dpHeight - 20)) + "\" src=\"" + url + "\"frameborder=\"0\" allowfullscreen></iframe></body></html>";


                wv = (WebView) findViewById(R.id.wv);
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                wv.setWebChromeClient(new WebChromeClient());
                wv.setWebViewClient(new MyWVClient());
                wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", null);
            }
        }
    }






    private class MyWVClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.setVisibility(View.GONE);
        }
    }
}
