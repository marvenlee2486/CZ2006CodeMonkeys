package com.CodeMonkey.saveme.Entity;

import com.google.gson.JsonObject;

import java.util.List;
import java.sql.Timestamp;

public class Certificate {

    private String phoneNumber;
    private String fileData;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }


}

