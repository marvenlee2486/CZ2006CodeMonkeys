package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;

public class RegisterSubPage extends BaseActivity implements View.OnClickListener{

    private EditText name;
    private EditText nric;
    private EditText homeAddress;
    private TextView homeAddressLocation;
    private EditText workAddress;
    private TextView workAddressLocation;
    private String exactHomeAddress;
    private String exactWorkAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_register_sub_page);
        init();
    }

    private void init(){

        name = findViewById(R.id.name);
        nric = findViewById(R.id.NRIC);
        homeAddress = findViewById(R.id.homeAddress);
        homeAddressLocation = findViewById(R.id.homeAddressLocation);
        workAddress = findViewById(R.id.workAddress);
        workAddressLocation = findViewById(R.id.workAddressLocation);

        homeAddressLocation.setOnClickListener(this);
        workAddressLocation.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.homeAddressLocation:
                intent = new Intent(RegisterSubPage.this, SelectLocationPage.class);
                intent.putExtra("Type", "home address");
                startActivityForResult(intent, 1);
                break;
            case R.id.workAddressLocation:
                intent = new Intent(RegisterSubPage.this, SelectLocationPage.class);
                intent.putExtra("Type", "work address");
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        switch (requestCode){
            case 1:
                exactHomeAddress = data.getDoubleExtra("latitude", 0) + "," + data.getDoubleExtra("longitude", 0);
                String homeAddressString = String.format("%.6f",data.getDoubleExtra("latitude", 0)) + "," + String.format("%.6f",data.getDoubleExtra("longitude", 0));
                homeAddressLocation.setText(homeAddressString);
                break;
            case 2:
                exactHomeAddress = data.getDoubleExtra("latitude", 0) + "," + data.getDoubleExtra("longitude", 0);
                String workAddressString = String.format("%.6f",data.getDoubleExtra("latitude", 0)) + "," + String.format("%.6f",data.getDoubleExtra("longitude", 0));
                workAddressLocation.setText(workAddressString);
                break;
        }
    }
}