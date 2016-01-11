package com.yrazlik.loltr.data;

/**
 * Created by yrazlik on 1/11/16.
 */
public class ChampGameAnalysis {

    private int champId;
    private int winCount, loseCount, killCount, deathCount, assistCount, totalPlayCount = winCount + loseCount;

    public int getAssistCount() {
        return assistCount;
    }

    public void setAssistCount(int assistCount) {
        this.assistCount = assistCount;
    }

    public int getChampId() {
        return champId;
    }

    public void setChampId(int champId) {
        this.champId = champId;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public int getTotalPlayCount() {
        return totalPlayCount;
    }

    public void setTotalPlayCount(int totalPlayCount) {
        this.totalPlayCount = totalPlayCount;
    }
}
