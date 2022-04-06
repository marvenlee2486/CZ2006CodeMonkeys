package com.CodeMonkey.saveme.Entity;

import java.util.List;

public class Rescuer extends User{
    private String name;
    private boolean hasUploadedCertificate;
    private boolean isVerified;
    private List<S3BucketParameters> s3BucketParametersList;
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

    public void setCertificateList (List<S3BucketParameters> s3BucketParametersList){
        this.s3BucketParametersList = s3BucketParametersList;
    }

    public List<S3BucketParameters> getCertificateList(){
        return s3BucketParametersList;
    }
  
    public void setAchievementList (List<Achivement> achievementList){
        this.achievementList = achievementList;
    }

    public List<Achivement> getAchievementList(){
        return achievementList;
    }



}

