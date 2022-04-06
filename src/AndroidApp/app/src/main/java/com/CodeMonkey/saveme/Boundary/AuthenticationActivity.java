package com.CodeMonkey.saveme.Boundary;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.CodeMonkey.saveme.Controller.LanguageController;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.UserRsp;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.result.AuthSessionResult;
import com.amplifyframework.core.Amplify;
import com.google.android.material.tabs.TabLayout;

import rx.Observer;


public class AuthenticationActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageController.getInstance().setLanguage(this);
        setContentView(R.layout.reg_sign_page);
        autoSignIn();
    }

    private void autoSignIn(){
        //If user has log in or register before, automatically sign in

        AuthUser currentUser = Amplify.Auth.getCurrentUser();

        Intent intent = new Intent(getApplicationContext(), RegSignPage.class);

        if(currentUser == null){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                intent = new Intent(getApplicationContext(), LocaServPage.class);
            startActivity(intent);
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.autologinpopup));
            AlertDialog dialog = builder.create();
            dialog.show();
            Log.i("Auth", currentUser.getUserId());
            Amplify.Auth.fetchAuthSession(
                    result -> {
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                        String token = cognitoAuthSession.getUserPoolTokens().getValue().getIdToken();
                        Log.i("token", token);
                        UserController.getUserController().setToken(token);
                        RequestUtil.getUserData(new Observer<UserRsp>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Auth", e.toString());
                            }

                            @Override
                            public void onNext(UserRsp userRsp) {
                                UserController.getUserController().setUser(userRsp.getBody());
                                Log.i("User info", userRsp.getBody().toString());
                                Log.i("Auth", Amplify.Auth.getCurrentUser().getUsername());
                                Intent intent2 = new Intent(getApplicationContext(),  MainPage.class);
                                intent2.putExtra("type", "common");
                                startActivity(intent2);
                                finish();
                            }
                        }, currentUser.getUsername().substring(3), token);
                    },
                    error -> Log.e("Auth token failed", error.toString())
            );
        }
    }
}
