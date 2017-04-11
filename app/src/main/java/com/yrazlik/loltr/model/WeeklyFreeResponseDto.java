package com.yrazlik.loltr.model;

import java.util.List;

/**
 * Created by yrazlik on 11/04/17.
 */

public class WeeklyFreeResponseDto {
    private List<ChampionDto> champions;

    public List<ChampionDto> getChampions() {
        return champions;
    }

    public void setChampions(List<ChampionDto> champions) {
        this.champions = champions;
    }
}
