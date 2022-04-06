package com.CodeMonkey.saveme.Util;

import com.CodeMonkey.saveme.Controller.RetrofitManager;
import com.CodeMonkey.saveme.Entity.CertificateRsp;
import com.CodeMonkey.saveme.Entity.CertificateURLScheme;
import com.CodeMonkey.saveme.Entity.GovDataRsp;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.Entity.UserRsp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Completable;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * RequestUtil created by Wang Tianyu 10/02/2022
 * Util for calling API and sending HTTP request
 */
public class RequestUtil {

    private static HTTPUtil govService = RetrofitManager.getGovRetrofitManager().create(HTTPUtil.class);
    private static HTTPUtil dbService = RetrofitManager.getDBRetrofitManager().create(HTTPUtil.class);
    private static HTTPUtil ltaService = RetrofitManager.getLTARetrofitManager().create(HTTPUtil.class);
    private static HTTPUtil s3Service = RetrofitManager.gets3RetrofitManager().create(HTTPUtil.class);

    public static void getHumidity(Observer<GovDataRsp> observer, String dataTime){
        setSubscribe(govService.getHumidity(dataTime), observer);
    }

    public static void getTemperature(Observer<GovDataRsp> observer, String dataTime){
        setSubscribe(govService.getTemperature(dataTime), observer);
    }

    public static void postUserData(Observer<User> observer, User user, String token){
        setSubscribe(dbService.postUserData(user, token), observer);
    }

    public static void putUserData(Observer<User> observer, User user, String token){
        setSubscribe(dbService.putUserData(user, token), observer);
    }

    public static void getUserData(Observer<UserRsp> observer, String phoneNumber, String token){
        setSubscribe(dbService.getUserData(phoneNumber, token), observer);
    }

    public static void postCertData(Observer<CertificateRsp> observer, CertificateURLScheme certificateURLScheme, String token){
        setSubscribe(dbService.postCertData(certificateURLScheme, token), observer);
    }

    public static void getCertData(Observer<ResponseBody> observer, String url){
        setSubscribe(s3Service.getCertData(url), observer);
    }

    public static void getBusDataAll(Observer<ResponseBody> observer){
        setSubscribe(ltaService.getBusArrivals("83139"), observer);
    }

    public static void checkValidation(Observer<ResponseBody> observer, String phoneNumber, String token){
        setSubscribe(dbService.checkAvailability(phoneNumber, token), observer);
    }

    public static void postRealCertData(Observer<Completable> observer, MultipartBody.Part key,
                                        MultipartBody.Part token, MultipartBody.Part accessKey,
                                        MultipartBody.Part policy, MultipartBody.Part signature, RequestBody file){
        setSubscribe(s3Service.postRealCertData(key, token, accessKey, policy, signature, file), observer);
    }


    private static <T> void setSubscribe(Observable<T> observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
