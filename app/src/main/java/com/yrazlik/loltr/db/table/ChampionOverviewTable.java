package com.yrazlik.loltr.db.table;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.SerializedName;
import com.yrazlik.loltr.data.Info;
import com.yrazlik.loltr.model.InfoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrazlik on 14/04/17.
 */

public class ChampionOverviewTable extends Model{

    @Column(name = "champId", unique = true)
    private int champId;
    @Column(name = "title")
    private String title;
    @Column(name = "name")
    private String name;
    @Column(name = "key")
    private String key;
    @Column(name = "info")
    private InfoDto info;
    @Column(name = "tags")
    private List<String> tags;
    @Column(name = "version")
    private String version;

    public ChampionOverviewTable() {
    }

    public ChampionOverviewTable(int champId, String title, String name, String key, InfoDto info, List<String> tags, String version) {
        this.champId = champId;
        this.title = title;
        this.name = name;
        this.key = key;
        this.info = info;
        this.tags = tags;
        this.version = version;
    }

    public int getChampId() {
        return champId;
    }

    public void setChampId(int champId) {
        this.champId = champId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public InfoDto getInfo() {
        return info;
    }

    public void setInfo(InfoDto info) {
        this.info = info;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
