package com.yrazlik.loltr.data;

import com.google.android.gms.ads.formats.NativeAd;

import java.util.Date;

/**
 * Created by yrazlik on 12/25/15.
 */
public class ChampionDiscount {

    private String startDate;
    private String endDate;
    private String name;
    private String priceBeforeDiscount;
    private String priceAfterDiscount;
    private String imageUrl;
    private long championId;
    private Date createdAt;

    private boolean isAd;
    private NativeAd nativeAd;

    public ChampionDiscount(String startDate, String endDate, String name, String priceBeforeDiscount, String priceAfterDiscount, String imageUrl, long champId, Date createdAt){
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.imageUrl = imageUrl;
        this.championId = champId;
        this.createdAt = createdAt;
    }

    public ChampionDiscount() {

    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(String priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public void setPriceBeforeDiscount(String priceBeforeDiscount) {
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public long getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
