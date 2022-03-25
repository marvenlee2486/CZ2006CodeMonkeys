
package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;

import javax.swing.text.html.ImageView;

import java.sql.Timestamp;


public class User {
    private String id;
    private String phoneNumber;
    private String name;
    private String nric;
    private Location homeAddress;
    private Location workAddress;
    private int age;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private List<String> medicalConditions;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setNric(String nric){
        this.nric = nric;
    }

    public String getNric(){
        return nric;
    }

    public void setHomeAddress(Location homeAddress){
        this.homeAddress = homeAddress;
    }

    public Location getHomeAddress(){
        return homeAddress;
    }
    
    public void setWorkAddress(Location workAddress){
        this.workAddress = workAddress;
    }

    public Location getWorkAddress(){
        return workAddress;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getAge(){
        return age;
    }

    public void setEmergencyContactName(String emergencyContactName){
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactName(){
        return emergencyContactName;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber){
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public String getEmergencyContactNumber(){
        return emergencyContactNumber;
    }

    public void setMedicalConditions(List<String> medicalConditions){
        this.medicalConditions = medicalConditions;
    }

    public List<String> getMedicalConditions(){
        return emergencyContactNumber;
    }
}
