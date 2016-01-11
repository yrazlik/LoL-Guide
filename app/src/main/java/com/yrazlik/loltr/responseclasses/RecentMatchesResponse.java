package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.data.Game;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yrazlik on 1/5/16.
 */
public class RecentMatchesResponse implements Serializable{

    private static final long serialVersionUID = 7L;

    private List<Game> games;
    private long summonerId;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
