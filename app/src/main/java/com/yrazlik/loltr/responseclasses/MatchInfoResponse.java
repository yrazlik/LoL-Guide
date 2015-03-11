package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.Summoner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yrazlik on 3/5/15.
 */
public class MatchInfoResponse implements Serializable{

    private long gameLength;
    private String gameMode;
    private ArrayList<Champion> bannedChampions;
    private String gameType;
    private long gameId;
    private ArrayList<Summoner> participants;
    private String platformId;

    public long getGameLength() {
        return gameLength;
    }

    public void setGameLength(long gameLength) {
        this.gameLength = gameLength;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public ArrayList<Champion> getBannedChampions() {
        return bannedChampions;
    }

    public void setBannedChampions(ArrayList<Champion> bannedChampions) {
        this.bannedChampions = bannedChampions;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public ArrayList<Summoner> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Summoner> participants) {
        this.participants = participants;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}
