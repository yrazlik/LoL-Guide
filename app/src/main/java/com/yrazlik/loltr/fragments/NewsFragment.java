package com.yrazlik.loltr.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.NewsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.News;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by yrazlik on 12/28/15.
 */
public class NewsFragment extends BaseFragment {

    private ListView newsLV;
    private NewsAdapter adapter;
    private ArrayList<News> news;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        newsLV = (ListView) v.findViewById(R.id.newsLV);
        news = new ArrayList<>();
        final Dialog progress = ServiceRequest.showLoading(getActivity());
        if(progress != null){
            progress.show();
        }

     /*   try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("News");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    try {
                        if (e == null) {
                            for (ParseObject post : list) {
                                String title = post.getString("title");
                                String titleEnglish = post.getString("titleEnglish");
                                String message = post.getString("message");
                                String messageEnglish = post.getString("messageEnglish");
                                String smallImage = post.getString("smallImage");
                                String largeImage = post.getString("largeImage");
                                String videoUrl = post.getString("videoUrl");
                                Date createdAt = post.getCreatedAt();

                                if(Commons.SELECTED_LANGUAGE != null) {
                                    if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("tr")) {
                                        if(title != null && title.length() > 0) {
                                            news.add(new News(title, titleEnglish, message, messageEnglish, smallImage, largeImage, videoUrl, createdAt));

                                        }
                                    } else if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                                        if(titleEnglish != null && titleEnglish.length() > 0) {
                                            news.add(new News(title, titleEnglish, message, messageEnglish, smallImage, largeImage, videoUrl, createdAt));

                                        }
                                    }
                                }else {
                                    if(titleEnglish != null && titleEnglish.length() > 0) {
                                        news.add(new News(title, titleEnglish, message, messageEnglish, smallImage, largeImage, videoUrl, createdAt));
                                    }
                                }
                            }
                            if(news != null && news.size() > 0) {
                                news = sortByDateCreated(news);
                                Collections.reverse(news);
                                hideProgress();
                                adapter = new NewsAdapter(getActivity(), R.layout.list_row_news, news);
                                newsLV.setAdapter(adapter);
                            }
                        } else {
                            hideProgress();
                        }
                    } catch (Exception ignored) {
                        hideProgress();
                    }
                }
            });

        } catch (Exception e) {
        }
*/
        newsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    News selectedNews = news.get(position);

                    FragmentManager fm = getFragmentManager();
                    Bundle args = new Bundle();
                    if(selectedNews.getTitle() != null && selectedNews.getTitle().length() > 0) {
                        args.putString(NewsDetailFragment.EXTRA_TITLE, selectedNews.getTitle());
                    }else if(selectedNews.getTitleEnglish() != null && selectedNews.getTitleEnglish().length() > 0){
                        args.putString(NewsDetailFragment.EXTRA_TITLE, selectedNews.getTitleEnglish());
                    }

                    if(selectedNews.getMessage() != null && selectedNews.getMessage().length() > 0) {
                        args.putString(NewsDetailFragment.EXTRA_MESSAGE, selectedNews.getMessage());
                    }else if(selectedNews.getMessageEnglish() != null && selectedNews.getMessageEnglish().length() > 0) {
                        args.putString(NewsDetailFragment.EXTRA_MESSAGE, selectedNews.getMessageEnglish());
                    }
                    args.putString(NewsDetailFragment.EXTRA_IMAGE_URL, selectedNews.getLargeImage());
                    args.putString(NewsDetailFragment.EXTRA_WV_URL, selectedNews.getVideoUrl());
                    NewsDetailFragment fragment = new NewsDetailFragment();
                    fragment.setArguments(args);
                    fm.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(Commons.NEWS_DETAIL_FRAGMENT).commit();

                }catch (Exception ignored){}
            }
        });

        return v;
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

    private void hideProgress(){
        ((ActionBarActivity)getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ServiceRequest.hideLoading();
            }
        });
    }



    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("NewsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
