package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
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
            intent = new Intent(getApplicationContext(), RegSignPage.class);
        }
        else {
            intent = new Intent(getApplicationContext(), TestActivity.class);
            Log.i("Auth", Amplify.Auth.getCurrentUser().getUsername());
        }
        startActivity(intent);
        finish();
    }



}
