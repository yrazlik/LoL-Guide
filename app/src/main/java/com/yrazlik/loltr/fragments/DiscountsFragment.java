package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 12/25/15.
 */
public class DiscountsFragment extends BaseFragment{

    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_discounts, container,
                false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setTabs(v);
            }
        }, 350);
        return v;
    }

    private void setTabs(View v){
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(new DiscountsPagerAdapter(getChildFragmentManager()));

        tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabs.setIndicatorColor(getResources().getColor(R.color.tab_color));
        tabs.setBackgroundColor(getResources().getColor(R.color.app_color));
        tabs.setDividerColor(getResources().getColor(R.color.white));
        tabs.setTextColor(getResources().getColor(R.color.white));
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getActivity().getResources().getDisplayMetrics());
        tabs.setTextSize(textSize);

        tabs.setIndicatorHeight(8);
        tabs.setViewPager(pager);
    }

    public class DiscountsPagerAdapter extends FragmentPagerAdapter {

        public DiscountsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return getResources().getString(R.string.champion_sale);
            }else {
                return getResources().getString(R.string.skin_sale);
            }
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                ChampionDiscountsFragment championDiscountsFragment = new ChampionDiscountsFragment();
                return championDiscountsFragment;
            }else{
                CostumeDiscountsFragment costumeDiscountsFragment = new CostumeDiscountsFragment();
                return costumeDiscountsFragment;
            }
        }
    }


    @Override
    public void reportGoogleAnalytics() {

    }
}
