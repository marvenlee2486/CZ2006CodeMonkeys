package com.CodeMonkey.saveme.Util;

import com.CodeMonkey.saveme.Controller.RetrofitManager;
import com.CodeMonkey.saveme.Entity.Certificate;
import com.CodeMonkey.saveme.Entity.CertificateRsp;
import com.CodeMonkey.saveme.Entity.CertificateURLScheme;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.Entity.UserRsp;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * RequestUtil created by Wang Tianyu 10/02/2022
 * Util for calling API and sending HTTP request
 */
public class RequestUtil {

    private static HTTPUtil dbService = RetrofitManager.getDBRetrofitManager().create(HTTPUtil.class);
    private static HTTPUtil ltaService = RetrofitManager.getLTARetrofitManager().create(HTTPUtil.class);
    private static HTTPUtil s3Service = RetrofitManager.gets3RetrofitManager().create(HTTPUtil.class);


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

    public static void getCertData(Observer<String> observer, String phoneNumber){
        setSubscribe(dbService.getCertData(phoneNumber), observer);
    }

    public static void getBusDataAll(Observer<ResponseBody> observer){
        setSubscribe(ltaService.getBusArrivals("83139"), observer);
    }

    public static void checkValidation(Observer<ResponseBody> observer, String phoneNumber, String token){
        setSubscribe(dbService.checkAvailability(phoneNumber, token), observer);
    }

    public static void postRealCertData(Observer<ResponseBody> observer, Certificate certificate){
        setSubscribe(s3Service.postRealCertData(certificate), observer);
    }


    private static <T> void setSubscribe(Observable<T> observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
