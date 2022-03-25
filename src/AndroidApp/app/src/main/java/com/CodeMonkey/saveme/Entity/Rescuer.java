package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;

import javax.swing.text.html.ImageView;

import java.sql.Timestamp;

public class Rescuver {
    private boolean hasUploadedCertificate;
    private boolean isVerified;
    private List<Certificate> certificateList;
    private List<Achivement> achivementList;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setIsVerified (String isVerified){
        this.isVerified = isVerified;
    }

    public boolean getIsVerified(){
        return isVerified;
    }

    public void setCertificateList (String certificateList){
        this.certificateList = certificateList;
    }

    public List<Certificate> getCertificateList(){
        return certificateList;
    }
  
    public void setAchivementList (String achivementList){
        this.achivementList = achivementList;
    }

    public List<Achivement> getAchivementList(){
        return achivementList;
    }



}

