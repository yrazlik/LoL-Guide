package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.BuildConfig;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ChampionLegendResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.utils.LocalizationUtils;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LegendFragment extends BaseFragment implements ResponseListener{

	private RobotoTextView legend;
	private int champId;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_legend, container, false);
		initUI(v);
		getExtras();
		
		ArrayList<String> pathParams = new ArrayList<String>();
		pathParams.add("static-data");
		pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
		pathParams.add("v1.2");
		pathParams.add("champion");
		pathParams.add(String.valueOf(champId));
		HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", LocalizationUtils.getInstance().getLocale());
		queryParams.put("champData", "lore");
		queryParams.put("api_key", BuildConfig.API_KEY);
		
		ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_LEGEND_REQUEST, pathParams, queryParams, null, this);
		
		return v;
	}
	
	private void getExtras(){
		Bundle args = getArguments();
		if(args != null){
			champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
		}
	}
	
	private void initUI(View v){
		legend = (RobotoTextView)v.findViewById(R.id.textviewLegend);
	}

	private String formatLoreString(String lore) {
		if(lore.contains("<br>")){
			lore = lore.replaceAll("<br>", "\n");
		}
		return lore;
	}
	
	@Override
	public void onSuccess(Object response) {
        if(isAttached) {
            if (response instanceof ChampionLegendResponse) {
                ChampionLegendResponse resp = (ChampionLegendResponse) response;
                String lore = resp.getLore();
                lore = formatLoreString(lore);
                legend.setText(lore);
            }
        }
	}

	@Override
	public void onFailure(Object response) {
        if(isAttached) {
            String errorMessage = (String) response;
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
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
        t.setScreenName("LegendFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
