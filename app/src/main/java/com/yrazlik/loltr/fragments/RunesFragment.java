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
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RunesFragment extends BaseFragment implements ResponseListener{

	ListView list;
	RuneAdapter adapter;
	ArrayList<Rune> runes;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_runes, container, false);
		initUI(v);
		ArrayList<String> pathParams = new ArrayList<String>();
		pathParams.add("static-data");
		pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
		pathParams.add("v1.2");
		pathParams.add("rune");
		HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
		queryParams.put("runeListData", "image,sanitizedDescription");
		queryParams.put("api_key", Commons.API_KEY);
		ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_RUNES_REQUEST, pathParams, queryParams, null, this);
		
		return v;

	}

	private void initUI(View v) {
		list = (ListView) v.findViewById(R.id.listViewRunes);
	}

	@Override
	public void onSuccess(Object response) {
		if (response instanceof RuneResponse) {
			RuneResponse resp = (RuneResponse)response;
			if(runes != null){
				runes.clear();
			}else{
				runes = new ArrayList<Rune>();
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
            try{
                if(getContext() != null){
                    adapter = new RuneAdapter(getContext(), R.layout.rune_row, runes);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }catch (Exception ignored){}

		}
		
		

	}

	@Override
	public void onFailure(Object response) {
		String errorMessage = (String) response;
		Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
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