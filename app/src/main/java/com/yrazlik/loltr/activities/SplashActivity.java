package com.yrazlik.loltr.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.yrazlik.loltr.MainActivity;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.billing.PaymentSevice;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.view.RegionDialog;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by yrazlik on 1/13/16.
 */
public class SplashActivity extends Activity{

    public interface IsAppPurchasedListener {
        void onAppPurchaseResultReceived();
    }

    private boolean mainActivityStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(Commons.SELECTED_REGION == null || Commons.SELECTED_REGION.length() <= 0 || Commons.SELECTED_LANGUAGE == null || Commons.SELECTED_LANGUAGE.length() <= 0){
            setContentView(R.layout.activity_splash);
            Dialog d  = new RegionDialog(this);
            if(d != null){
                d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Commons.SELECTED_REGION == null || Commons.SELECTED_REGION.length() <= 0 || Commons.SELECTED_LANGUAGE == null || Commons.SELECTED_LANGUAGE.length() <= 0) {
                            setRegionAuomatically();
                            if(Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                                Toast.makeText(getApplicationContext(), "Region was set to " + Commons.SELECTED_REGION.toUpperCase(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Bolge, " + Commons.SELECTED_REGION.toUpperCase() + " olarak secildi.", Toast.LENGTH_SHORT).show();
                            }
                            getPurchaseEvent();
                        } else {
                            saveToSharedPrefs();
                            if(Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                                Toast.makeText(getApplicationContext(), "Region was set to " + Commons.SELECTED_REGION.toUpperCase(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Bolge, " + Commons.SELECTED_REGION.toUpperCase() + " olarak secildi.", Toast.LENGTH_SHORT).show();
                            }
                            getPurchaseEvent();
                        }
                    }

                });
                d.show();
            }else{
                setRegionAuomatically();
                getPurchaseEvent();
            }
        }else{
            getPurchaseEvent();
        }
    }

    private void startMainActivity(){
        Firebase firebase = new Firebase(getResources().getString(R.string.lol_firebase));
        firebase.child("latestVersion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String value = (String) postSnapshot.getValue();

                    if (key != null && key.equalsIgnoreCase("latestVersion")) {
                        if (value != null && value.length() > 0) {
                            Commons.LATEST_VERSION = value;
                        }
                    } else if (key != null && key.equalsIgnoreCase("latestItemVersion")) {
                        if (value != null && value.length() > 0) {
                            Commons.RECOMMENDED_ITEMS_VERSION = value;
                        }
                    }
                }
                Commons.updateLatestVersionVariables();
                if(!mainActivityStarted) {
                    mainActivityStarted = true;
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if(!mainActivityStarted) {
                    mainActivityStarted = true;
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });
    }

    private void setRegionAuomatically(){
        Locale locale = getApplicationContext().getResources().getConfiguration().locale;
        if(locale.getISO3Country().equalsIgnoreCase("tur") || locale.getISO3Language().equalsIgnoreCase("tur")){
            Commons.SELECTED_LANGUAGE = "tr";
            Commons.SELECTED_REGION = "tr";
            Locale myLocale = new Locale("tr");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }else{
            Commons.SELECTED_LANGUAGE = "en_us";
            Commons.SELECTED_REGION = "na";
            Locale myLocale = new Locale("en_us");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        saveToSharedPrefs();
    }

    private void saveToSharedPrefs(){
        try{
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_LANGUAGE, Commons.SELECTED_LANGUAGE).commit();
            prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_REGION, Commons.SELECTED_REGION).commit();
        }catch (Exception ignored){}
    }

    private void getPurchaseEvent() {
        boolean isPurchased = Commons.getInstance(getApplicationContext()).loadPurchaseData();
        if(isPurchased) {
            Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
            setupInAppPurchases(null);
            startMainActivity();
        } else {
            setupInAppPurchases(appPurchasedListener);
        }
    }

    private void setupInAppPurchases(IsAppPurchasedListener listener) {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        ServiceConnection mServiceConn = PaymentSevice.getInstance(this, listener, true).getServiceConnection();
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    private IsAppPurchasedListener appPurchasedListener = new IsAppPurchasedListener() {
        @Override
        public void onAppPurchaseResultReceived() {
            IInAppBillingService mService = PaymentSevice.getInstance(SplashActivity.this).getService();
            if(mService != null) {
                try {
                    Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
                    if(ownedItems != null) {
                        int response = ownedItems.getInt("RESPONSE_CODE");
                        if (response == 0) {
                            ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                            if(ownedSkus != null && ownedSkus.size() > 0) {
                                for (int i = 0; i < ownedSkus.size(); ++i) {
                                    String sku = ownedSkus.get(i);
                                    if(sku.equalsIgnoreCase(Commons.REMOVE_ADS_ID)) {
                                        Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
                                        Commons.getInstance(getApplicationContext()).savePurchaseData();
                                    }
                                }
                            }
                        }
                    }
                } catch (RemoteException e) {
                    Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
                }
            } else {
                Commons.getInstance(getApplicationContext()).ADS_ENABLED = false;
            }
            startMainActivity();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (PaymentSevice.getInstance(this).getServiceConnection() != null) {
            try {
                unbindService(PaymentSevice.getInstance(this).getServiceConnection());
            }catch (IllegalArgumentException ignored) {}
        }
    }
}
