package com.yrazlik.loltr;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yrazlik.loltr.commons.Commons;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * Created by yrazlik on 3/3/15.
 */
public class LolApplication extends MultiDexApplication{

    public static boolean firebaseInitialized;
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    public static ImageLoader imageLoader;
    public InterstitialAd mInterstitialAd;
    public static Context mAppContext;
    static int ad_show = 0;

    public InterstitialAd getmInterstitialAd() {
        return mInterstitialAd;
    }

    public void setmInterstitialAd(InterstitialAd mInterstitialAd) {
        this.mInterstitialAd = mInterstitialAd;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();

        try {
            mInterstitialAd = new InterstitialAd(getApplicationContext());
            mInterstitialAd.setAdUnitId("ca-app-pub-3219973945608696/2334363161");

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                }
            });
            requestNewInterstitial();
        }catch (Exception ignored){}


        try {
            Firebase.setAndroidContext(this);
            firebaseInitialized = true;

        }catch (Exception e){
            firebaseInitialized = false;
        }

        try {
            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            tracker = analytics.newTracker(Commons.GOOGLE_ANALYTICS_TRACKING_ID);
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(false);
            tracker.enableAutoActivityTracking(true);

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            SharedPreferences prefs = getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
            String language = prefs.getString(Commons.LOL_TR_SHARED_PREF_LANGUAGE, null);
            String region = prefs.getString(Commons.LOL_TR_SHARED_PREF_REGION, null);

            if(language == null || region == null){
                Commons.SELECTED_LANGUAGE = null;
                Commons.SELECTED_REGION = null;
            }else{
                Commons.SELECTED_LANGUAGE = language;
                Commons.SELECTED_REGION = region;
                if(language.equalsIgnoreCase("tr")){
                    Locale myLocale = new Locale("tr");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }else{
                    Locale myLocale = new Locale("en_us");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }

            }


        }catch (Exception e){
            Commons.SELECTED_LANGUAGE = "en_us";
            Commons.SELECTED_REGION = "na";
            Locale myLocale = new Locale("en_us");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
    }

    public void requestNewInterstitial() {
        try {
            if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }
        }catch (Exception ignored){}
    }

    public void showInterstitial(){
        if(Commons.getInstance(getApplicationContext()).ADS_ENABLED) {
            if (mInterstitialAd.isLoaded()) {
                try {
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mInterstitialAd.show();
                            } catch (Exception ignored) {
                            }
                        }
                    }, 350);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public  boolean shouldShowInterstitial(){
        ad_show++;
        if(ad_show == 3){
            return true;
        } else if(ad_show != 0 && ad_show % 10 == 0){
            return true;
        }
        return false;
    }


    public static Tracker getTracker() {
        return tracker;
    }
}
