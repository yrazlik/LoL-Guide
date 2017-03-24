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
    private String titleEnglish;
    private String message;
    private String messageEnglish;
    private String smallImage;
    private String largeImage;
    private String videoUrl;
    private Date createdAt;

    public News(String url, String title, String titleEnglish, String message, String messageEnglish, String smallImage, String largeImage, String videoUrl, Date createdAt){
        this.url = url;
        this.title = title;
        this.titleEnglish = titleEnglish;
        this.message = message;
        this.messageEnglish = messageEnglish;
        this.smallImage = smallImage;
        this.largeImage = largeImage;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessageEnglish() {
        return messageEnglish;
    }

    public void setMessageEnglish(String messageEnglish) {
        this.messageEnglish = messageEnglish;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }
}
