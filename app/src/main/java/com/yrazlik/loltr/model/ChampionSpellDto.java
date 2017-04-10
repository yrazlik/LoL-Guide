package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yrazlik on 27/03/17.
 */

public class ChampionSpellDto {

    @SerializedName("cooldownBurn")
    private String cooldownBurn;
    @SerializedName("resource")
    private String resource;
    @SerializedName("leveltip")
    private LevelTipDto leveltip;
    @SerializedName("vars")
    private List<SpellVarsDto> vars;
    @SerializedName("costType")
    private String costType;
    @SerializedName("altimages")
    private List<ImageDto> altimages;
    @SerializedName("sanitizedDescription")
    private String sanitizedDescription;
    @SerializedName("sanitizedTooltip")
    private String sanitizedTooltip;
    @SerializedName("effect")
    private Object effect;
    @SerializedName("tooltip")
    private String tooltip;
    @SerializedName("maxrank")
    private int maxrank;
    @SerializedName("costBurn")
    private String costBurn;
    @SerializedName("rangeBurn")
    private String rangeBurn;
    @SerializedName("range")
    private Object range;
    @SerializedName("cooldown")
    private List<Double> cooldown;
    @SerializedName("cost")
    private List<Integer> cost;
    @SerializedName("key")
    private String key;
    @SerializedName("description")
    private String description;
    @SerializedName("effectBurn")
    private List<String> effectBurn;
    @SerializedName("image")
    private ImageDto image;
    @SerializedName("name")
    private String name;

    public String getCooldownBurn() {
        return cooldownBurn;
    }

    public void setCooldownBurn(String cooldownBurn) {
        this.cooldownBurn = cooldownBurn;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public LevelTipDto getLeveltip() {
        return leveltip;
    }

    public void setLeveltip(LevelTipDto leveltip) {
        this.leveltip = leveltip;
    }

    public List<SpellVarsDto> getVars() {
        return vars;
    }

    public void setVars(List<SpellVarsDto> vars) {
        this.vars = vars;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public List<ImageDto> getAltimages() {
        return altimages;
    }

    public void setAltimages(List<ImageDto> altimages) {
        this.altimages = altimages;
    }

    public String getSanitizedDescription() {
        return sanitizedDescription;
    }

    public void setSanitizedDescription(String sanitizedDescription) {
        this.sanitizedDescription = sanitizedDescription;
    }

    public String getSanitizedTooltip() {
        return sanitizedTooltip;
    }

    public void setSanitizedTooltip(String sanitizedTooltip) {
        this.sanitizedTooltip = sanitizedTooltip;
    }

    public Object getEffect() {
        return effect;
    }

    public void setEffect(Object effect) {
        this.effect = effect;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public int getMaxrank() {
        return maxrank;
    }

    public void setMaxrank(int maxrank) {
        this.maxrank = maxrank;
    }

    public String getCostBurn() {
        return costBurn;
    }

    public void setCostBurn(String costBurn) {
        this.costBurn = costBurn;
    }

    public String getRangeBurn() {
        return rangeBurn;
    }

    public void setRangeBurn(String rangeBurn) {
        this.rangeBurn = rangeBurn;
    }

    public Object getRange() {
        return range;
    }

    public void setRange(Object range) {
        this.range = range;
    }

    public List<Double> getCooldown() {
        return cooldown;
    }

    public void setCooldown(List<Double> cooldown) {
        this.cooldown = cooldown;
    }

    public List<Integer> getCost() {
        return cost;
    }

    public void setCost(List<Integer> cost) {
        this.cost = cost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getEffectBurn() {
        return effectBurn;
    }

    public void setEffectBurn(List<String> effectBurn) {
        this.effectBurn = effectBurn;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
