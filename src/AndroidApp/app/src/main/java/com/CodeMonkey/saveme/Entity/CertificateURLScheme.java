package com.CodeMonkey.saveme.Entity;

public class CertificateURLScheme {

    private String phoneNumber;
    private String fileExtension;

    public String getFileExtension(){
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
