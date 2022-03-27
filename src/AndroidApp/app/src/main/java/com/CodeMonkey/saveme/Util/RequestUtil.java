package com.CodeMonkey.saveme.Util;

import com.CodeMonkey.saveme.Controller.RetrofitManager;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * RequestUtil created by Wang Tianyu 10/02/2022
 * Util for calling API and sending HTTP request
 */
public class RequestUtil {

    private static HTTPUtil service = RetrofitManager.getInstance().create(HTTPUtil.class);

//    public static void getNews(Observer<NewsRspAll> observer) {
//        setSubscribe(service.getNews(), observer);
//    }

    public static void getUserData(Observer<String> observer){
        setSubscribe(service.getUserData(), observer);
    }

    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
