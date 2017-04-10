package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yrazlik on 27/03/17.
 */

public class SpellVarsDto {

    @SerializedName("ranksWith")
    private String ranksWith;
    @SerializedName("dyn")
    private String dyn;
    @SerializedName("link")
    private String link;
    @SerializedName("coeff")
    private List<Double> coeff;
    @SerializedName("key")
    private String key;

    public String getRanksWith() {
        return ranksWith;
    }

    public void setRanksWith(String ranksWith) {
        this.ranksWith = ranksWith;
    }

    public String getDyn() {
        return dyn;
    }

    public void setDyn(String dyn) {
        this.dyn = dyn;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Double> getCoeff() {
        return coeff;
    }

    public void setCoeff(List<Double> coeff) {
        this.coeff = coeff;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
