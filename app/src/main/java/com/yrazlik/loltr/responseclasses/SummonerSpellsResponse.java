package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.data.SummonerSpell;

import java.util.ArrayList;

/**
 * Created by yrazlik on 1/6/16.
 */
public class SummonerSpellsResponse {

    private String type;
    private String version;
    private ArrayList<SummonerSpell> spells;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<SummonerSpell> getSpells() {
        return spells;
    }

    public void setSpells(ArrayList<SummonerSpell> spells) {
        this.spells = spells;
    }
}
