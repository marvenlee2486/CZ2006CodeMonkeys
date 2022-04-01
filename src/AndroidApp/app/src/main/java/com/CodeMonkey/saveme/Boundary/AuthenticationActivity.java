package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.result.AuthSessionResult;
import com.amplifyframework.core.Amplify;
import com.google.android.material.tabs.TabLayout;


public class AuthenticationActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autoSignIn();
    }

    private void autoSignIn(){
        //If user has log in or register before, automatically sign in

        AuthUser currentUser = Amplify.Auth.getCurrentUser();

        Intent intent;

        if(currentUser == null){
            intent = new Intent(getApplicationContext(), LocaServPage.class);
            startActivity(intent);
            finish();
        }
        else {
            Log.e("Auth", currentUser.getUserId());
            Amplify.Auth.fetchAuthSession(
                    result -> {
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                        String token = cognitoAuthSession.getUserPoolTokens().getValue().getIdToken();
                        Log.e("token", token);
                        UserController.getUserController().setToken(token);
                        UserController.getUserController().setCurrentSignInUser(currentUser.getUsername().substring(3));
                        Log.i("Auth", Amplify.Auth.getCurrentUser().getUsername());
                        Intent intent2 = new Intent(getApplicationContext(),  MainPage.class);
                        intent2.putExtra("type", "common");
                        startActivity(intent2);
                        finish();
                    },
                    error -> Log.e("Auth token failed", error.toString())
            );
        }
    }



}
