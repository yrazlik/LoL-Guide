package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 12/25/15.
 */
public class DiscountsFragment extends BaseFragment{

    private FragmentTabHost tabhost;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discounts, container,
                false);
        setTabhost(v);

        return v;
    }

    private void setTabhost(View v){
        tabhost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        tabhost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        tabhost.addTab(
                tabhost.newTabSpec("tabChampions").setIndicator(getResources().getString(R.string.champion_sale), null),
                ChampionDiscountsFragment.class, null);

        tabhost.addTab(
                tabhost.newTabSpec("tabCostumes").setIndicator(getResources().getString(R.string.skin_sale), null),
                CostumeDiscountsFragment.class, null);

        TabWidget tw = (TabWidget)tabhost.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getChildCount(); ++i)
        {
            View tabView = tw.getChildTabViewAt(i);
            TextView tv = (TextView)tabView.findViewById(android.R.id.title);
            tv.setTextSize(10);
            // tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selected2);
        }
        //  tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.unselected2);
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

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
    public void reportGoogleAnalytics() {

    }
}
