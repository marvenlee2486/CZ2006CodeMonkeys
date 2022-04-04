package com.CodeMonkey.saveme.Util;


import com.CodeMonkey.saveme.Entity.Certificate;
import com.CodeMonkey.saveme.Entity.CertificateRsp;
import com.CodeMonkey.saveme.Entity.CertificateURLScheme;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.Entity.UserRsp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Completable;
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

    @GET()
    Observable<ResponseBody> getCertData(@Url String url);

    @POST(URLUtil.certData)
    Observable<CertificateRsp> postCertData(@Body CertificateURLScheme certificateURLScheme, @Header("Authorization") String token);

    @Multipart
    @POST(".")
    Observable<Completable> postRealCertData(@Part MultipartBody.Part key, @Part MultipartBody.Part token,
                                             @Part MultipartBody.Part accessKey, @Part MultipartBody.Part policy,
                                             @Part MultipartBody.Part signature, @Part("file") RequestBody file);

    @GET(URLUtil.validation)
    Observable<ResponseBody> checkAvailability(@Query("phoneNumber") String phoneNumber, @Header("Authorization") String token);

    @Headers("AccountKey:"+ URLUtil.LTAKey)
    @GET(URLUtil.busArrival)
    Observable<ResponseBody> getBusArrivals(@Query("BusStopCode") String busStopCode);
    
}
