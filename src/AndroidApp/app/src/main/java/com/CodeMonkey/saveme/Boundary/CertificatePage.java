package com.CodeMonkey.saveme.Boundary;



import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.CodeMonkey.saveme.Controller.CertificateController;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;


public class CertificatePage extends BaseActivity {
    private TextView mainName;
    private ImageView certificateImage;

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_profile2);
        init();
        DisplayPage();
    }

    private void init(){
        mainName = findViewById(R.id.mainName);
        certificateImage = findViewById(R.id.certificatePhoto);
    }

    private void DisplayPage(){
        mainName.setText(UserController.getUserController().getUser().getName());
        certificateImage.setImageURI(Uri.parse(CertificateController.getInstance().getCertificateUrl()));
    }
}