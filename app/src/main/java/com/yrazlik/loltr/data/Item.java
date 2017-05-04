package com.yrazlik.loltr.data;

import com.google.android.gms.ads.formats.NativeAd;

/**
 * Created by yrazlik on 8/6/15.
 */
public class Item {

    private Data data;
    private boolean isAd;
    private NativeAd nativeAd;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }
}
