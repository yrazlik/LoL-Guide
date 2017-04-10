package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yrazlik on 27/03/17.
 */

public class BlockDto{

    @SerializedName("items")
    private java.util.List<BlockItemDto> items;
    @SerializedName("recMath")
    private boolean recMath;
    @SerializedName("type")
    private String type;

    public List<BlockItemDto> getItems() {
        return items;
    }

    public void setItems(List<BlockItemDto> items) {
        this.items = items;
    }

    public boolean isRecMath() {
        return recMath;
    }

    public void setRecMath(boolean recMath) {
        this.recMath = recMath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
