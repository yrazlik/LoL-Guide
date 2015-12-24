package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.graphics.Paint;
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
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ChampionStrategyResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class StrategyFragment extends BaseFragment implements ResponseListener{
	
	private int champId;
	private TextView allyTips, enemyTips, allyTipsTitle, enemyTipstitle;
	private Typeface typeFace, typeFaceBold;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_strategy, container, false);
		
		initUI(v);
		getExtras();
		
		ArrayList<String> pathParams = new ArrayList<String>();
		pathParams.add("static-data");
		pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
		pathParams.add("v1.2");
		pathParams.add("champion");
		pathParams.add(String.valueOf(champId));
		HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
		queryParams.put("version", Commons.LATEST_VERSION);
		queryParams.put("champData", "allytips,enemytips");
		queryParams.put("api_key", Commons.API_KEY);
		
		ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_STRATEGY_REQUEST, pathParams, queryParams, null, this);
		
		return v;
	}
	
	private void getExtras(){
		Bundle args = getArguments();
		if(args != null){
			champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
		}
	}

	private void initUI(View v){
		typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/dinproregular.ttf");
		typeFaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/dinprobold.ttf");
		allyTips = (TextView)v.findViewById(R.id.textviewAllyTips);
		enemyTips = (TextView)v.findViewById(R.id.textviewEnemyTips);
		allyTipsTitle = (TextView)v.findViewById(R.id.textviewAllyTipsTitle);
		enemyTipstitle = (TextView)v.findViewById(R.id.textviewEnemyTipsTitle);
	}
	
	@Override
	public void onSuccess(Object response) {
		if(response instanceof ChampionStrategyResponse){
			ChampionStrategyResponse resp = (ChampionStrategyResponse)response;
			ArrayList<String> allyTipsArray = resp.getAllytips();
			ArrayList<String>enemyTipsArray = resp.getEnemytips();
			String allyTipsString = "";
			String enemyTipsString = "";
			for(String s : allyTipsArray){
				allyTipsString = allyTipsString + "-  " + s + "\n\n";
			}
			for(String s : enemyTipsArray){
				enemyTipsString = enemyTipsString + "-  " + s + "\n\n";
			}
			String key = resp.getKey();

            String region = Commons.getLanguage();
            if(region.equalsIgnoreCase("tr")) {
                allyTipsTitle.setText(key + " Olarak Oynarken:");
                enemyTipstitle.setText("Rakip " + key + " Iken:");
            }else{
                allyTipsTitle.setText("When you are playing as " + key + ":");
                enemyTipstitle.setText("When the opponent is " + key + ":");
            }
			allyTipsTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
			enemyTipstitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
			allyTips.setText(allyTipsString);
			enemyTips.setText(enemyTipsString);
			allyTips.setTypeface(typeFace);
			enemyTips.setTypeface(typeFace);
			allyTipsTitle.setTypeface(typeFaceBold);
			enemyTipstitle.setTypeface(typeFaceBold);
			
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

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("StrategyFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
