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
import com.CodeMonkey.saveme.Entity.UserRsp;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;

import java.util.ArrayList;

import rx.Observer;

/***
 * SignInPage created by Luo Yihang 12/02/2022
 *
 */

public class SignInPage extends BaseActivity implements View.OnClickListener{
    private Button next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_sign_in_page);
        init();
    }

    private void init(){
        //Initialize buttons
        next = findViewById(R.id.allowButton);

        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.allowButton:
                onPressLogin(view);
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
                                    RequestUtil.getUserData(new Observer<UserRsp>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(UserRsp userRsp) {
                                            UserController.getUserController().setUser(userRsp.getBody());
                                        }
                                    }, txtPhoneNum.getText().toString(), token);
                                },
                                error -> Log.e("Auth token failed", error.toString())
                        );
                        Intent intent = new Intent(this, MainPage.class);
                        intent.putExtra("type", "common");
                        startActivity(intent);
                        finishAll();},
                    error -> {Log.e("Auth", "Log in failed, please check your phone number or password");}
            );

    }

}