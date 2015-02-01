package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.data.Skin;

import java.util.ArrayList;

/**
 * Created by yrazlik on 2/1/15.
 */
public class ChampionSkinsResponse {

    public ArrayList<Skin> skins;
    public String key;

    public ArrayList<Skin> getSkins() {
        return skins;
    }

    public void setSkins(ArrayList<Skin> skins) {
        this.skins = skins;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
