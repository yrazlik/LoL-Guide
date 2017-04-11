package com.yrazlik.loltr.api.error;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yrazlik on 27/03/17.
 */

public interface IResponseHandler {
    void onResponseFromCache(Object response);
    void handleSuccess(Call call, Response response);
    void handleFailure(Call call, Throwable t, RetryHelper retryHelper);
}
