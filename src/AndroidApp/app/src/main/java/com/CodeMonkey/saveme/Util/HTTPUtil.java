package com.CodeMonkey.saveme.Util;

import com.CodeMonkey.saveme.Entity.NewsRspAll;

import retrofit2.http.GET;
import rx.Observable;

/***
 * RegSignPage created by Wang Tianyu 10/02/2022
 * Util for HTTP request interface
 */
public interface HTTPUtil {

    @GET(URLUtil.testAPI)
    Observable<NewsRspAll> getNews();
}
