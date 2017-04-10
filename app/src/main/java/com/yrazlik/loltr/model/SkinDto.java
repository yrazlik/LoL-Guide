package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 27/03/17.
 */

public class SkinDto {

    @SerializedName("num")
    private int num;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
