package com.CodeMonkey.saveme.Controller;

import android.os.Handler;

import com.CodeMonkey.saveme.Util.URLUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/***
 * RetrofitManager created by Wang Tianyu 02/03/2022
 * Singleton pattern for retrofit manager
 */
public class RetrofitManager {

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private volatile static RetrofitManager dbRetrofitManager;
    private volatile static RetrofitManager ltaRetrofitManager;

    private RetrofitManager(String requestPath) {

        okHttpClient = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(requestPath)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    public static RetrofitManager getDBRetrofitManager(){
        if (dbRetrofitManager == null){
            synchronized (RetrofitManager.class){
                if (dbRetrofitManager == null){
                    dbRetrofitManager = new RetrofitManager(URLUtil.dynamoDBAPIBase);
                }
            }
        }
        return dbRetrofitManager;
    }

    public static RetrofitManager getLTARetrofitManager(){
        if (ltaRetrofitManager == null){
            synchronized (RetrofitManager.class){
                if (ltaRetrofitManager == null){
                    ltaRetrofitManager = new RetrofitManager(URLUtil.LTABase);
                }
            }
        }
        return ltaRetrofitManager;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}

