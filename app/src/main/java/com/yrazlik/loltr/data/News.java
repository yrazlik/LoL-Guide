package com.yrazlik.loltr.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Created by yrazlik on 12/28/15.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class News {

    private String url;
    private String title;
    private String shortDesc;
    private String img;
    private Date createdAt;

    public News(String url, String title, String shortDesc, String img, Date createdAt){
        this.url = url;
        this.title = title;
        this.shortDesc = shortDesc;
        this.img = img;
        this.createdAt = createdAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
