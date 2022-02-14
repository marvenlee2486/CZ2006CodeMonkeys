package com.CodeMonkey.saveme.Boundary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.CodeMonkey.saveme.R;

/***
 * RegSignPage created by Luo Yihang 12/02/2022
 * The OTP page for user to input the sent OTP
 */

public class OTPPage extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_otp_page);
        init();
    }

    private void init(){
        //Initialize buttons

    }

    @Override
    public void onClick(View view) {

    }
}