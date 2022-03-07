package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CodeMonkey.saveme.R;

/***
 * RegSignPage created by Wang Tianyu 10/02/2022
 * The first page when user opened our app will see
 */

public class RegSignPage extends BaseActivity implements View.OnClickListener{

    private Button registerButton;
    private Button signButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_sign_page);
        autoSignIn();
        init();
    }

    private void init(){
        //Initialize buttons
        registerButton = findViewById(R.id.registerButton);
        signButton = findViewById(R.id.signButton);

        registerButton.setOnClickListener(this);
        signButton.setOnClickListener(this);
    }

    private void autoSignIn(){
        //If user has log in or register before, automatically sign in

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

}
