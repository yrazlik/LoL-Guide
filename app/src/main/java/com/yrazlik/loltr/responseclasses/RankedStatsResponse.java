package com.yrazlik.loltr.responseclasses;

import com.yrazlik.loltr.data.ChampionStatsDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yrazlik on 1/11/16.
 */
public class RankedStatsResponse  implements Serializable{

    private static final long serialVersionUID = 8L;


    private List<ChampionStatsDto> champions;
    private long modifyDate;
    private long summonerId;

    public List<ChampionStatsDto> getChampions() {
        return champions;
    }

    public void setChampions(List<ChampionStatsDto> champions) {
        this.champions = champions;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
