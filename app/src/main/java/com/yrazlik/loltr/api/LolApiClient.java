package com.yrazlik.loltr.api;

import com.yrazlik.lolquiz.BuildConfig;
import com.yrazlik.lolquiz.utils.LocalizationUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yrazlik on 27/03/17.
 */

public class LolApiClient {

    private static final String LOL_API_BASE_URL = "https://global.api.riotgames.com";
    public static final String CHAMP_DATA_ALL = "all";

    private static Retrofit retrofit = null;
    private static LolApiInterface mApiInterface = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.addInterceptor(apiKeyInterceptor);
            OkHttpClient httpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(LOL_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    //.addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static Interceptor apiKeyInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            HttpUrl url = chain
                    .request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("locale", LocalizationUtils.getInstance().getLocale())
                    .addQueryParameter("api_key", getApiKey()).build();

            Request.Builder requestBuilder = chain.request().newBuilder().url(url);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    };

    public static LolApiInterface getApiInterface() {
        if (mApiInterface == null) {
            mApiInterface = LolApiClient.getClient().create(LolApiInterface.class);
        }
        return mApiInterface;
    }

    public static String getApiKey() {
        return BuildConfig.API_KEY;
    }
}
