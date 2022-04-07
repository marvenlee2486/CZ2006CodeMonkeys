
package com.CodeMonkey.saveme.Entity;

import android.location.Location;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import java.sql.Timestamp;


public class User {

    private String phoneNumber;
    private List<String> achievementsName = new ArrayList<>();
    private String homeAddress;
    private String homeLocation;
    private String workAddress;
    private String workLocation;
    private String certificateUrl;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private List<String> medicalConditions = new ArrayList<>();
    private String isVolunteer;
    private int numberOfRescue;
    private String name;
    private String age;
    private String dateJoined;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAchievementsName(List<String> achievementsName) {
        this.achievementsName = achievementsName;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setHomeLocation(String homeLocation) {
        this.homeLocation = homeLocation;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public void setMedicalConditions(List<String> medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public void setIsVolunteer(String isVolunteer) {
        this.isVolunteer = isVolunteer;
    }

    public void setNumberOfRescue(int numberOfRescue) {
        this.numberOfRescue = numberOfRescue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getAchievementsName() {
        return achievementsName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getHomeLocation() {
        return homeLocation;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public List<String> getMedicalConditions() {
        return medicalConditions;
    }

    public String getIsVolunteer() {
        return isVolunteer;
    }

    public int getNumberOfRescue() {
        return numberOfRescue;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    @Override
    public String toString() {
        return "User{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", achievementsName=" + achievementsName +
                ", homeAddress='" + homeAddress + '\'' +
                ", homeLocation='" + homeLocation + '\'' +
                ", workAddress='" + workAddress + '\'' +
                ", workLocation='" + workLocation + '\'' +
                ", certificateUrl='" + certificateUrl + '\'' +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                ", emergencyContactNumber='" + emergencyContactNumber + '\'' +
                ", medicalConditions=" + medicalConditions +
                ", isVolunteer='" + isVolunteer + '\'' +
                ", numberOfRescue=" + numberOfRescue +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", dateJoined='" + dateJoined + '\'' +
                '}';
    }
}
