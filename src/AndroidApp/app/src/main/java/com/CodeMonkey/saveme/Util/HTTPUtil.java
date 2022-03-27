package com.CodeMonkey.saveme.Util;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/***
 * RegSignPage created by Wang Tianyu 10/02/2022
 * Util for HTTP request interface
 */
public interface HTTPUtil {

//    @GET(URLUtil.testAPI)
//    Observable<NewsRspAll> getNews();

    @GET(URLUtil.userData)
    Observable<String> getUserData();

    @Headers("AccountKey:"+ URLUtil.LTAKey)
    @GET(URLUtil.busArrival)
    Observable<ResponseBody> getBusArrivals();
}
