package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;
import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
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
    private Intent intent;
    private String phoneNumString;
    private String psw;

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

        intent = getIntent();
        phoneNumString = "+65" + intent.getStringExtra("phoneNum");
        psw = intent.getStringExtra("password");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.resend:
                Amplify.Auth.resendSignUpCode(
                        phoneNumString,
                        result -> Log.i("AuthDemo", "Code was sent again: " + result.toString()),
                        error -> Log.e("AuthDemo", "Failed to resend code.", error)
                );
                break;
            case R.id.updateContactInfo:
                intent = new Intent(OTPPage.this, RegSignPage.class);
                startActivity(intent);
                break;
            case R.id.allowButton:
                onPressConfirm();
                break;
        }
    }

    private void onPressConfirm(){
        Amplify.Auth.confirmSignUp(
                phoneNumString,
                otpText.getText().toString(),
                result -> {
                    Amplify.Auth.signIn(
                            phoneNumString,
                            psw,
                            result2 ->{
                                Amplify.Auth.fetchAuthSession(
                                        result3 -> {
                                            AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result3;
                                            String token = cognitoAuthSession.getUserPoolTokens().getValue().getIdToken();
                                            UserController.getUserController().setToken(token);
                                            Log.e("token", UserController.getUserController().getToken());
                                            UserController.getUserController().getUser().setPhoneNumber(intent.getStringExtra("phoneNum"));
                                            Log.i("Auth", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                                            intent = new Intent(OTPPage.this, RegisterSubPage.class);
                                            startActivity(intent);
                                        },
                                        error -> Log.e("Auth token failed", error.toString())
                                );
                            },
                            error2 ->{}
                    );
                },
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }
}