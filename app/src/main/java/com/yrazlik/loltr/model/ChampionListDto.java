package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by yrazlik on 27/03/17.
 */

public class ChampionListDto {

    @SerializedName("keys")
    private Map<String, String> keys;
    @SerializedName("data")
    private Map<String, ChampionDto> data;
    @SerializedName("version")
    private String version;
    @SerializedName("type")
    private String type;
    @SerializedName("format")
    private String format;

    public Map<String, String> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, String> keys) {
        this.keys = keys;
    }

    public Map<String, ChampionDto> getData() {
        return data;
    }

    public void setData(Map<String, ChampionDto> data) {
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
