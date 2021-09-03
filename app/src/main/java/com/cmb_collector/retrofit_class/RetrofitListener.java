package com.cmb_collector.retrofit_class;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by DAT-165 on 17-10-2017.
 */

public interface RetrofitListener {
    void onSuccess(Call call, Response response, String method);

    void onFailure(String errorMessage);
}
