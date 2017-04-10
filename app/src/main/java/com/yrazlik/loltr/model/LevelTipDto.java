package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yrazlik on 27/03/17.
 */

public class LevelTipDto {

    @SerializedName("effect")
    private List<String> effect;
    @SerializedName("label")
    private List<String> label;

    public List<String> getEffect() {
        return effect;
    }

    public void setEffect(List<String> effect) {
        this.effect = effect;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }
}
