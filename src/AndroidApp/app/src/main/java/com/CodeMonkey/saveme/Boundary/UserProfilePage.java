package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;

import rx.Observer;

public class UserProfilePage extends BaseActivity implements View.OnClickListener {
    private Button SaveButton;

    private static final String TAG = RegisterSubPage.class.getSimpleName();

    private TextView mainName;
    private EditText name;
    private EditText homeAddress;
    private TextView homeAddressLocation;
    private EditText workAddress;
    private TextView workAddressLocation;
    private EditText age;
    private EditText emergencyContactName;
    private EditText emergencyContactNumber;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_profile1);
        init();
    }

    private void init(){
        user = UserController.getUserController().getUser();

        SaveButton = findViewById(R.id.saveBtn);

        mainName = findViewById(R.id.mainName);
        name = findViewById(R.id.name);
        homeAddress = findViewById(R.id.homeAddress);
        homeAddressLocation = findViewById(R.id.homeAddressLocation);
        workAddress = findViewById(R.id.workAddress);
        workAddressLocation = findViewById(R.id.workAddressLocation);
        age = findViewById(R.id.age);
        emergencyContactName = findViewById(R.id.emergencyContactName);
        emergencyContactNumber = findViewById(R.id.emergencyContactNum);

        homeAddressLocation.setOnClickListener(this);
        workAddressLocation.setOnClickListener(this);

        DisplayPage();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.homeAddressLocation:
                intent = new Intent(this, SelectLocationPage.class);
                intent.putExtra("Type", "home address");
                startActivityForResult(intent, 1);
                break;
            case R.id.workAddressLocation:
                intent = new Intent(this, SelectLocationPage.class);
                intent.putExtra("Type", "work address");
                startActivityForResult(intent, 2);
                break;
            case R.id.saveBtn:
                UpdatePersonalInfo();
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode,resultCode, data);
//        if (data == null)
//            return;
//        switch (requestCode){
//            case 1:
//                exactHomeAddress = data.getDoubleExtra("latitude", 0) + "," + data.getDoubleExtra("longitude", 0);
//                String homeAddressString = String.format("%.6f",data.getDoubleExtra("latitude", 0)) + "," + String.format("%.6f",data.getDoubleExtra("longitude", 0));
//                homeAddressLocation.setText(homeAddressString);
//                break;
//            case 2:
//                exactHomeAddress = data.getDoubleExtra("latitude", 0) + "," + data.getDoubleExtra("longitude", 0);
//                String workAddressString = String.format("%.6f",data.getDoubleExtra("latitude", 0)) + "," + String.format("%.6f",data.getDoubleExtra("longitude", 0));
//                workAddressLocation.setText(workAddressString);
//                break;
//        }
//    }

    private void DisplayPage(){
        age.setText(user.getAge());
        name.setText(user.getName());
        homeAddress.setText(user.getHomeAddress());
        workAddress.setText(user.getWorkAddress());
        emergencyContactName.setText(user.getEmergencyContactName());
        emergencyContactNumber.setText(user.getEmergencyContactNumber());
    }

    private void UpdatePersonalInfo(){
        user.setName(name.getText().toString());
        user.setHomeAddress(homeAddress.getText().toString());
        user.setHomeLocation(homeAddressLocation.getText().toString());
        user.setWorkAddress(workAddress.getText().toString());
        user.setWorkLocation(workAddressLocation.getText().toString());
        user.setIsVolunteer("NO");
        if (!age.getText().toString().equals(""))
            user.setAge(age.getText().toString());
        if (!emergencyContactName.getText().toString().equals(""))
            user.setEmergencyContactName(emergencyContactName.getText().toString());
        if (!emergencyContactNumber.getText().toString().equals(""))
            user.setEmergencyContactNumber(emergencyContactNumber.getText().toString());
    }
}