package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;

import java.sql.Timestamp;

public class Rescuer extends User{
    private String name;
    private boolean hasUploadedCertificate;
    private boolean isVerified;
    private List<Certificate> certificateList;
    private List<Achivement> achievementList;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setIsVerified (boolean isVerified){
        this.isVerified = isVerified;
    }

    public boolean getIsVerified(){
        return isVerified;
    }

    public void setCertificateList (List<Certificate> certificateList){
        this.certificateList = certificateList;
    }

    public List<Certificate> getCertificateList(){
        return certificateList;
    }
  
    public void setAchievementList (List<Achivement> achievementList){
        this.achievementList = achievementList;
    }

    public List<Achivement> getAchievementList(){
        return achievementList;
    }



}

