package com.yrazlik.loltr.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.activities.FullScreenVideoActivity;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.RobotoTextView;

/**
 * Created by yrazlik on 12/28/15.
 */
public class NewsDetailFragment extends BaseFragment{

    public static final String EXTRA_TITLE = "com.yrazlik.loltr.fragments.newsdetailfragment.extratitle";
    public static final String EXTRA_MESSAGE = "com.yrazlik.loltr.fragments.newsdetailfragment.extramessage";
    public static final String EXTRA_IMAGE_URL = "com.yrazlik.loltr.fragments.newsdetailfragment.extraimageurl";
    public static final String EXTRA_WV_URL  ="com.yrazlik.loltr.fragments.newsdetailfragment.extrawvurl";

    private RobotoTextView titleTV, messageTV;
    private ImageView largeImage;
    private WebView wv;
    private WebChromeClient webChromeClient;
    private RelativeLayout wvLayout;
    private TextView watchFullScreenTV, clickToUpdateTV;
    private String html;
    private String wvUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_detail, container, false);

        watchFullScreenTV = (TextView) v.findViewById(R.id.watchFullScreenTV);
        clickToUpdateTV = (TextView) v.findViewById(R.id.clickToUpdate);
        titleTV = (RobotoTextView) v.findViewById(R.id.title);
        messageTV = (RobotoTextView) v.findViewById(R.id.message);
        largeImage = (ImageView) v.findViewById(R.id.largeImage);
        webChromeClient = new WebChromeClient();
        wvLayout = (RelativeLayout)v.findViewById(R.id.wvLayout);
        wv = (WebView) v.findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv.setWebChromeClient(webChromeClient);
        wv.setWebViewClient(new MyWVClient());

        Bundle args = getArguments();

        if(args != null){
            String title = args.getString(EXTRA_TITLE);
            String message = args.getString(EXTRA_MESSAGE);
            String imageUrl = args.getString(EXTRA_IMAGE_URL);
            wvUrl = args.getString(EXTRA_WV_URL);

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
                LolImageLoader.getInstance().loadImage(imageUrl, largeImage);
            }

            watchFullScreenTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), FullScreenVideoActivity.class);
                    i.putExtra(FullScreenVideoActivity.HTML, wvUrl);
                    startActivity(i);
                }
            });

            clickToUpdateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getActivity().getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });


            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

            //wvUrl = "https://www.youtube.com/embed/BZ9rr-6FYxY";
            if(wvUrl != null && wvUrl.length() > 0){
                wvLayout.setVisibility(View.VISIBLE);
                html = "<html><body><iframe width=\"" + ((int)(dpWidth - 28)) + "\" height=\"" + ((dpWidth*40)/55) + "\" src=\"" + wvUrl + "\"frameborder=\"0\" allowfullscreen></iframe></body></html>";
                wv.loadDataWithBaseURL("", html, "text/html", "UTF-8", null);
            }else{
                wvLayout.setVisibility(View.GONE);
            }
        }

        return v;
    }


    private class MyWVClient extends WebViewClient {

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
