package com.CodeMonkey.saveme.Util;

import com.CodeMonkey.saveme.Controller.RetrofitManager;
import com.CodeMonkey.saveme.Entity.Certificate;
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

    private static HTTPUtil service = RetrofitManager.getRetrofitManager().create(HTTPUtil.class);


    public static void postUserData(Observer<User> observer, User user){
        setSubscribe(service.postUserData(user), observer);
    }

    public static void putUserData(Observer<User> observer, User user){
        setSubscribe(service.putUserData(user), observer);
    }

    public static void getUserData(Observer<UserRsp> observer, String phoneNumber){
        setSubscribe(service.getUserData(phoneNumber), observer);
    }

    public static void postCertData(Observer<ResponseBody> observer, Certificate certificate){
        setSubscribe(service.postCertData(certificate), observer);
    }

    public static void getCertData(Observer<String> observer){
        setSubscribe(service.getCertData(), observer);
    }

    private static <T> void setSubscribe(Observable<T> observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
