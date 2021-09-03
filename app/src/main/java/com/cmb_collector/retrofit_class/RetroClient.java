package com.cmb_collector.retrofit_class;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    public Retrofit retrofit;
    RetrofitListener retroListener;
    String method_name;
    Context mContext;
    String API_URL = "";

    public RetroClient(Context mContext, RetrofitListener retroListener) {
        this.mContext = mContext;
        this.retroListener = retroListener;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Search logcat with OkHttp: for raw response : addInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120000, TimeUnit.SECONDS)
                .connectTimeout(120000, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }


    /*public RetroClient(View.OnClickListener onClickListener, View.OnClickListener onClickListener1) {
    }*/


    public interface RestInterface {


        // @Multipart
        //  @POST(API.NEW_MEMBER_URL)
        // Call<Info_model> putInfoData(@Part("user_id") RequestBody user_id, @Part("req_name") RequestBody req_name, @Part("req_ps") RequestBody req_ps, @Part("req_dev") RequestBody req_dev, @Part("rank") RequestBody rank, @Part("imei") RequestBody imei, @Part("req_phone") RequestBody req_phone, @Part("accused_name") RequestBody accused_name, @Part("accused_phone") RequestBody accused_phone, @Part("other_info") RequestBody other_info, @Part MultipartBody.Part[] image);


        //   keys = new String[]{"case_yr", "total_img", "prov_crm_no","pic"};
//saveSpecialServiceDocument
    }

    public void makeHttpRequest(Call call, final String method) {
        this.method_name = method;

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                // do something with the response//
                Log.e("TAG", "onResponse :" + method_name);
                retroListener.onSuccess(call, response, method_name);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.request().body();
                if (t instanceof NoRouteToHostException) {
                    Log.e("TAG", "onFailure1 : server unreach");
                    retroListener.onFailure("Server Unreachable");
                } else if (t instanceof SocketTimeoutException) {
                    Log.e("TAG", "onFailure2 : time out");
                    retroListener.onFailure("Server Time Out");
                } else if (t instanceof IOException) {
                    Log.e("TAG", "onFailure3 : No internet");
                    retroListener.onFailure("No Internet connection");
                } else {
                    Log.e("TAG", "onFailure4 : " + t.getMessage());
                    retroListener.onFailure(t.getMessage());
                }
            }
        });
    }

}
