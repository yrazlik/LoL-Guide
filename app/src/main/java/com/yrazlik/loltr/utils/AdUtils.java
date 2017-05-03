package com.yrazlik.loltr.utils;

import android.content.Context;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.yrazlik.loltr.LolApplication;

/**
 * Created by yrazlik on 03/05/17.
 */

public class AdUtils {

    private static AdUtils mInstance;

    private NativeAdLoader nativeAdLoader;
    private boolean adsEnabled = true;

    private NativeAppInstallAd newsInstallAd;
    private NativeContentAd newsContentAd;

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
        //TODO: make it false
        adsEnabled = true;
    }

    public boolean isAdsEnabled() {
        return adsEnabled;
    }

    public void loadNativeAd(Context context, String adUnitId, NativeAdLoader.NativeAdLoadedListener nativeAdLoadedListener) {
        if(adsEnabled) {
            new NativeAdLoader().loadAd(context, adUnitId, nativeAdLoadedListener);
        } else {
            if(nativeAdLoadedListener != null) {
                nativeAdLoadedListener.onAdFailedToLoad();
            }
        }
    }

  /*  public NativeAd getCachedAd(String adUnitId) {
        NativeAd adToReturn = null;
        if(newsInstallAd != null) {
            adToReturn = newsInstallAd;
        } else if(newsContentAd != null) {
            adToReturn = newsContentAd;
        }

        loadNativeAd(LolApplication.getAppContext(), adUnitId);

        return adToReturn;
    }*/
}
