package com.yrazlik.loltr.data;

import com.google.android.gms.ads.formats.NativeAd;

/**
 * Created by yrazlik on 1/10/15.
 */
public class Streams {

    private String viewers;
    private Channel channel;

    private boolean isAd;
    private NativeAd nativeAd;

    public String getViewers() {
        return viewers;
    }

    public void setViewers(String viewers) {
        this.viewers = viewers;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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
