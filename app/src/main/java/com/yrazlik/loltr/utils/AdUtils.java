package com.yrazlik.loltr.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.Date;

/**
 * Created by yrazlik on 03/05/17.
 */

public class AdUtils {

    private static final long AD_CACHE_TIMEOUT = 90 * 1000;
    private static AdUtils mInstance;

    private long lastAdRequestTime;
    private boolean adsEnabled = true;
    private LayoutInflater mLayoutInflater;

    private NativeAd nativeAd;

    private AdUtils() {
        mLayoutInflater = LayoutInflater.from(LolApplication.getAppContext());
    }

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
        adsEnabled = false;
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
        if(adsEnabled) {
            if (shouldRequestNewAd()) {
                loadNativeAd(LolApplication.getAppContext(), LolApplication.getAppContext().getString(R.string.native_ad_unit_id));
            }
            return nativeAd;
        }
        return null;
    }

    private boolean shouldRequestNewAd() {
        long now = new Date().getTime();
        if(now - lastAdRequestTime > AD_CACHE_TIMEOUT) {
            return true;
        }
        return false;
    }

    public NativeAdView createLargeAdView() {
        if(nativeAd instanceof NativeAppInstallAd) {
            return createLargeNativeAppInstallAdView(getCachedAd());
        } else if(nativeAd instanceof NativeContentAd) {
            return createLargeNativeContentAdView(getCachedAd());
        }
        return null;
    }

    private NativeAppInstallAdView createLargeNativeAppInstallAdView(NativeAd ad) {
        try {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) ad;
            NativeAppInstallAdView adView = (NativeAppInstallAdView) mLayoutInflater.inflate(R.layout.large_nativeinstalladview, null, false);
            RelativeLayout adContainerView = (RelativeLayout) adView.findViewById(R.id.adContainerView);
            ImageView adIV = (ImageView) adView.findViewById(R.id.adIV);
            RobotoTextView headlineTV = (RobotoTextView) adView.findViewById(R.id.headlineTV);
            RobotoTextView bodyTV = (RobotoTextView) adView.findViewById(R.id.bodyTV);
            RobotoTextView callToActionTV = (RobotoTextView) adView.findViewById(R.id.callToActionTV);

            headlineTV.setText(nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(headlineTV);

            bodyTV.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(bodyTV);

            if(nativeAppInstallAd.getImages() != null && nativeAppInstallAd.getImages().size() > 0) {
                adIV.setImageDrawable(nativeAppInstallAd.getImages().get(0).getDrawable());
            } else {
                adIV.setImageDrawable(LolApplication.getAppContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setIconView(adIV);

            callToActionTV.setText(Utils.makeCamelCase(nativeAppInstallAd.getCallToAction().toString()));
            adView.setCallToActionView(adContainerView);
            adView.setNativeAd(nativeAppInstallAd);

            return adView;
        } catch (Exception e) {
            return null;
        }
    }

    private NativeContentAdView createLargeNativeContentAdView(NativeAd ad) {
        try {
            NativeContentAd nativeContentAd = (NativeContentAd) ad;
            NativeContentAdView adView = (NativeContentAdView) mLayoutInflater.inflate(R.layout.large_contentadview, null, false);
            RelativeLayout adContainerView = (RelativeLayout) adView.findViewById(R.id.adContainerView);
            ImageView adIV = (ImageView) adView.findViewById(R.id.adIV);
            RobotoTextView headlineTV = (RobotoTextView) adView.findViewById(R.id.headlineTV);
            RobotoTextView bodyTV = (RobotoTextView) adView.findViewById(R.id.bodyTV);
            RobotoTextView callToActionTV = (RobotoTextView) adView.findViewById(R.id.callToActionTV);

            headlineTV.setText(nativeContentAd.getHeadline());
            adView.setHeadlineView(headlineTV);

            bodyTV.setText(nativeContentAd.getBody());
            adView.setBodyView(bodyTV);

            if(nativeContentAd.getImages() != null && nativeContentAd.getImages().size() > 0) {
                adIV.setImageDrawable(nativeContentAd.getImages().get(0).getDrawable());
            } else {
                adIV.setImageDrawable(LolApplication.getAppContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setImageView(adIV);

            callToActionTV.setText(Utils.makeCamelCase(nativeContentAd.getCallToAction().toString()));
            adView.setCallToActionView(adContainerView);
            adView.setNativeAd(nativeContentAd);

            return adView;
        } catch (Exception e) {
            return null;
        }
    }

    public NativeAdView createSmallAdView() {
        if(nativeAd instanceof NativeAppInstallAd) {
            return createSmallNativeAppInstallAdView(getCachedAd());
        } else if(nativeAd instanceof NativeContentAd) {
            return createSmallNativeContentAdView(getCachedAd());
        }
        return null;
    }

    private NativeAppInstallAdView createSmallNativeAppInstallAdView(NativeAd ad) {
        try {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) ad;
            NativeAppInstallAdView adView = (NativeAppInstallAdView) mLayoutInflater.inflate(R.layout.list_row_small_appinstalladview, null, false);
            RelativeLayout adContainerView = (RelativeLayout) adView.findViewById(R.id.adContainerView);
            ImageView adIV = (ImageView) adView.findViewById(R.id.adImage);
            RobotoTextView headlineTV = (RobotoTextView) adView.findViewById(R.id.adHeadline);
            RobotoTextView bodyTV = (RobotoTextView) adView.findViewById(R.id.adBody);

            headlineTV.setText(nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(headlineTV);

            bodyTV.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(bodyTV);

            if(nativeAppInstallAd.getIcon() != null && nativeAppInstallAd.getIcon().getDrawable() != null) {
                adIV.setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());
            } else {
                adIV.setImageDrawable(LolApplication.getAppContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setIconView(adIV);

            adView.setCallToActionView(adContainerView);
            adView.setNativeAd(nativeAppInstallAd);

            return adView;
        } catch (Exception e) {
            return null;
        }
    }

    private NativeContentAdView createSmallNativeContentAdView(NativeAd ad) {
        try {
            NativeContentAd nativeContentAd = (NativeContentAd) ad;
            NativeContentAdView adView = (NativeContentAdView) mLayoutInflater.inflate(R.layout.list_row_small_contentadview, null, false);
            RelativeLayout adContainerView = (RelativeLayout) adView.findViewById(R.id.adContainerView);
            ImageView adIV = (ImageView) adView.findViewById(R.id.adImage);
            RobotoTextView headlineTV = (RobotoTextView) adView.findViewById(R.id.adHeadline);
            RobotoTextView bodyTV = (RobotoTextView) adView.findViewById(R.id.adBody);

            headlineTV.setText(nativeContentAd.getHeadline());
            adView.setHeadlineView(headlineTV);

            bodyTV.setText(nativeContentAd.getBody());
            adView.setBodyView(bodyTV);

            if(nativeContentAd.getLogo() != null && nativeContentAd.getLogo().getDrawable() != null) {
                adIV.setImageDrawable(nativeContentAd.getLogo().getDrawable());
            } else {
                adIV.setImageDrawable(LolApplication.getAppContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setLogoView(adIV);

            adView.setCallToActionView(adContainerView);
            adView.setNativeAd(nativeContentAd);

            return adView;
        } catch (Exception e) {
            return null;
        }
    }

}
