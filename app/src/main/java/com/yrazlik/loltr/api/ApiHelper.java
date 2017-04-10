package com.yrazlik.loltr.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yrazlik.lolquiz.api.error.IResponseHandler;
import com.yrazlik.lolquiz.api.error.RetryHelper;
import com.yrazlik.lolquiz.model.ChampionListDto;
import com.yrazlik.lolquiz.utils.LocalizationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yrazlik on 27/03/17.
 */

public class ApiHelper {

    private Context mContext;

    private static ApiHelper mInstance;

    public static ApiHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiHelper();
        }
        mInstance.setContext(context);
        return mInstance;
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private ApiHelper() {

    }

    private boolean isNetworkAvailable() {
        if(mContext != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return true;
    }

    private void onSuccessResponse(IResponseHandler retrofitResponseHandler, Call call, Response response) {
        if(retrofitResponseHandler != null) {
            retrofitResponseHandler.handleSuccess(call, response);
        }
    }

    private void onFailResponse(final IResponseHandler retrofitResponseHandler, final Call call, Throwable t) {
        if(retrofitResponseHandler != null) {
            retrofitResponseHandler.handleFailure(call, t, new RetryHelper() {
                @Override
                public void retry() {
                    if(isNetworkAvailable()) {
                        call.clone().enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                onSuccessResponse(retrofitResponseHandler, call, response);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                onFailResponse(retrofitResponseHandler, call, t);
                            }
                        });
                    }
                }
            });
        }
    }

    public void getAllChampions(final IResponseHandler retrofitResponseHandler) {
        Call<ChampionListDto> call = LolApiClient.getApiInterface().getAllChampions(LocalizationUtils.getInstance().getRegion(), LolApiClient.CHAMP_DATA_ALL);
        call.enqueue(new Callback<ChampionListDto>() {
            @Override
            public void onResponse(Call<ChampionListDto> call, Response<ChampionListDto> response) {
                onSuccessResponse(retrofitResponseHandler, call, response);
            }

            @Override
            public void onFailure(Call<ChampionListDto> call, Throwable t) {
                onFailResponse(retrofitResponseHandler, call, t);
            }
        });
    }
}
