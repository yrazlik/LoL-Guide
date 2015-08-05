package com.yrazlik.loltr;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.yrazlik.loltr.commons.Commons;

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

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
    }

    public static Tracker getTracker() {
        return tracker;
    }
}
