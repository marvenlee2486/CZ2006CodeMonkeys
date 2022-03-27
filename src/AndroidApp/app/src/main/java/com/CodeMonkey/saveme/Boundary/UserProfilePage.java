package com.CodeMonkey.saveme.Boundary;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;

public class UserProfilePage extends BaseActivity {
    private Button CertificatePageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_profile1);
        init();
    }

    private void init(){
        CertificatePageButton = findViewById(R.id.regSignPageButton);
    }
}