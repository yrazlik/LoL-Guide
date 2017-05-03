package com.yrazlik.loltr.utils;

/**
 * Created by yrazlik on 03/05/17.
 */

public class AdUtils {

    private static AdUtils mInstance;

    private boolean adsEnabled = true;

    private AdUtils() {}

    public static AdUtils getInstance() {
        if(mInstance == null) {
            mInstance = new AdUtils();
        }
        return mInstance;
    }

    public void enableAds() {
        adsEnabled = true;
    }

    public void disableAds() {
        adsEnabled = false;
    }

    public boolean isAdsEnabled() {
        return adsEnabled;
    }

}
