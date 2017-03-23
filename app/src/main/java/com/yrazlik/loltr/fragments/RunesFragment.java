package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.internal.LinkedTreeMap;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.RuneAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Rune;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.RuneResponse;
import com.yrazlik.loltr.service.ServiceHelper;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RunesFragment extends BaseFragment implements ResponseListener{

	private ListView runesLV;
	private RuneAdapter runesAdapter;
	private ArrayList<Rune> runes;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_runes, container, false);
            initUI(rootView);
        }

        setRunesAdapter();

		return rootView;

	}

	private void initUI(View v) {
		runesLV = (ListView) v.findViewById(R.id.listViewRunes);
	}

    private void setRunesAdapter() {
        if(runes == null || runes.size() == 0) {
            showProgressWithWhiteBG();
            ServiceHelper.getInstance(getContext()).makeGetAllRunesRequest(this);
        } else {
            if(runesAdapter == null || runesAdapter.getCount() == 0) {
                if(getContext() != null){
                    runesAdapter = new RuneAdapter(getContext(), R.layout.rune_row, runes);
                    runesLV.setAdapter(runesAdapter);
                    runesAdapter.notifyDataSetChanged();
                }
            } else {
                runesAdapter.notifyDataSetChanged();
            }
        }
    }

	@Override
	public void onSuccess(Object response) {
        dismissProgress();
		if (response instanceof RuneResponse) {
			RuneResponse resp = (RuneResponse)response;
			if(runes != null){
				runes.clear();
			}else{
				runes = new ArrayList<>();
			}
			
			Map<String, Map<String, Object>> data = resp.getData();
			for(Entry<String, Map<String, Object>> entry : data.entrySet()){
				String key = entry.getKey();
				String imageUrl = (String) ((LinkedTreeMap<String, Object>)entry.getValue().get("image")).get("full");
				String sanitizedDescription = (String) entry.getValue().get("sanitizedDescription");
				String name = (String) entry.getValue().get("name");
				Rune rune = new Rune();
				rune.setName(name);
				rune.setSanitizedDescription(sanitizedDescription);
				rune.setImageUrl(imageUrl);
				runes.add(rune);
			}

            setRunesAdapter();
		}
	}

	@Override
	public void onFailure(Object response) {
		String errorMessage = (String) response;
		Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        showRetryView();
	}

    @Override
    protected void retry() {
        super.retry();
        setRunesAdapter();
    }

    @Override
	public Context getContext() {
		return getActivity();
	}

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("RunesFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}