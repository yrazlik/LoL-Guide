package com.yrazlik.loltr.data;

import java.util.Date;

/**
 * Created by yrazlik on 12/25/15.
 */
public class Discount {

    private String discountType;
    private String startDate;
    private String endDate;
    private String name;
    private String nameEnglish;
    private String priceBeforeDiscount;
    private String priceAfterDiscount;
    private String imageUrl;
    private Date createdAt;

    public Discount(String discountType, String startDate, String endDate, String name, String nameEnglish, String priceBeforeDiscount, String priceAfterDiscount, String imageUrl, Date createdAt){
        this.discountType = discountType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.nameEnglish = nameEnglish;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
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

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
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
