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
    private volatile static RetrofitManager govRetrofitManager;
    private volatile static RetrofitManager dbRetrofitManager;
    private volatile static RetrofitManager s3RetrofitManager;

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

    public static RetrofitManager gets3RetrofitManager(){
        if (s3RetrofitManager == null){
            synchronized (RetrofitManager.class){
                if (s3RetrofitManager == null){
                    s3RetrofitManager = new RetrofitManager(URLUtil.awsS3Base);
                }
            }
        }
        return s3RetrofitManager;
    }

    public static RetrofitManager getGovRetrofitManager(){
        if (govRetrofitManager == null){
            synchronized (RetrofitManager.class){
                if (govRetrofitManager == null){
                    govRetrofitManager = new RetrofitManager(URLUtil.dataGovSG);
                }
            }
        }
        return govRetrofitManager;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}

