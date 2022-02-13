package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;

public class TestActivity extends BaseActivity implements View.OnClickListener{

    private Button regSignButton;
    private Button locaServButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);

        regSignButton = findViewById(R.id.regSignPageButton);
        locaServButton = findViewById(R.id.locaServPage);

        regSignButton.setOnClickListener(this);
        locaServButton.setOnClickListener(this);
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
        }
        startActivity(intent);
    }
}
