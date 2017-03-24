package com.yrazlik.loltr.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
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
import com.yrazlik.loltr.MainActivity;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.billing.PaymentSevice;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.view.RobotoButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yrazlik on 12/05/16.
 */
public class RemoveAdsFragment extends BaseFragment {

    private RobotoButton removeAdsButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_remove_ads, container, false);
        removeAdsButton = (RobotoButton) v.findViewById(R.id.removeAdsButton);
        removeAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPurchaseButtonClickEvent();
                ArrayList<String> skuList = new ArrayList<String>();
                skuList.add("remove_ads");
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
                                    if (sku.equalsIgnoreCase(Commons.REMOVE_ADS_ID)) {
                                        Bundle buyIntentBundle = PaymentSevice.getInstance(getActivity()).buyRemoveAdsItem(sku);
                                        if (buyIntentBundle != null) {
                                            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                                            try {
                                                getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                                                        Commons.REMOVE_ADS_REQUEST_CODE, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                                        Integer.valueOf(0));
                                            } catch (IntentSender.SendIntentException e) {
                                                sendPurchaseFailEvent();
                                            }
                                        } else {
                                            sendPurchaseFailEvent();
                                        }
                                    }
                                }
                            }
                        } catch (RemoteException e) {
                            sendPurchaseFailEvent();
                        } catch (JSONException e) {
                            sendPurchaseFailEvent();
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

    public void sendPurchaseButtonClickEvent() {
        try {
            Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
            t.send(new HitBuilders.EventBuilder().setCategory(Commons.PURCHASE_CLICK)
                    .setAction(Commons.PURCHASE_CLICK)
                    .setLabel(Commons.PURCHASE_CLICK)
                    .build());
        } catch (Exception e) {
        }
    }

    public void sendPurchaseFailEvent() {
        try {
            Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
            t.send(new HitBuilders.EventBuilder().setCategory(Commons.PURCHASE_FAIL)
                    .setAction(Commons.PURCHASE_FAIL)
                    .setLabel(Commons.PURCHASE_FAIL)
                    .build());
        } catch (Exception e) {
        }
    }
}
