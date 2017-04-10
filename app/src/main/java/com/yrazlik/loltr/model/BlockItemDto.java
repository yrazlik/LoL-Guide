package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 27/03/17.
 */

public class BlockItemDto {

    @SerializedName("count")
    private int count;
    @SerializedName("id")
    private int id;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
