package com.yrazlik.loltr.data;

import java.io.Serializable;

public class Champion implements Serializable{
	
	private String championImageUrl;
	private String championName;
	private String championRp;
	private String championIp;
	private String dateInterval;
	private boolean botMmEnabled;
    private int id;
    private String title;
	private String name;
    private boolean rankedPlayEnabled;
    private boolean botEnabled;
    private boolean active;
    private boolean freeToPlay;
    private String key;
    private long teamId;
    private int pickTurn;
    private long championId;
	
	public String getChampionImageUrl() {
		return championImageUrl;
	}
	public void setChampionImageUrl(String championImageUrl) {
		this.championImageUrl = championImageUrl;
	}
	public String getChampionName() {
		return championName;
	}
	public void setChampionName(String championName) {
		this.championName = championName;
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
	public String getDateInterval() {
		return dateInterval;
	}
	public void setDateInterval(String dateInterval) {
		this.dateInterval = dateInterval;
	}
	public boolean isBotMmEnabled() {
		return botMmEnabled;
	}
	public void setBotMmEnabled(boolean botMmEnabled) {
		this.botMmEnabled = botMmEnabled;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isRankedPlayEnabled() {
		return rankedPlayEnabled;
	}
	public void setRankedPlayEnabled(boolean rankedPlayEnabled) {
		this.rankedPlayEnabled = rankedPlayEnabled;
	}
	public boolean isBotEnabled() {
		return botEnabled;
	}
	public void setBotEnabled(boolean botEnabled) {
		this.botEnabled = botEnabled;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isFreeToPlay() {
		return freeToPlay;
	}
	public void setFreeToPlay(boolean freeToPlay) {
		this.freeToPlay = freeToPlay;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public int getPickTurn() {
        return pickTurn;
    }

    public void setPickTurn(int pickTurn) {
        this.pickTurn = pickTurn;
    }

    public long getChampionId() {
        return championId;
    }

    public void setChampionId(long championId) {
        this.championId = championId;
    }
}
