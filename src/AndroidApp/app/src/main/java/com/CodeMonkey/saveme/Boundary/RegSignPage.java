package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;

/***
 * RegSignPage created by Wang Tianyu 10/02/2022
 * The first page when user opened our app will see
 */

public class RegSignPage extends BaseActivity implements View.OnClickListener{

    private Button registerButton;
    private Button signButton;
//    private TCPManager tcpManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tcpManager = TCPManager.getTCPManager();
        setContentView(R.layout.reg_sign_page);
//        autoSignIn();
        init();
    }

    private void init(){
        //Initialize buttons
        registerButton = findViewById(R.id.registerButton);
        signButton = findViewById(R.id.signButton);

        registerButton.setOnClickListener(this);
        signButton.setOnClickListener(this);

//        TCPClient.sendTcpMessage("test");
//        tcpManager.send("1234");


    }

    private void autoSignIn(){
        //If user has log in or register before, automatically sign in
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i("test", userStateDetails.getUserState().toString());
                switch (userStateDetails.getUserState()){
                    case SIGNED_IN:
                        Intent i = new Intent(RegSignPage.this, TestActivity.class);
                        startActivity(i);
                        break;
                    case SIGNED_OUT:
                        showSignIn();
                        break;
                    default:
                        AWSMobileClient.getInstance().signOut();
                        showSignIn();
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("test", e.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.registerButton:
                intent = new Intent( RegSignPage.this, RegisterMainPage.class);
                break;
            case R.id.signButton:
                //Switch to sign in page
                intent = new Intent(RegSignPage.this, SignInPage.class);
                break;
        }
        startActivity(intent);
    }

    private void showSignIn() {
        try {
            AWSMobileClient.getInstance().showSignIn(this,
                    SignInUIOptions.builder().nextActivity(AuthenticationActivity.class).build());
        } catch (Exception e) {
            Log.e("test", e.toString());
        }
    }

}
