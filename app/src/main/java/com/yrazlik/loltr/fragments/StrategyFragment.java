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
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class StrategyFragment extends BaseFragment implements ResponseListener{
	
	private int champId;
	private RobotoTextView allyTips, enemyTips, allyTipsTitle, enemyTipstitle;
	
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
		
		ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.CHAMPION_STRATEGY_REQUEST, pathParams, queryParams, null, false, this);
		
		return v;
	}
	
	private void getExtras(){
		Bundle args = getArguments();
		if(args != null){
			champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
		}
	}

	private void initUI(View v){
		allyTips = (RobotoTextView)v.findViewById(R.id.textviewAllyTips);
		enemyTips = (RobotoTextView)v.findViewById(R.id.textviewEnemyTips);
		allyTipsTitle = (RobotoTextView)v.findViewById(R.id.textviewAllyTipsTitle);
		enemyTipstitle = (RobotoTextView)v.findViewById(R.id.textviewEnemyTipsTitle);
	}
	
	@Override
	public void onSuccess(Object response) {
        if(isAttached) {
            if (response instanceof ChampionStrategyResponse) {
                ChampionStrategyResponse resp = (ChampionStrategyResponse) response;
                ArrayList<String> allyTipsArray = resp.getAllytips();
                ArrayList<String> enemyTipsArray = resp.getEnemytips();
                String allyTipsString = "";
                String enemyTipsString = "";
                for (String s : allyTipsArray) {
                    allyTipsString = allyTipsString + "-  " + s + "\n\n";
                }
                for (String s : enemyTipsArray) {
                    enemyTipsString = enemyTipsString + "-  " + s + "\n\n";
                }
                String key = resp.getKey();


                allyTipsTitle.setText(getString(R.string.playing_as, key));
                enemyTipstitle.setText(getString(R.string.playing_against, key));

                Commons.underline(allyTipsTitle);
                Commons.underline(enemyTipstitle);
                allyTips.setText(allyTipsString);
                enemyTips.setText(enemyTipsString);
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
        t.setScreenName("StrategyFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
