package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.NewsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.News;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.utils.AdUtils;
import com.yrazlik.loltr.utils.LocalizationUtils;
import com.yrazlik.loltr.utils.NativeAdLoader;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by yrazlik on 12/28/15.
 */
public class NewsFragment extends BaseFragment implements ResponseListener, AdapterView.OnItemClickListener{

    private ListView newsLV;
    private NewsAdapter adapter;
    private ArrayList<News> news;
    private RobotoTextView noRecentNewsTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_news, container, false);
            noRecentNewsTV = (RobotoTextView) rootView.findViewById(R.id.noRecentNewsTV);
            newsLV = (ListView) rootView.findViewById(R.id.newsLV);
            newsLV.setOnItemClickListener(this);
        }

        setNewsAdapter();

        return rootView;
    }

    private void setNewsAdapter() {
        if (news == null) {
            news = new ArrayList<>();
            showProgressWithWhiteBG();

            if (LolApplication.firebaseInitialized) {
                try {
                    Firebase firebase = new Firebase(getResources().getString(R.string.lol_firebase));
                    firebase.child(getLocalizedNews()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                HashMap<String, Object> keyValues = (HashMap<String, Object>) postSnapshot.getValue();
                                if (keyValues != null && keyValues.size() > 0) {
                                    String url = (String) keyValues.get("url");
                                    String title = (String) keyValues.get("newsTitle");
                                    String message = (String) keyValues.get("shortDesc");
                                    String smallImage = (String) keyValues.get("img");
                                    String createdAt = (String) keyValues.get("createdAt");

                                    if (createdAt != null) {
                                        try {
                                            String[] values = createdAt.split("\\.");
                                            int day = Integer.parseInt(values[0]);
                                            int month = Integer.parseInt(values[1]);
                                            int year = Integer.parseInt(values[2]);
                                            Calendar c = Calendar.getInstance();
                                            c.set(year, month - 1, day, 0, 0);

                                            if (title != null && title.length() > 0) {
                                                news.add(new News(url, title, message, smallImage, c.getTime()));
                                            }
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                            }

                            dismissProgress();

                            if (news != null && news.size() > 0) {
                                noRecentNewsTV.setVisibility(View.GONE);
                                news = sortByDateCreated(news);
                                Collections.reverse(news);
                                adapter = new NewsAdapter(getActivity(), R.layout.list_row_news, news);
                                newsLV.setAdapter(adapter);

                                AdUtils.getInstance().loadNativeAd(getContext(), getResources().getString(R.string.news_ad_unit_id), new NativeAdLoader.NativeAdLoadedListener() {
                                    @Override
                                    public void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
                                        if(nativeAppInstallAd != null) {
                                            News ad = new News();
                                            ad.setAd(true);
                                            ad.setNativeAd(nativeAppInstallAd);
                                            try {
                                                news.add(0, ad);
                                            } catch (Exception ignored) {}
                                            notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onContentAdLoaded(NativeContentAd nativeContentAd) {
                                        if(nativeContentAd != null) {
                                            News ad = new News();
                                            ad.setAd(true);
                                            ad.setNativeAd(nativeContentAd);
                                            try {
                                                news.add(0, ad);
                                            } catch (Exception ignored) {}
                                            notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onAdFailedToLoad() {
                                        //do nothing
                                    }
                                });
                            } else {
                                noRecentNewsTV.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            dismissProgress();
                        }
                    });

                } catch (Exception e) {}
            }
        } else {
            news = sortByDateCreated(news);
            Collections.reverse(news);
            dismissProgress();

            if(adapter != null) {
                adapter.notifyDataSetChanged();
            } else {
                adapter = new NewsAdapter(getActivity(), R.layout.list_row_news, news);
                newsLV.setAdapter(adapter);
            }
        }
    }

    private void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private ArrayList<News> sortByDateCreated(ArrayList<News> news){
        try{
            Collections.sort(news, new Comparator<News>() {
                @Override
                public int compare(News n1, News n2) {
                    return n1.getCreatedAt().compareTo(n2.getCreatedAt());
                }
            });
        }catch (Exception e){
            return null;
        }
        return news;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            News selectedNews = news.get(position);
            if(!selectedNews.isAd()) {
                Commons.openInBrowser(getContext(), selectedNews.getUrl());
            }
        } catch (Exception ignored) {
        }
    }

    public String getLocalizedNews() {
        return "news" + "-" +  LocalizationUtils.getInstance().getLocale().toLowerCase();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("NewsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSuccess(Object response) {

    }

    @Override
    public void onFailure(Object response) {

    }
}
