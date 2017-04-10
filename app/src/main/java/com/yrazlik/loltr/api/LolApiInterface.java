package com.yrazlik.loltr.api;

import com.yrazlik.lolquiz.model.ChampionListDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by yrazlik on 27/03/17.
 */

public interface LolApiInterface {

    @GET("/api/lol/static-data/{region}/v1.2/champion")
    Call<ChampionListDto> getAllChampions(@Path("region") String region,
                                          @Query("champData") String champData);

    @GET("/api/lol/static-data/{region}/v1.2/summoner-spell")
    Call<ChampionListDto> getSummonerSpells(@Path("region") String region,
                                            @Query("champData") String champData);

}
