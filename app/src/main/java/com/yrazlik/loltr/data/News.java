package com.yrazlik.loltr.data;

import java.util.Date;

/**
 * Created by yrazlik on 12/28/15.
 */
public class News {

    private String title;
    private String titleEnglish;
    private String message;
    private String messageEnglish;
    private String smallImage;
    private String largeImage;
    private String videoUrl;
    private String createdAt;

    public News(String title, String titleEnglish, String message, String messageEnglish, String smallImage, String largeImage, String videoUrl, String createdAt){
        this.title = title;
        this.titleEnglish = titleEnglish;
        this.message = message;
        this.messageEnglish = messageEnglish;
        this.smallImage = smallImage;
        this.largeImage = largeImage;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
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
