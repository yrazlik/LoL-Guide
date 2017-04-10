package com.yrazlik.loltr.api;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yrazlik on 27/03/17.
 */

public class LolCallback<T> implements Callback<T>{


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.d("TAG", "LolCallback response");
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d("TAG", "LolCallback failure");
    }
}
