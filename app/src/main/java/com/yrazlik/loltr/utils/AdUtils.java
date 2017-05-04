package com.yrazlik.loltr.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;

import java.util.Date;

/**
 * Created by yrazlik on 03/05/17.
 */

public class AdUtils {

    private static final long AD_CACHE_TIMEOUT = 60 * 1000;
    private static AdUtils mInstance;

    private long lastAdRequestTime;
    private boolean adsEnabled = true;

    private NativeAd nativeAd;

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

    public void loadNativeAd(Context context, final String adUnitId) {
        if(adsEnabled) {
            new NativeAdLoader().loadAd(context, adUnitId, new NativeAdLoader.NativeAdLoadedListener() {
                @Override
                public void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
                    lastAdRequestTime = new Date().getTime();
                    updateAds(nativeAppInstallAd);
                }

                @Override
                public void onContentAdLoaded(NativeContentAd nativeContentAd) {
                    lastAdRequestTime = new Date().getTime();
                    updateAds(nativeContentAd);
                }

                @Override
                public void onAdFailedToLoad() {}
            });
        } else {
            makeAllAdsNull();
        }
    }

    private void updateAds(NativeAd nativeAd) {
        synchronized (nativeAd) {
            this.nativeAd = nativeAd;
        }
    }

    private void makeAllAdsNull() {
        synchronized (nativeAd) {
            nativeAd = null;
        }
    }

    public NativeAd getCachedAd() {
        if(shouldRequestNewAd()) {
            loadNativeAd(LolApplication.getAppContext(), LolApplication.getAppContext().getString(R.string.native_ad_unit_id));
        }
        return nativeAd;
    }

    private boolean shouldRequestNewAd() {
        long now = new Date().getTime();
        if(now - lastAdRequestTime > AD_CACHE_TIMEOUT) {
            return true;
        }
        return false;
    }
}
