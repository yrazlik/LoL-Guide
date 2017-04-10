package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yrazlik on 27/03/17.
 */

public class RecommendedDto {

    @SerializedName("map")
    private String map;
    @SerializedName("blocks")
    private List<BlockDto> blocks;
    @SerializedName("champion")
    private String champion;
    @SerializedName("title")
    private String title;
    @SerializedName("priority")
    private boolean priority;
    @SerializedName("mode")
    private String mode;
    @SerializedName("type")
    private String type;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public List<BlockDto> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockDto> blocks) {
        this.blocks = blocks;
    }

    public String getChampion() {
        return champion;
    }

    public void setChampion(String champion) {
        this.champion = champion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
