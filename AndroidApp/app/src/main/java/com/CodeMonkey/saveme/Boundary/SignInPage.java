package com.CodeMonkey.saveme.Boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.CodeMonkey.saveme.R;

public class SignInPage extends AppCompatActivity {
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_sign_in_page);
    }

    private void init(){
        //Initialize buttons
        registerButton = findViewById(R.id.registerButton);
    }
}