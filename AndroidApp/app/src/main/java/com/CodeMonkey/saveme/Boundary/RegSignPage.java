package com.CodeMonkey.saveme.Boundary;

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

public class RegSignPage extends AppCompatActivity implements View.OnClickListener{

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
        switch (view.getId()){
            case R.id.registerButton:
                //Switch to register page
                break;
            case R.id.signButton:
                //Switch to sign in page
                break;
        }
    }

}
