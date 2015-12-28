package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.service.ServiceRequest;

/**
 * Created by yrazlik on 12/28/15.
 */
public class NewsDetailFragment extends BaseFragment{

    public static final String EXTRA_TITLE = "com.yrazlik.loltr.fragments.newsdetailfragment.extratitle";
    public static final String EXTRA_MESSAGE = "com.yrazlik.loltr.fragments.newsdetailfragment.extramessage";
    public static final String EXTRA_IMAGE_URL = "com.yrazlik.loltr.fragments.newsdetailfragment.extraimageurl";
    public static final String EXTRA_WV_URL  ="com.yrazlik.loltr.fragments.newsdetailfragment.extrawvurl";

    private TextView titleTV, messageTV;
    private NetworkImageView largeImage;
    private WebView wv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_detail, container, false);

        titleTV = (TextView) v.findViewById(R.id.title);
        messageTV = (TextView) v.findViewById(R.id.message);
        largeImage = (NetworkImageView) v.findViewById(R.id.largeImage);
        wv = (WebView) v.findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(new MyWVClient());

        Bundle args = getArguments();

        if(args != null){
            String title = args.getString(EXTRA_TITLE);
            String message = args.getString(EXTRA_MESSAGE);
            String imageUrl = args.getString(EXTRA_IMAGE_URL);
            String wvUrl = args.getString(EXTRA_WV_URL);

            if(title != null){
                titleTV.setText(title);
            }else{
                titleTV.setText("");
            }

            if(message != null){
                messageTV.setText(message);
            }else{
                messageTV.setText("");
            }

            if(imageUrl != null){
                largeImage.setImageUrl(imageUrl, ServiceRequest.getInstance(getActivity()).getImageLoader());
            }

            if(wvUrl != null && wvUrl.length() > 0){
                wv.setVisibility(View.VISIBLE);
                wv.loadUrl(wvUrl);
            }else{
                wv.setVisibility(View.GONE);
            }
        }

        return v;
    }

    private class MyWVClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            final String url_string = url;
            if(url_string != null && !url_string.contains("youtube") && !url_string.contains("embed")){//do not show popup on youtube videos

            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("NewsDetailFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
