package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;


public class ChampionDetailFragment extends BaseFragment {
	
	public static String EXTRA_CHAMPION_ID = "com.yrazlik.leagueoflegends.fragments.championdetailfragment.extrachampionid";
	public static String EXTRA_CHAMPION_IMAGE_URL ="com.yrazlik.leagueoflegends.fragments.championdetailfragment.extrachampionimageurl";
	public static String EXTRA_CHAMPION_NAME = "com.yrazlik.leagueoflegends.fragments.championdetailfragment.extrachampionname";

	private FragmentTabHost tabhost;
	private int champId;
	private String champLogoImageUrl;
	private String splashImageUrl;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_champion_detail, container,
				false);
		getExtras();
		setTabhost(v);
		
		return v;
	}
	
	private void getExtras(){
		Bundle args = getArguments();
		if(args != null){
			champId = args.getInt(EXTRA_CHAMPION_ID);
			champLogoImageUrl = args.getString(EXTRA_CHAMPION_IMAGE_URL);
			splashImageUrl = args.getString(EXTRA_CHAMPION_NAME);
		}
	}
	
	private void setTabhost(View v){
		tabhost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
		tabhost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
		Bundle args = new Bundle();
		args.putInt(EXTRA_CHAMPION_ID, champId);
		args.putString(EXTRA_CHAMPION_IMAGE_URL, champLogoImageUrl);
		args.putString(EXTRA_CHAMPION_NAME, splashImageUrl);
		tabhost.addTab(
                tabhost.newTabSpec("tabGeneral").setIndicator(getResources().getString(R.string.general), null),
                ChampionOverviewFragment.class, args);
		
		tabhost.addTab(
                tabhost.newTabSpec("tabAbilities").setIndicator(getResources().getString(R.string.abilities), null),
                ChampionSpellsFragment.class, args);
		tabhost.addTab(
                tabhost.newTabSpec("tabLegend").setIndicator(getResources().getString(R.string.legend), null),
                LegendFragment.class, args);
		tabhost.addTab(
                tabhost.newTabSpec("tabStrategy").setIndicator(getResources().getString(R.string.strategy), null),
                StrategyFragment.class, args);
		
		TabWidget tw = (TabWidget)tabhost.findViewById(android.R.id.tabs);
	    for (int i = 0; i < tw.getChildCount(); ++i)
	    {
	        View tabView = tw.getChildTabViewAt(i);
	        TextView tv = (TextView)tabView.findViewById(android.R.id.title);
	        tv.setTextSize(8);
	       // tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selected2);
	    }
	  //  tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.unselected2);
	    tabhost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				TabWidget tw = (TabWidget)tabhost.findViewById(android.R.id.tabs);
				for (int i = 0; i < tw.getChildCount(); ++i)
			    {
			      //  tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selected2);
			    }
			//	tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.unselected2);
			}
		});
	}

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("ChampionDetailFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
