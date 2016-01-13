package com.yrazlik.loltr;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.yrazlik.loltr.commons.Commons;

import java.util.Locale;

/**
 * Created by yrazlik on 3/3/15.
 */
public class LolApplication extends Application{

    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    public static ImageLoader imageLoader;


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ParseCrashReporting.enable(this);
            Parse.initialize(this, "tjNvuPzFqKLUGV3KjxKnsIK7qztkvorEkDCrn0Bz", "T0iDbKd213pDduIWFupDYVCusdwKeoSJUAWoRwSR");
        }catch (Exception e){
            e.printStackTrace();
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
                Commons.REGION_SELECTED = false;
               /* Locale locale = getApplicationContext().getResources().getConfiguration().locale;
                if(locale.getISO3Country().equalsIgnoreCase("tur") || locale.getISO3Language().equalsIgnoreCase("tur")){
                    Commons.SELECTED_LANGUAGE = "tr";
                    Commons.SELECTED_REGION = "tr";
                    Commons.SERVICE_BASE_URL_SELECTED = Commons.SERVICE_BASE_URL_TR;
                    Locale myLocale = new Locale("tr");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }else{
                    Commons.SELECTED_LANGUAGE = "en_us";
                    Commons.SELECTED_REGION = "na";
                    Commons.SERVICE_BASE_URL_SELECTED = Commons.SERVICE_BASE_URL_NA;
                    Locale myLocale = new Locale("en_us");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }*/
            }else{
                Commons.REGION_SELECTED = true;
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

                Commons.SERVICE_BASE_URL_SELECTED = "https://" + Commons.SELECTED_REGION +".api.pvp.net/api/lol";
            }


        }catch (Exception e){
            Commons.SELECTED_LANGUAGE = "en_us";
            Commons.SELECTED_REGION = "na";
            Commons.SERVICE_BASE_URL_SELECTED = Commons.SERVICE_BASE_URL_NA;
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

    public static Tracker getTracker() {
        return tracker;
    }
}
