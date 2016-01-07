package com.yrazlik.loltr.data;

import java.io.Serializable;

/**
 * Created by yrazlik on 1/7/16.
 */
public class RecentSearchItem implements Serializable{

    private static final long serialVersionUID = 5L;


    private String name;
    private long id;
    private int profileIconId;
    private String region;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
