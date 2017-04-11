package com.yrazlik.loltr.api;

import com.yrazlik.loltr.BuildConfig;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.utils.LocalizationUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yrazlik on 11/04/17.
 */

public class RiotApiInterceptor implements Interceptor{

    private volatile String host;

    private void setHost(String host) {
        this.host = host;
    }

    public void setHost(LolApiClient.BASE_URL_TYPE baseUrlType) {
        setHost(getBaseUrl(baseUrlType));
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String host = this.host;
        if (host != null) {
            HttpUrl newUrl = request.url().newBuilder()
                    .host(host)
                    .addQueryParameter("locale", LocalizationUtils.getInstance().getLocale())
                    .addQueryParameter("api_key", BuildConfig.API_KEY).build();
            request = request.newBuilder()
                    .url(newUrl)
                    .build();
        }
        return chain.proceed(request);
    }

    private static String getBaseUrl(LolApiClient.BASE_URL_TYPE baseUrlType) {
        switch (baseUrlType) {
            case WEEKLY_FREE_CHAMPIONS:
                return Commons.SELECTED_REGION + ".api.riotgames.com";
            case STATIC_DATA:
                return "global.api.riotgames.com";
            default:
                return "global.api.riotgames.com";
        }
    }
}
