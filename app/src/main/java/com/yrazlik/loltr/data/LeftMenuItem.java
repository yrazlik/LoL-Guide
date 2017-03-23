package com.yrazlik.loltr.data;

/**
 * Created by yrazlik on 23/03/17.
 */

public class LeftMenuItem {

    private int image;
    private String title;

    public LeftMenuItem(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
