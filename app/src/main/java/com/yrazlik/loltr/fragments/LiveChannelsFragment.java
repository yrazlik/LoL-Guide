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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.LiveChannelsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Streams;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.LiveChannelsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yrazlik on 1/10/15.
 */
public class LiveChannelsFragment extends BaseFragment implements ResponseListener, AdapterView.OnItemClickListener{

    private LiveChannelsAdapter adapter;
    private ListView liveChannelsList;
    private TextView errorText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_livechannels, container, false);

        errorText = (TextView)v.findViewById(R.id.errorText);
        errorText.setVisibility(View.GONE);
        liveChannelsList = (ListView) v.findViewById(R.id.listViewLiveChannels);
        liveChannelsList.setOnItemClickListener(this);
        ArrayList<String> pathParams = new ArrayList<String>();
        HashMap<String, String> queryParams = new HashMap<String, String>();
        ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.LIVE_CHANNELS_REQUEST, pathParams, queryParams, null, this);

        return v;
    }


    @Override
    public void onSuccess(Object response) {
        if(response instanceof LiveChannelsResponse){
            errorText.setVisibility(View.GONE);
            LiveChannelsResponse resp = (LiveChannelsResponse) response;
            ArrayList<Streams> channels = resp.getStreams();
            adapter = new LiveChannelsAdapter(getContext(), R.layout.list_row_livechannel, channels);
            liveChannelsList.setAdapter(adapter);

        }
    }

    @Override
    public void onFailure(Object response) {
        errorText.setVisibility(View.VISIBLE);
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
                Intent i = new Intent(getContext(), LiveChannelActivity.class);
                i.putExtra(LiveChannelActivity.EXTRA_STREAM_URL, s.getChannel().getUrl());
                startActivity(i);

                break;
            default:
                break;
        }
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
