package com.yrazlik.loltr.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;

/**
 * Created by yrazlik on 03/05/17.
 */

public class NativeAdLoader {

    public interface NativeAdLoadedListener {
        void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd);

        void onContentAdLoaded(NativeContentAd nativeContentAd);

        void onAdFailedToLoad();
    }

    private AdLoader adLoader;
    private NativeAdLoadedListener nativeAdLoadedListener;

    public void loadAd(final Context context, final String adUnitId, final NativeAdLoadedListener nativeAdLoadedListener) {

        if ((adLoader != null) && adLoader.isLoading()) {
            return;
        }

        this.nativeAdLoadedListener = nativeAdLoadedListener;

        adLoader = new AdLoader.Builder(context, adUnitId)
                .forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                    @Override
                    public void onAppInstallAdLoaded(NativeAppInstallAd nativeAppInstallAd) {
                        if(nativeAdLoadedListener != null) {
                            nativeAdLoadedListener.onAppInstallAdLoaded(nativeAppInstallAd);
                        }
                    }
                })
                .forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @Override
                    public void onContentAdLoaded(NativeContentAd nativeContentAd) {
                        if(nativeAdLoadedListener != null) {
                            nativeAdLoadedListener.onContentAdLoaded(nativeContentAd);
                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int i) {
                        if(nativeAdLoadedListener != null) {
                            nativeAdLoadedListener.onAdFailedToLoad();
                        }
                    }
                }).withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().addTestDevice("30A5B8102FD434AF380A8BC930C13642").build());
    }
}
