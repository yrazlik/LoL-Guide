package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 27/03/17.
 */

public class ImageDto {

    @SerializedName("full")
    private String full;
    @SerializedName("group")
    private String group;
    @SerializedName("sprite")
    private String sprite;
    @SerializedName("h")
    private int h;
    @SerializedName("w")
    private int w;
    @SerializedName("y")
    private int y;
    @SerializedName("x")
    private int x;

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
