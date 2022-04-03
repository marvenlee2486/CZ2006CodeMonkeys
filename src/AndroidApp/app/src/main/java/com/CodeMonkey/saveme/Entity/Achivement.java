package com.CodeMonkey.saveme.Entity;

import android.widget.ImageView;

import com.google.gson.JsonObject;

import java.util.List;

import java.sql.Timestamp;

public class Achivement {
    private String name;
    private ImageView icon;
    private ImageView icon2;
   

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setIcon(ImageView icon){
        this.icon = icon;
    }

    public ImageView getIcon(){
        return icon;
    }

    public void setIcon2(ImageView icon2){
        this.icon2 = icon2;
    }

    public ImageView getIcon2(){
        return icon2;
    }

}

