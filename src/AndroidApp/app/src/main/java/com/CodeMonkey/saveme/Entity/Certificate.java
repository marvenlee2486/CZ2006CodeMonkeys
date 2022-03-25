package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;
import java.sql.Timestamp;

public class Certificate {
    private String type;
    private String name;
    private String imgUrl;
    private boolean isVerified;
   
    
    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getImgUrl(){
        return imgUrl;
    }
    
    public void setIsVerified (String isVerified){
        this.isVerified = isVerified;
    }

    public boolean getIsVerified(){
        return isVerified;
    }





}

