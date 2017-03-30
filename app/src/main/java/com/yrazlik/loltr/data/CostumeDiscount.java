package com.yrazlik.loltr.data;

import java.util.Date;

/**
 * Created by yrazlik on 30/03/17.
 */

public class CostumeDiscount {

    private String startDate;
    private String endDate;
    private String name;
    private String priceBeforeDiscount;
    private String priceAfterDiscount;
    private String imageUrl;
    private Date createdAt;

    public CostumeDiscount(String startDate, String endDate, String name, String priceBeforeDiscount, String priceAfterDiscount, String imageUrl, Date createdAt){
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
