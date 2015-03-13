package com.yrazlik.loltr.data;

/**
 * Created by yrazlik on 3/13/15.
 */
public class PlayerStatsSummaryDto {

    private int wins;
    private int losses;
    private String playerStatSummaryType;

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public String getPlayerStatSummaryType() {
        return playerStatSummaryType;
    }

    public void setPlayerStatSummaryType(String playerStatSummaryType) {
        this.playerStatSummaryType = playerStatSummaryType;
    }
}
