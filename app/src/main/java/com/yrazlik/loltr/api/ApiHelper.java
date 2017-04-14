package com.yrazlik.loltr.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.yrazlik.loltr.api.error.IResponseHandler;
import com.yrazlik.loltr.api.error.RetryHelper;
import com.yrazlik.loltr.db.DbHelper;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;
import com.yrazlik.loltr.model.WeeklyFreeResponseDto;
import com.yrazlik.loltr.utils.CacheUtils;
import com.yrazlik.loltr.utils.LocalizationUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yrazlik on 27/03/17.
 */

public class ApiHelper {

    public enum REQUEST_TYPE {WEEKLY_FREE_CHAMPIONS_REQUEST,
        STATIC_DATA_WITH_ALT_IMAGES_REQUEST, CHAMPION_OVERVIEW_REQUEST, }

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

    public void getWeeklyFreeChampions(final IResponseHandler retrofitResponseHandler) {
        List<ChampionDto> weeklyFreeChampionsData = DbHelper.getInstance().getWeeklyFreeChampionsData();
        if(weeklyFreeChampionsData != null && weeklyFreeChampionsData.size() > 0) {
            retrofitResponseHandler.onResponseFromCache(weeklyFreeChampionsData);
        } else {
            Call<WeeklyFreeResponseDto> call = LolApiClient.getApiInterface(LolApiClient.BASE_URL_TYPE.WEEKLY_FREE_CHAMPIONS).getWeeklyFreeChampions(LocalizationUtils.getInstance().getRegion(), true);
            call.enqueue(new Callback<WeeklyFreeResponseDto>() {
                @Override
                public void onResponse(Call<WeeklyFreeResponseDto> call, Response<WeeklyFreeResponseDto> response) {
                    onSuccessResponse(retrofitResponseHandler, call, response);
                }

                @Override
                public void onFailure(Call<WeeklyFreeResponseDto> call, Throwable t) {
                    onFailResponse(retrofitResponseHandler, call, t);
                }
            });
        }
    }

    public void getAllChampions(final IResponseHandler retrofitResponseHandler) {
        List<ChampionDto> allChampions = DbHelper.getInstance().getAllChampionsData();
        if(allChampions != null) {
            retrofitResponseHandler.onResponseFromCache(allChampions);
        } else {
            Call<ChampionListDto> call = LolApiClient.getApiInterface(LolApiClient.BASE_URL_TYPE.STATIC_DATA).getAllChampions(LocalizationUtils.getInstance().getRegion(), LolApiClient.CHAMP_DATA_ALTIMAGES);
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
}
