package com.CodeMonkey.saveme.Util;


import com.CodeMonkey.saveme.Entity.Certificate;
import com.CodeMonkey.saveme.Entity.CertificateRsp;
import com.CodeMonkey.saveme.Entity.CertificateURLScheme;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.Entity.UserRsp;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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
    Observable<UserRsp> getUserData(@Query("phoneNumber") String phoneNumber, @Header("Authorization") String token);

    @POST(URLUtil.userData)
    Observable<User> postUserData(@Body User user, @Header("Authorization") String token);

    @PUT(URLUtil.userData)
    Observable<User> putUserData(@Body User user, @Header("Authorization") String token);

    @GET(URLUtil.certData)
    Observable<ResponseBody> getCertData(@Query("phoneNumber") String phoneNumber);

    @POST(URLUtil.certData)
    Observable<CertificateRsp> postCertData(@Body CertificateURLScheme certificateURLScheme, @Header("Authorization") String token);

    @POST()
    Observable<ResponseBody> postRealCertData(@Body Certificate certificate);

    @GET(URLUtil.validation)
    Observable<ResponseBody> checkAvailability(@Query("phoneNumber") String phoneNumber, @Header("Authorization") String token);

    @Headers("AccountKey:"+ URLUtil.LTAKey)
    @GET(URLUtil.busArrival)
    Observable<ResponseBody> getBusArrivals(@Query("BusStopCode") String busStopCode);
    
}
