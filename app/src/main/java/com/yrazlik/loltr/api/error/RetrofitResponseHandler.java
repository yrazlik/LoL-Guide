package com.yrazlik.loltr.api.error;

import com.google.gson.Gson;
import com.yrazlik.loltr.model.RiotApiHttpError;
import com.yrazlik.loltr.utils.Utils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yrazlik on 27/03/17.
 */

public class RetrofitResponseHandler implements IResponseHandler{

    private ApiResponseListener apiResponseListener;

    public RetrofitResponseHandler(ApiResponseListener apiResponseListener) {
        this.apiResponseListener = apiResponseListener;
    }

    private RetrofitResponseHandler() {}

    @Override
    public void handleSuccess(Call call, Response response) {
        if (apiResponseListener != null) {
            if(response != null) {
                if(response.code() > 200) {
                    RiotApiHttpError riotApiHttpError;
                    try {
                        riotApiHttpError = new Gson().fromJson(response.errorBody().string(), RiotApiHttpError.class);
                    } catch (Exception e) {
                        riotApiHttpError = null;
                    }

                    if(riotApiHttpError != null && riotApiHttpError.getStatus() != null) {
                        if(Utils.isValidString(riotApiHttpError.getStatus().getMessage())) {
                            apiResponseListener.onFailure(riotApiHttpError.getStatus().getMessage());
                        } else {
                            apiResponseListener.onUnknownError();
                        }
                    } else {
                        apiResponseListener.onUnknownError();
                    }
                } else {
                    if(response.body() != null) {
                        apiResponseListener.onResponse(call, response);
                    } else {
                        apiResponseListener.onUnknownError();
                    }
                }
            } else {
                apiResponseListener.onUnknownError();
            }
        }
    }

    @Override
    public void handleFailure(Call call, Throwable t, RetryHelper retryHelper) {
        if(apiResponseListener != null) {
            if(t instanceof IOException) {
                apiResponseListener.onNetworkError();
            } else {
                apiResponseListener.onFailure(call, t);
            }
        }
    }
}
