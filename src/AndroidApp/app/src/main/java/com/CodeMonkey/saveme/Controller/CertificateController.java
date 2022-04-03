package com.CodeMonkey.saveme.Controller;

import android.os.Debug;
import android.util.Log;

import com.CodeMonkey.saveme.Entity.Certificate;

import java.util.ArrayList;
import java.util.List;

public class CertificateController{
    private static CertificateController instance;

    public static CertificateController getInstance()
    {
        if (instance == null)
        {
            instance = new CertificateController();
        }
        return instance;
    }

    public String getCertificateUrl(){
        if(UserController.getUserController().getUser().getIsVolunteer().equals("YES")){

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
