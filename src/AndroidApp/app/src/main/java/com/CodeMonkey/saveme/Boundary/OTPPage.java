package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;

/***
 * RegSignPage created by Luo Yihang 12/02/2022
 * The OTP page for user to input the sent OTP
 */

public class OTPPage extends BaseActivity implements View.OnClickListener{

    private TextView resend;
    private TextView updateInfo;
    private Button confirm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_otp_page);
        init();
    }

    private void init(){
        //Initialize buttons
        resend = findViewById(R.id.resend);
        updateInfo = findViewById(R.id.updateContactInfo);
        confirm = findViewById(R.id.allowButton);

        resend.setOnClickListener(this);
        updateInfo.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.resend:

                break;
            case R.id.updateContactInfo:
                finish();
                break;
            case R.id.allowButton:
                intent = new Intent(OTPPage.this, RegisterSubPage.class);
                startActivity(intent);
                break;

        }
    }
}