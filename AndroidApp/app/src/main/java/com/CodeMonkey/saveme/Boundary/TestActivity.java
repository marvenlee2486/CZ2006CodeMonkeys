package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;


/***
 * TestActivity created by Wang Tianyu 13/02/2022
 * Debugging page for testing
 */
public class TestActivity extends BaseActivity implements View.OnClickListener{

    private Button regSignButton;
    private Button locaServButton;
    private Button mainPageButton;
    private Button OTPPageButton;
    private Button contactServButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);

        regSignButton = findViewById(R.id.regSignPageButton);
        locaServButton = findViewById(R.id.locaServPage);
        mainPageButton = findViewById(R.id.mainPage);
        OTPPageButton = findViewById(R.id.otp);
        contactServButton = findViewById(R.id.contactServPage);

        regSignButton.setOnClickListener(this);
        locaServButton.setOnClickListener(this);
        mainPageButton.setOnClickListener(this);
        OTPPageButton.setOnClickListener(this);
        contactServButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.regSignPageButton:
                intent = new Intent(TestActivity.this, RegSignPage.class);
                break;
            case R.id.locaServPage:
                intent = new Intent(TestActivity.this, LocaServPage.class);
                break;
            case R.id.mainPage:
                intent = new Intent(TestActivity.this, MainPage.class);
                break;
            case R.id.otp:
                intent = new Intent(TestActivity.this, OTPPage.class);
                break;
            case R.id.contactServPage:
                intent = new Intent(TestActivity.this, ContactServPage.class);
                break;
        }
        startActivity(intent);
    }
}
