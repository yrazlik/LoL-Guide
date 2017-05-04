package com.yrazlik.loltr.data;

import com.google.android.gms.ads.formats.NativeAd;

/**
 * Created by yrazlik on 2/1/15.
 */
public class Skin {

    private int id, num;
    private String name;

    private boolean isAd;
    private NativeAd nativeAd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
