package com.yrazlik.loltr.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.activities.FullScreenImageActivity;
import com.yrazlik.loltr.adapters.CostumeDiscountsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.ChampionDiscount;
import com.yrazlik.loltr.data.CostumeDiscount;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.utils.AdUtils;
import com.yrazlik.loltr.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by yrazlik on 12/25/15.
 */
public class CostumeDiscountsFragment extends BaseFragment implements ResponseListener, AdapterView.OnItemClickListener{

    private ListView discountsLV;
    private CostumeDiscountsAdapter adapter;
    private ArrayList<CostumeDiscount> costumeDiscounts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView == null) {
            showProgressWithWhiteBG();
            rootView = inflater.inflate(R.layout.fragment_discount_costumes, container, false);
            discountsLV = (ListView) rootView.findViewById(R.id.discountLV);
            discountsLV.setOnItemClickListener(this);
            costumeDiscounts = new ArrayList<>();

            if (LolApplication.firebaseInitialized) {
                try {
                    Firebase firebase = new Firebase(getResources().getString(R.string.lol_firebase));
                    firebase.child(getLocalizedCostumeDiscounts()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                HashMap<String, Object> keyValues = (HashMap<String, Object>) postSnapshot.getValue();
                                if (keyValues != null && keyValues.size() > 0) {

                                    String startDate = (String) keyValues.get("startDate");
                                    String endDate = (String) keyValues.get("endDate");
                                    String priceBeforeDiscount = (String) keyValues.get("priceBeforeDiscount");
                                    String priceAfterDiscount = (String) keyValues.get("priceAfterDiscount");
                                    String name = (String) keyValues.get("name");
                                    String imageUrl = (String) keyValues.get("imageUrl");
                                    String createdAt = (String) keyValues.get("createdAt");
                                    if (createdAt != null) {
                                        try {
                                            String[] values = createdAt.split("\\.");
                                            int day = Integer.parseInt(values[0]);
                                            int month = Integer.parseInt(values[1]);
                                            int year = Integer.parseInt(values[2]);
                                            Calendar c = Calendar.getInstance();
                                            c.set(year, month - 1, day, 0, 0);
                                            CostumeDiscount costumeDiscount = new CostumeDiscount(startDate, endDate, name, priceBeforeDiscount, priceAfterDiscount, imageUrl, c.getTime());
                                            costumeDiscounts.add(costumeDiscount);
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                            }
                            dismissProgress();
                            costumeDiscounts = sortCostumeDiscountsByDateCreated(costumeDiscounts);
                            if (costumeDiscounts != null && costumeDiscounts.size() > 0) {
                                Collections.reverse(costumeDiscounts);
                                addAdsViewToDiscounts();
                                adapter = new CostumeDiscountsAdapter(getContext(), R.layout.list_row_discount_champions, costumeDiscounts);
                                discountsLV.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            dismissProgress();
                        }
                    });

                } catch (Exception e) {
                    dismissProgress();
                }
            } else {
                dismissProgress();
            }
        }
        return rootView;
    }

    private void addAdsViewToDiscounts() {
        NativeAd nativeAd = AdUtils.getInstance().getCachedAd();
        if(nativeAd != null) {
            CostumeDiscount ad = new CostumeDiscount();
            ad.setAd(true);
            ad.setNativeAd(nativeAd);

         /*   CostumeDiscount ad2 = new CostumeDiscount();
            ad2.setAd(true);
            ad2.setNativeAd(nativeAd);*/

            try {
                costumeDiscounts.add(4, ad);
              //  costumeDiscounts.add(ad2);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("CostumeDiscountsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private ArrayList<CostumeDiscount> sortCostumeDiscountsByDateCreated(ArrayList<CostumeDiscount> costumeDiscounts){
        try{
            Collections.sort(costumeDiscounts, new Comparator<CostumeDiscount>() {
                @Override
                public int compare(CostumeDiscount d1, CostumeDiscount d2) {
                    return d1.getCreatedAt().compareTo(d2.getCreatedAt());
                }
            });
        }catch (Exception e){
            return null;
        }
        return costumeDiscounts;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            CostumeDiscount d = (CostumeDiscount) parent.getAdapter().getItem(position);
            if(!d.isAd()) {
                Intent i = new Intent(getActivity(), FullScreenImageActivity.class);
                i.putExtra(FullScreenImageActivity.EXTRA_IMAGE_URL, d.getImageUrl());
                startActivity(i);
            }
        } catch (Exception ignored) {}
    }

    public String getLocalizedCostumeDiscounts() {
        return "costume-discounts" + "-" +  LocalizationUtils.getInstance().getLocale().toLowerCase();
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
