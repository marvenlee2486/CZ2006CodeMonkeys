package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;
import com.amplifyframework.core.Amplify;

/***
 * RegSignPage created by Luo Yihang 12/02/2022
 * The OTP page for user to input the sent OTP
 */

public class OTPPage extends BaseActivity implements View.OnClickListener{

    private TextView resend;
    private TextView updateInfo;
    private Button confirm;
    private EditText otpText;

    private String phoneNumString;

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
        otpText = findViewById(R.id.idEnterCode);

        resend.setOnClickListener(this);
        updateInfo.setOnClickListener(this);
        confirm.setOnClickListener(this);

        Intent intent = getIntent();
        phoneNumString = "+65" + intent.getStringExtra("phoneNum");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.resend:

                break;
            case R.id.updateContactInfo:
                intent = new Intent(OTPPage.this, RegSignPage.class);
                break;
            case R.id.allowButton:
                onPressConfirm();
                intent = new Intent(OTPPage.this, RegisterSubPage.class);
                startActivity(intent);
                break;
        }
    }

    private void onPressConfirm(){
        Amplify.Auth.confirmSignUp(
                phoneNumString,
                otpText.getText().toString(),
                result -> {
                    Log.i("Auth", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}