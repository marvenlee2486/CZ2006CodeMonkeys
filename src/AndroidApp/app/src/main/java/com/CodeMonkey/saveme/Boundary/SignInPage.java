package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;

import java.util.ArrayList;

/***
 * SignInPage created by Luo Yihang 12/02/2022
 *
 */

public class SignInPage extends BaseActivity implements View.OnClickListener{
    private Button next;
    private TextView forgetPsw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_sign_in_page);
        init();
    }

    private void init(){
        //Initialize buttons
        next = findViewById(R.id.allowButton);
        forgetPsw = findViewById(R.id.forgetPasswordButton);

        next.setOnClickListener(this);
        forgetPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.allowButton:
                onPressLogin(view);
                break;
            case R.id.forgetPasswordButton:
                OnForgetPassword(view);
                break;
        }
    }

    public void onPressLogin(View view) {
            EditText txtPhoneNum = findViewById(R.id.phoneNum);
            EditText txtPassword = findViewById(R.id.password);

            Amplify.Auth.signIn(
                    "+65" + txtPhoneNum.getText().toString(),
                    txtPassword.getText().toString(),
                    result -> {
                        Amplify.Auth.fetchAuthSession(
                                result2 -> {
                                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result2;
                                    String token = cognitoAuthSession.getUserPoolTokens().getValue().getIdToken();
                                    UserController.getUserController().setToken(token);
                                    Log.e("token", UserController.getUserController().getToken());
                                },
                                error -> Log.e("Auth token failed", error.toString())
                        );
                        Intent intent = new Intent(this, TestActivity.class);
                        startActivity(intent);
                        finishAll();},
                    error -> {Toast.makeText(this, "Log in failed, please check your phone number or password", Toast.LENGTH_SHORT).show();}
            );

    }

    public void OnForgetPassword(View view) {
        Amplify.Auth.resetPassword(
                "+6593581948",
                result -> Log.i("AuthQuickstart", result.toString()),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }

}