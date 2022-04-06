package com.CodeMonkey.saveme.Controller;

import android.util.Log;

public class CertificateController{

    private volatile static CertificateController instance;

    public static CertificateController getInstance(){
        if (instance == null){
            synchronized (CertificateController.class){
                if (instance == null)
                    instance = new CertificateController();
            }
        }
        return instance;
    }

    public String getCertificateUrl(){
        if(UserController.getUserController().getUser().getIsVolunteer().equals("PLEDGED")){

            if(UserController.getUserController().getUser().getCertificateUrl() != null){
                return UserController.getUserController().getUser().getCertificateUrl();
            }
            else{
                Log.w("Certificate", "No certificate image url, use default image");
                return "https://volunteercerticatebucket.s3.ap-southeast-1.amazonaws.com/poh+swee+hong.jpg";
            }
        }
       return null;
    }

}
