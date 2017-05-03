package com.yrazlik.loltr.model;

import android.graphics.drawable.Drawable;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yrazlik on 27/03/17.
 */

public class ChampionDto {

    @SerializedName("info")
    private InfoDto info;
    @SerializedName("enemytips")
    private List<String> enemytips;
    @SerializedName("stats")
    private StatsDto stats;
    @SerializedName("name")
    private String name;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("image")
    private ImageDto image = new ImageDto();
    @SerializedName("title")
    private String title;
    @SerializedName("partype")
    private String partype;
    @SerializedName("skins")
    private List<SkinDto> skins;
    @SerializedName("passive")
    private PassiveDto passive;
    @SerializedName("recommended")
    private List<RecommendedDto> recommended;
    @SerializedName("allytips")
    private List<String> allytips;
    @SerializedName("key")
    private String key;
    @SerializedName("lore")
    private String lore;
    @SerializedName("id")
    private int id;
    @SerializedName("blurb")
    private String blurb;
    @SerializedName("spells")
    private List<ChampionSpellDto> spells;
    @SerializedName("freeToPlay")
    private boolean freeToPlay;
    @SerializedName("active")
    private boolean active;
    @SerializedName("botEnabled")
    private boolean botEnabled;
    @SerializedName("botMmEnabled")
    private boolean botMmEnabled;
    @SerializedName("rankedPlayEnabled")
    private boolean rankedPlayEnabled;

    private NativeAd nativeAd;
    private boolean isAd;

    private String dateInterval;
    private String championRp;
    private String championIp;

    public ChampionDto() {
    }

    public ChampionDto(int champId, String key, String name, String imageUrl, String title) {
        this.id = champId;
        this.key = key;
        this.name = name;
        ImageDto imageDto = new ImageDto();
        imageDto.setFull(imageUrl);
        this.image = imageDto;
        this.title = title;
    }

    public ChampionDto(int champId, String name, String dateInterval, String imageUrl, String ipPrice, String rpPrice) {
        this.id = champId;
        this.name = name;
        this.dateInterval = dateInterval;
        this.championIp = ipPrice;
        this.championRp = rpPrice;
        this.image = new ImageDto();
        this.image.setFull(imageUrl);
    }


    public InfoDto getInfo() {
        return info;
    }

    public void setInfo(InfoDto info) {
        this.info = info;
    }

    public List<String> getEnemytips() {
        return enemytips;
    }

    public void setEnemytips(List<String> enemytips) {
        this.enemytips = enemytips;
    }

    public StatsDto getStats() {
        return stats;
    }

    public void setStats(StatsDto stats) {
        this.stats = stats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPartype() {
        return partype;
    }

    public void setPartype(String partype) {
        this.partype = partype;
    }

    public List<SkinDto> getSkins() {
        return skins;
    }

    public void setSkins(List<SkinDto> skins) {
        this.skins = skins;
    }

    public PassiveDto getPassive() {
        return passive;
    }

    public void setPassive(PassiveDto passive) {
        this.passive = passive;
    }

    public List<RecommendedDto> getRecommended() {
        return recommended;
    }

    public void setRecommended(List<RecommendedDto> recommended) {
        this.recommended = recommended;
    }

    public List<String> getAllytips() {
        return allytips;
    }

    public void setAllytips(List<String> allytips) {
        this.allytips = allytips;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public List<ChampionSpellDto> getSpells() {
        return spells;
    }

    public void setSpells(List<ChampionSpellDto> spells) {
        this.spells = spells;
    }

    public boolean isFreeToPlay() {
        return freeToPlay;
    }

    public void setFreeToPlay(boolean freeToPlay) {
        this.freeToPlay = freeToPlay;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isBotEnabled() {
        return botEnabled;
    }

    public void setBotEnabled(boolean botEnabled) {
        this.botEnabled = botEnabled;
    }

    public boolean isBotMmEnabled() {
        return botMmEnabled;
    }

    public void setBotMmEnabled(boolean botMmEnabled) {
        this.botMmEnabled = botMmEnabled;
    }

    public boolean isRankedPlayEnabled() {
        return rankedPlayEnabled;
    }

    public void setRankedPlayEnabled(boolean rankedPlayEnabled) {
        this.rankedPlayEnabled = rankedPlayEnabled;
    }

    public String getDateInterval() {
        return dateInterval;
    }

    public void setDateInterval(String dateInterval) {
        this.dateInterval = dateInterval;
    }

    public String getChampionRp() {
        return championRp;
    }

    public void setChampionRp(String championRp) {
        this.championRp = championRp;
    }

    public String getChampionIp() {
        return championIp;
    }

    public void setChampionIp(String championIp) {
        this.championIp = championIp;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }
}
