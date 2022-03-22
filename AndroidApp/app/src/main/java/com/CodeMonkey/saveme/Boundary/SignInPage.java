package com.CodeMonkey.saveme.Boundary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.CodeMonkey.saveme.R;

public class SignInPage extends AppCompatActivity implements View.OnClickListener{
    private Button next;
    private TextView forgetPsw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_sign_in_page);
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
        switch(view.getId()){
            case R.id.allowButton:

                break;
            case R.id.forgetPasswordButton:

                break;
        }
    }
}