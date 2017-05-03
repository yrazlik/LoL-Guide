package com.yrazlik.loltr.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.utils.AdUtils;
import com.yrazlik.loltr.view.VideoEnabledWebChromeClient;
import com.yrazlik.loltr.view.VideoEnabledWebView;

/**
 * Created by yrazlik on 1/10/15.
 */
public class LiveChannelActivity extends Activity{

    public static final String EXTRA_STREAM_URL = "com.yrazlik.loltr.fragments.livechannelfragment.extrastreamurl";
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;
    private String streamUrl;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        streamUrl = getIntent().getStringExtra(EXTRA_STREAM_URL);
        // Set layout
        if(AdUtils.getInstance().isAdsEnabled()) {
            setContentView(R.layout.fragment_live_video);
        } else {
            setContentView(R.layout.fragment_live_video_noad);
        }
        adView = (AdView)findViewById(R.id.adView);
        if(adView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        // Save the web view
        webView = (VideoEnabledWebView) findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
        View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Your code...
            }
        };
        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }
                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }

            }
        });
        webView.setWebViewClient(new mWebViewClient());
        webView.setWebChromeClient(webChromeClient);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(streamUrl != null) {
            String html = "<iframe width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"no\" src=\""+ streamUrl + "/embed" +"\"></iframe>";
            webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", null);
        }
    }

    private  class mWebViewClient extends WebViewClient{

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.loadUrl("about:blank");
            try {
                Toast.makeText(getApplicationContext(), "Video yuklenirken bir hata olustu. Lutfen tekrar deneyin", Toast.LENGTH_LONG).show();
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onBackPressed()
    {
        // Notify the VideoEnabledWebChromeClient, and handle it ourselves if it doesn't handle it
        if (!webChromeClient.onBackPressed())
        {
            if (webView.canGoBack())
            {
                webView.goBack();
            }
            else
            {
                // Close app (presumably)
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        webView.loadData("", "text/html", "utf-8");
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.setWebChromeClient(null);

        webView.loadData("", "text/html", "utf-8");
    }



}
