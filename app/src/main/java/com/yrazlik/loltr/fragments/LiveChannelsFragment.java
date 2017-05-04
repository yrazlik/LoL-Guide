package com.yrazlik.loltr.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.LiveChannelsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Channel;
import com.yrazlik.loltr.data.Streams;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.LiveChannelsResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.utils.AdUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrazlik on 1/10/15.
 */
public class LiveChannelsFragment extends BaseFragment implements ResponseListener, AdapterView.OnItemClickListener{

    private LiveChannelsAdapter adapter;
    private ListView liveChannelsList;
    private TextView errorText;
    private ArrayList<Streams> channels;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_livechannels, container, false);
            errorText = (TextView) rootView.findViewById(R.id.errorText);
            errorText.setVisibility(View.GONE);
            liveChannelsList = (ListView) rootView.findViewById(R.id.listViewLiveChannels);
            liveChannelsList.setOnItemClickListener(this);
        }

        requestLiveChannels();
        return rootView;
    }

    private void requestLiveChannels() {
        ArrayList<String> pathParams = new ArrayList<String>();
        HashMap<String, String> queryParams = new HashMap<String, String>();
        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.LIVE_CHANNELS_REQUEST, pathParams, queryParams, null, this);
    }


    @Override
    public void onSuccess(Object response) {
        dismissProgress();
        if(response instanceof LiveChannelsResponse){
            errorText.setVisibility(View.GONE);
            LiveChannelsResponse resp = (LiveChannelsResponse) response;
            channels = resp.getStreams();
            for(int i = 0; i < channels.size(); i++) {
                Streams ch = channels.get(i);
                if(ch != null && ch.getChannel() != null) {
                    ch.getChannel().setUrl("https://player.twitch.tv/?channel=" + ch.getChannel().getName());
                }
            }
            addAdsToNewsArray();
            adapter = new LiveChannelsAdapter(getContext(), R.layout.list_row_livechannel, channels);
            liveChannelsList.setAdapter(adapter);
        }
    }

    private void addAdsToNewsArray() {
        NativeAd nativeAd = AdUtils.getInstance().getCachedAd();
        if(nativeAd != null) {
            Streams ad = new Streams();
            ad.setAd(true);
            ad.setNativeAd(nativeAd);
            try {
                channels.add(1, ad);
                channels.add(11, ad);
                channels.add(19, ad);
                channels.add(ad);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onFailure(final Object response) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    errorText.setVisibility(View.VISIBLE);
                    showRetryView();
                }
            });
        }
    }

    @Override
    protected void retry() {
        super.retry();
        requestLiveChannels();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.listViewLiveChannels:

                Streams s = (Streams) liveChannelsList.getItemAtPosition(position);
                if(!s.isAd()) {
                    Intent i = new Intent(getContext(), LiveChannelActivity.class);
                    i.putExtra(LiveChannelActivity.EXTRA_STREAM_URL, s.getChannel().getUrl());
                    showInterstitial();
                    startActivity(i);
                }
                break;
            default:
                break;
        }
    }

    private void showInterstitial(){
        try {
            if (((LolApplication) (getActivity().getApplication())).shouldShowInterstitial()) {
                ((LolApplication) (getActivity().getApplication())).showInterstitial();
            }
        }catch (Exception ignored){}
    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("LiveChannelsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
