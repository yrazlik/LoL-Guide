package com.yrazlik.loltr.data;

import java.io.Serializable;

/**
 * Created by yrazlik on 1/5/16.
 */
public class Player implements Serializable{

    private static final long serialVersionUID = 2L;


    private int championId;
    private long summonerId;
    private int teamId;

    public Player(int championId, long summonerId, int teamId){
        this.championId = championId;
        this.summonerId = summonerId;
        this.teamId = teamId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
