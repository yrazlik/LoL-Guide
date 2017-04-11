package com.yrazlik.loltr.api.error;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yrazlik on 27/03/17.
 */

public interface ApiResponseListener {
    void onResponseFromCache(Object response);
    void onResponse(Call call, Response response);
    void onUnknownError();
    void onFailure(Call call, Throwable t);
    void onFailure(String errorMessage);
    void onNetworkError();
}