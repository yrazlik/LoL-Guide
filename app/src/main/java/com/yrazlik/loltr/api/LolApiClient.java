package com.yrazlik.loltr.api;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yrazlik on 27/03/17.
 */

public class LolApiClient {

    public enum BASE_URL_TYPE {WEEKLY_FREE_CHAMPIONS, STATIC_DATA}

    public static final String CHAMP_DATA_ALL = "all";
    public static final String CHAMP_DATA_ALTIMAGES = "altimages";
    public static final String CHAMP_DATA_INFO_TAGS = "info,tags";

    private static String apiBaseUrl = "https://tr.api.pvp.net";
    private static Retrofit retrofit = null;
    private static LolApiInterface mApiInterface = null;
    private static OkHttpClient.Builder builder;
    private static OkHttpClient httpClient;
    private static RiotApiInterceptor riotApiInterceptor;

    public static Retrofit getClient() {
        if (retrofit == null) {
            riotApiInterceptor = new RiotApiInterceptor();
            builder = new OkHttpClient.Builder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.addInterceptor(riotApiInterceptor);
            httpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    public static LolApiInterface getApiInterface(BASE_URL_TYPE baseUrlType) {
        if (mApiInterface == null) {
            mApiInterface = LolApiClient.getClient().create(LolApiInterface.class);
        }
        riotApiInterceptor.setHost(baseUrlType);
        return mApiInterface;
    }
}
