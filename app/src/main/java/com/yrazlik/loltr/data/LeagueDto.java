package com.yrazlik.loltr.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yrazlik on 3/13/15.
 */
public class LeagueDto implements Serializable{

    private static final long serialVersionUID = 11L;

    private String tier;
    private String name;
    private String queue;
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

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
