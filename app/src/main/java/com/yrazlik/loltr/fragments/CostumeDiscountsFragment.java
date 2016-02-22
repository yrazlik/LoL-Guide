package com.yrazlik.loltr.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.CostumeDiscountsAdapter;
import com.yrazlik.loltr.data.Discount;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by yrazlik on 12/25/15.
 */
public class CostumeDiscountsFragment extends BaseFragment implements ResponseListener{

    private ListView discountsLV;
    private CostumeDiscountsAdapter adapter;
    private ArrayList<Discount> discounts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discount_costumes, container, false);
        discountsLV = (ListView) v.findViewById(R.id.discountLV);
        discounts = new ArrayList<>();

        final Dialog progress = ServiceRequest.showLoading(getContext());
        if(progress != null){
            progress.show();
        }
        


     /*   try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Discount");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    try {
                        if(e == null) {
                            for (ParseObject post : list) {
                                String discountType = post.getString("discountType");
                                if(discountType != null && discountType.equalsIgnoreCase("costume")){
                                    String startDate = post.getString("startDate");
                                    String endDate = post.getString("endDate");
                                    String name = post.getString("name");
                                    String nameEnglish = post.getString("nameEnglish");
                                    String priceBeforeDiscount = post.getString("priceBeforeDiscount");
                                    String priceAfterDiscount = post.getString("priceAfterDiscount");
                                    String imageUrl = post.getString("imageUrl");
                                    Date createdAt = post.getCreatedAt();
                                    Discount discount = new Discount(discountType, startDate, endDate, name, nameEnglish, priceBeforeDiscount, priceAfterDiscount, imageUrl, createdAt);
                                    discounts.add(discount);
                                }
                            }
                            hideProgress();
                            discounts = sortByDateCreated(discounts);
                            if(discounts != null && discounts.size() > 0) {
                                Collections.reverse(discounts);
                                adapter = new CostumeDiscountsAdapter(getContext(), R.layout.list_row_discount_costumes, discounts);
                                discountsLV.setAdapter(adapter);
                            }
                        }else{
                            hideProgress();
                            try {
                                Toast.makeText(getContext(), R.string.networkError, Toast.LENGTH_SHORT).show();
                            }catch (Exception ignored){}
                        }
                    }catch (Exception ignored){
                        hideProgress();
                    }
                }
            });

        }catch (Exception e){
        }
*/
        return v;
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
        t.setScreenName("CostumeDiscountsFragment");
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
