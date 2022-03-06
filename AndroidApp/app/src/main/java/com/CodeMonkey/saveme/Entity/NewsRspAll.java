package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;

public class NewsRspAll {
    /**
     * 新闻接口返回内容
     */

    private String status;
    private int totalResults;
    private List<JsonObject> articles;

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public void setTotalResults(int totalResults){
        this.totalResults = totalResults;
    }

    public int getTotalResults(){
        return totalResults;
    }

    public void setNews(List<JsonObject> articles){
        this.articles = articles;
    }

    public List<JsonObject> getNews(){
        return articles;
    }
}

