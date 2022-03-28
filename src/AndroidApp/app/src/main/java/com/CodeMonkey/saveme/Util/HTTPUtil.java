package com.CodeMonkey.saveme.Util;


import com.CodeMonkey.saveme.Entity.Certificate;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.Entity.UserRsp;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/***
 * HTTPUtil created by Wang Tianyu 10/02/2022
 * Util for HTTP request interface
 */
public interface HTTPUtil {


    @GET(URLUtil.userData)
    Observable<UserRsp> getUserData(@Query("phoneNumber") String phoneNumber);

    @POST(URLUtil.userData)
    Observable<User> postUserData(@Body User user);

    @PUT(URLUtil.userData)
    Observable<User> putUserData(@Body User user);

    @POST(URLUtil.certData)
    Observable<String> getCertData();

    @Headers("Accept: application/json")
    @POST(URLUtil.certData)
    Observable<ResponseBody> postCertData(@Body Certificate certificate);

    @Headers("AccountKey:"+ URLUtil.LTAKey)
    @GET(URLUtil.busArrival)
    Observable<ResponseBody> getBusArrivals();
}
