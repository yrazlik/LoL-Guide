package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.billing.PaymentSevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yrazlik on 12/05/16.
 */
public class RemoveAdsFragment extends BaseFragment{

    private Button removeAdsButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_remove_ads, container, false);
        removeAdsButton = (Button) v.findViewById(R.id.removeAdsButton);
        removeAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> skuList = new ArrayList<String>();
                skuList.add("premiumUpgrade");
                skuList.add("gas");
                final Bundle querySkus = new Bundle();
                querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
                final Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bundle skuDetails = PaymentSevice.getInstance(getActivity()).getService().getSkuDetails(3, getActivity().getPackageName(), "inapp", querySkus);
                            int response = skuDetails.getInt("RESPONSE_CODE");
                            if (response == 0) {
                                ArrayList<String> responseList
                                        = skuDetails.getStringArrayList("DETAILS_LIST");

                                for (String thisResponse : responseList) {
                                    JSONObject object = new JSONObject(thisResponse);
                                    String sku = object.getString("productId");
                                    String price = object.getString("price");
                                    /*    if (sku.equals("premiumUpgrade")) mPremiumUpgradePrice = price;
                                        else if (sku.equals("gas")) mGasPrice = price;*/
                                }
                            }
                        } catch (RemoteException e) {

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        });
        reportGoogleAnalytics();
        return v;
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("RemoveAdsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
