package com.yrazlik.loltr.data;

import java.util.ArrayList;

/**
 * Created by yrazlik on 3/13/15.
 */
public class LeagueDto {

    private String tier;
    private String name;
    private String participantId;
    private ArrayList<Entries> entries;

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public ArrayList<Entries> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entries> entries) {
        this.entries = entries;
    }
}
