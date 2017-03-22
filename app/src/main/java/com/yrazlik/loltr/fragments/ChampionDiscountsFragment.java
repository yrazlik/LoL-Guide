package com.yrazlik.loltr.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.ChampionDiscountsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Discount;
import com.yrazlik.loltr.data.News;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yrazlik on 12/25/15.
 */
public class ChampionDiscountsFragment extends BaseFragment implements ResponseListener, AdapterView.OnItemClickListener{
    private ListView discountsLV;
    private ChampionDiscountsAdapter adapter;
    private ArrayList<Discount> discounts;
    private Dialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_champion_discount, container, false);
            discountsLV = (ListView) rootView.findViewById(R.id.discountLV);
            discounts = new ArrayList<>();
            progress = ServiceRequest.showLoading(getContext());
            discountsLV.setOnItemClickListener(this);
            showProgress();


            if (LolApplication.firebaseInitialized) {
                try {
                    Firebase firebase = new Firebase(getResources().getString(R.string.lol_firebase));
                    firebase.child("discounts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                HashMap<String, Object> keyValues = (HashMap<String, Object>) postSnapshot.getValue();
                                if (keyValues != null && keyValues.size() > 0) {
                                    String discountType = (String) keyValues.get("discountType");

                                    if (discountType != null && discountType.equalsIgnoreCase("champion")) {
                                        String startDate = (String) keyValues.get("startDate");
                                        String endDate = (String) keyValues.get("endDate");
                                        String priceBeforeDiscount = (String) keyValues.get("priceBeforeDiscount");
                                        String priceAfterDiscount = (String) keyValues.get("priceAfterDiscount");
                                        String name = (String) keyValues.get("name");
                                        String nameEnglish = (String) keyValues.get("nameEnglish");
                                        String imageUrl = (String) keyValues.get("imageUrl");
                                        String createdAt = (String) keyValues.get("createdAt");

                                        long championId = 0;
                                        try {
                                            championId = (long) keyValues.get("champId");
                                        } catch (Exception ignored) {}

                                        if (createdAt != null) {
                                            try {
                                                String[] values = createdAt.split("\\.");
                                                int day = Integer.parseInt(values[0]);
                                                int month = Integer.parseInt(values[1]);
                                                int year = Integer.parseInt(values[2]);
                                                Calendar c = Calendar.getInstance();
                                                c.set(year, month - 1, day, 0, 0);
                                                Discount discount = new Discount(discountType, startDate, endDate, name, nameEnglish, priceBeforeDiscount, priceAfterDiscount, imageUrl, championId, c.getTime());
                                                discounts.add(discount);
                                            } catch (Exception ignored) {
                                            }
                                        }
                                    }
                                }
                            }
                            hideProgress();
                            discounts = sortByDateCreated(discounts);
                            if (discounts != null && discounts.size() > 0) {
                                Collections.reverse(discounts);
                                adapter = new ChampionDiscountsAdapter(getContext(), R.layout.list_row_discount_champions, discounts);
                                discountsLV.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            hideProgress();
                        }
                    });

                } catch (Exception e) {
                    hideProgress();
                }
            } else {
                hideProgress();
            }
        }

        return rootView;
    }

    private void hideProgress(){
        ((ActionBarActivity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ServiceRequest.hideLoading();
            }
        });
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("ChampionDiscountsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private ArrayList<Discount> sortByDateCreated(ArrayList<Discount> discounts){
        try{
            Collections.sort(discounts, new Comparator<Discount>() {
                @Override
                public int compare(Discount d1, Discount d2) {
                     return d1.getCreatedAt().compareTo(d2.getCreatedAt());
                }
            });
        }catch (Exception e){
            return null;
        }
        return discounts;
    }

    private void showProgress(){
        if(progress != null){
            progress.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Discount d = (Discount) discountsLV.getItemAtPosition(position);
            int champId = (int) d.getChampionId();
            if (champId != 0) {
                ChampionDetailFragment fragment = new ChampionDetailFragment();
                Bundle args = new Bundle();
                args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
                args.putString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL, d.getImageUrl());
                args.putString(ChampionDetailFragment.EXTRA_CHAMPION_NAME, d.getName());
                fragment.setArguments(args);
                FragmentManager fm = getParentFragment().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);
                ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.CHAMPION_DETAILS_FRAGMENT).commit();
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onSuccess(Object response) {

    }

    @Override
    public void onFailure(Object response) {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
