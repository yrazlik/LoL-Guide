package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.ListAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.responseclasses.ChampionRpIpCostsResponse;
import com.yrazlik.loltr.responseclasses.WeeklyFreeChampionsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WeeklyFreeChampionsFragment extends Fragment implements
		ResponseListener, OnItemClickListener {

	ListView list;
	ListAdapter adapter;
	int freeToPlayChampsSize;
	ArrayList<String> weeklyFreeChampIds;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_weekly_free_champions,
				container, false);
		initUI(v);

		if (Commons.weeklyFreeChampions != null) {
			adapter = new ListAdapter(getActivity(), R.layout.list_row,
					Commons.weeklyFreeChampions);
			list.setAdapter(adapter);
		} else {
			Commons.weeklyFreeChampions = new ArrayList<Champion>();
			adapter = new ListAdapter(getActivity(), R.layout.list_row,
					Commons.weeklyFreeChampions);
			list.setAdapter(adapter);
			ArrayList<String> pathParams = new ArrayList<String>();
			HashMap<String, String> queryParams = new HashMap<String, String>();
			pathParams.add("tr");
			pathParams.add("v1.2");
			pathParams.add("champion");
			queryParams.put("api_key", Commons.API_KEY);
			queryParams.put("freeToPlay", "true");
			ServiceRequest.getInstance().makeGetRequest(
					Commons.WEEKLY_FREE_CHAMPIONS_REQUEST, pathParams,
					queryParams, null, this);
		}
		return v;

	}
	
	private void initUI(View v){
		list = (ListView) v.findViewById(R.id.listViewWeeklyFreeChampions);
		list.setOnItemClickListener(this);
	}

	@Override
	public void onSuccess(Object response) {
		if (response instanceof WeeklyFreeChampionsResponse) {
			WeeklyFreeChampionsResponse resp = (WeeklyFreeChampionsResponse) response;

            if(resp != null && resp.getChampions() != null && resp.getChampions().size() > 0) {
                weeklyFreeChampIds = new ArrayList<String>();
                for (Champion c : resp.getChampions()) {
                    weeklyFreeChampIds.add(String.valueOf(c.getId()));
                }
                freeToPlayChampsSize = weeklyFreeChampIds.size();
                ArrayList<String> pathParams = new ArrayList<String>();
                pathParams.add("static-data");
                pathParams.add("tr");
                pathParams.add("v1.2");
                pathParams.add("champion");
                HashMap<String, String> queryParams = new HashMap<String, String>();
                queryParams.put("champData", "altimages");
                queryParams.put("api_key", Commons.API_KEY);
                ServiceRequest.getInstance().makeGetRequest(
                        Commons.ALL_CHAMPIONS_REQUEST,
                        pathParams, queryParams, null, this);
            }else{
                try{
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    WeeklyFreeChampionsFragment fragment = new WeeklyFreeChampionsFragment();
                    fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }catch (Exception e){
                    Commons.weeklyFreeChampions = null;
                }
            }

		} else if (response instanceof AllChampionsResponse) {
			AllChampionsResponse resp = (AllChampionsResponse) response;
			Map<String, Map<String, String>> data = resp.getData();
			if(Commons.weeklyFreeChampions != null){
				Commons.weeklyFreeChampions.clear();
			}else{
				Commons.weeklyFreeChampions = new ArrayList<Champion>();
			}
			for(Entry<String, Map<String, String>> entry : data.entrySet()){
				String key = entry.getKey();
				String id = entry.getValue().get("id");
				for(String weeklyFreeChampId : weeklyFreeChampIds){
					if(weeklyFreeChampId.equals(id)){
						String imageUrl = Commons.CHAMPION_IMAGE_BASE_URL + key + ".png";
						Champion c = new Champion();
						c.setChampionImageUrl(imageUrl);
						c.setChampionName(entry.getValue().get("name"));
						c.setId(Integer.parseInt(entry.getValue().get("id")));
						c.setKey(entry.getValue().get("key"));
						c.setDateInterval(Commons.getTuesday());
						Commons.weeklyFreeChampions.add(c);
					}
				}
				
			}
			if (Commons.weeklyFreeChampions.size() == freeToPlayChampsSize) {
				ServiceRequest.getInstance().makeGetRequest(
						Commons.CHAMPION_RP_IP_COSTS_REQUEST, null, null, null,
						this);
			}
		} else if (response instanceof ChampionRpIpCostsResponse) {
			ArrayList<Champion> weeklyFreeChampions = new ArrayList<Champion>();
			ChampionRpIpCostsResponse resp = (ChampionRpIpCostsResponse) response;
			ArrayList<String> keys = new ArrayList<String>();
			Map<String, Map<String, String>> costs = resp.getCosts();
			Iterator it = costs.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				String key = (String) pairs.getKey();
				for(Champion c : Commons.weeklyFreeChampions){
					if(String.valueOf(c.getId()).equals(key)){
						Map<String, String> keyValues = (Map<String, String>) pairs.getValue();
						c.setChampionRp(String.valueOf(keyValues.get("rp_cost")));
						c.setChampionIp(String.valueOf(keyValues.get("ip_cost")));
						weeklyFreeChampions.add(c);
					}
				}
			}
			Commons.weeklyFreeChampions = weeklyFreeChampions;
			adapter.notifyDataSetChanged();
		}

	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.listViewWeeklyFreeChampions:
			Champion c = (Champion) list.getItemAtPosition(position);
			int champId = c.getId();
			ChampionDetailFragment fragment = new ChampionDetailFragment();
			Bundle args = new Bundle();
			args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
			args.putString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL, c.getChampionImageUrl());
			args.putString(ChampionDetailFragment.EXTRA_CHAMPION_NAME, c.getKey());
			fragment.setArguments(args);
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onFailure(Object response) {
		String errorMessage = (String)response;
		Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	

}
