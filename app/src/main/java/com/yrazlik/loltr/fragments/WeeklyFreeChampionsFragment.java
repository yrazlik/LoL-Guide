package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
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

public class WeeklyFreeChampionsFragment extends BaseFragment implements
		ResponseListener, OnItemClickListener {

	ListView list;
	ListAdapter adapter;
	int freeToPlayChampsSize;
	ArrayList<String> weeklyFreeChampIds;
    int weeklyFreeChampsTrialCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_weekly_free_champions,
				container, false);
		initUI(v);

		if (Commons.weeklyFreeChampions != null) {
            ArrayList<Champion>copyOfWeeklyFreeChampions = new ArrayList<Champion>();
            for(Champion c : Commons.weeklyFreeChampions){
                Champion c2 = new Champion();
                c2.setChampionImageUrl(c.getChampionImageUrl());
                c2.setChampionName(c.getChampionName());
                c2.setChampionRp(c.getChampionRp());
                c2.setChampionIp(c.getChampionIp());
                c2.setBotMmEnabled(c.isBotMmEnabled());
                c2.setId(c.getId());
                c2.setTitle(c.getTitle());
                c2.setRankedPlayEnabled(c.isRankedPlayEnabled());
                c2.setBotEnabled(c.isBotEnabled());
                c2.setActive(c.isActive());
                c2.setFreeToPlay(c.isFreeToPlay());
                c2.setKey(c.getKey());
                c2.setTeamId(c.getTeamId());
                c2.setPickTurn(c.getPickTurn());
                c2.setChampionId(c.getChampionId());
                c2.setDateInterval(Commons.getTuesday());
                copyOfWeeklyFreeChampions.add(c2);
            }
            Commons.weeklyFreeChampions = copyOfWeeklyFreeChampions;
			adapter = new ListAdapter(getActivity(), R.layout.list_row,
					Commons.weeklyFreeChampions);
			list.setAdapter(adapter);
		} else {
			Commons.weeklyFreeChampions = new ArrayList<Champion>();
			adapter = new ListAdapter(getActivity(), R.layout.list_row,
					Commons.weeklyFreeChampions);
			list.setAdapter(adapter);

            makeWeeklyFreeChampsRequest();
		}
		return v;

	}

    private void makeWeeklyFreeChampsRequest(){
        ArrayList<String> pathParams = new ArrayList<String>();
        HashMap<String, String> queryParams = new HashMap<String, String>();
        pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
        pathParams.add("v1.2");
        pathParams.add("champion");
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
        queryParams.put("api_key", Commons.API_KEY);
        queryParams.put("freeToPlay", "true");
        ServiceRequest.getInstance(getContext()).makeGetRequest(
                Commons.WEEKLY_FREE_CHAMPIONS_REQUEST, pathParams,
                queryParams, null, this);
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
                pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
                pathParams.add("v1.2");
                pathParams.add("champion");
                HashMap<String, String> queryParams = new HashMap<String, String>();
                queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
                queryParams.put("champData", "altimages");
                queryParams.put("api_key", Commons.API_KEY);
                ServiceRequest.getInstance(getContext()).makeGetRequest(
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
				ServiceRequest.getInstance(getContext()).makeGetRequest(
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
			FragmentTransaction ft = fm.beginTransaction();
            Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
            ft.addToBackStack(Commons.CHAMPION_DETAILS_FRAGMENT).replace(R.id.content_frame, fragment).commit();
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onFailure(Object response) {
        try {
            String errorMessage = (String) response;
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }catch (Exception ignored){}
        Commons.weeklyFreeChampions = null;
        if(weeklyFreeChampsTrialCount < 3){
            weeklyFreeChampsTrialCount++;
            makeWeeklyFreeChampsRequest();
        }else{
            Toast.makeText(getContext(), R.string.anErrorOccured, Toast.LENGTH_LONG).show();
        }
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
        t.setScreenName("WeeklyFreeChampionsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
