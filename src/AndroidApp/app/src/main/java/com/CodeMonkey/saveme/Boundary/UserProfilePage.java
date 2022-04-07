package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
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
    private String exactHomeAddress;
    private String exactWorkAddress;

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
        SaveButton.setOnClickListener(this);

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
                if (check()){
                    UpdatePersonalInfo();
                    Log.e("user", user.toString());
                    RequestUtil.postUserData(new Observer<User>() {
                        @Override
                        public void onCompleted() {
                            Toast.makeText(UserProfilePage.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, e.getMessage());
                        }

                        @Override
                        public void onNext(User user) {
                        Log.i(TAG, user.toString());
                    }

                    }, user, UserController.getUserController().getToken());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if (data == null)
            return;
        switch (requestCode){
            case 1:
                exactHomeAddress = data.getDoubleExtra("latitude", 0) + "," + data.getDoubleExtra("longitude", 0);
                String homeAddressString = String.format("%.3f",data.getDoubleExtra("latitude", 0)) + "," + String.format("%.3f",data.getDoubleExtra("longitude", 0));
                homeAddressLocation.setText(homeAddressString);
                break;
            case 2:
                exactHomeAddress = data.getDoubleExtra("latitude", 0) + "," + data.getDoubleExtra("longitude", 0);
                String workAddressString = String.format("%.3f",data.getDoubleExtra("latitude", 0)) + "," + String.format("%.3f",data.getDoubleExtra("longitude", 0));
                workAddressLocation.setText(workAddressString);
                break;
        }
    }

    private void DisplayPage(){
        age.setText(user.getAge());
        mainName.setText(user.getName());
        name.setText(user.getName());
        String latitude;
        String longitude;
        latitude = user.getHomeLocation().split(",")[0];
        longitude = user.getHomeLocation().split(",")[1];
        homeAddress.setText(user.getHomeAddress());
        workAddress.setText(user.getWorkAddress());
        homeAddressLocation.setText(String.format("%.3f", Double.parseDouble(latitude)) + "," + String.format("%.3f", Double.parseDouble(longitude)));
        latitude = user.getWorkLocation().split(",")[0];
        longitude = user.getWorkLocation().split(",")[1];
        workAddressLocation.setText(String.format("%.3f", Double.parseDouble(latitude)) + "," + String.format("%.3f", Double.parseDouble(longitude)));
        emergencyContactName.setText(user.getEmergencyContactName());
        emergencyContactNumber.setText(user.getEmergencyContactNumber());
    }

    private boolean check(){
        if (name.getText().toString().equals("")){
            Toast.makeText(this, "You have not input name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (homeAddress.getText().toString().equals("")){
            Toast.makeText(this, "You have not input home address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (homeAddressLocation.getText().toString().equals("")){
            Toast.makeText(this, "You have not select home address location!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (workAddress.getText().toString().equals("")){
            Toast.makeText(this, "You have not input work address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (workAddressLocation.getText().toString().equals("")){
            Toast.makeText(this, "You have not select work address location!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void UpdatePersonalInfo(){
        user.setName(name.getText().toString());
        user.setHomeAddress(homeAddress.getText().toString());
        user.setHomeLocation(homeAddressLocation.getText().toString());
        user.setWorkAddress(workAddress.getText().toString());
        user.setWorkLocation(workAddressLocation.getText().toString());
        user.setAge(age.getText().toString());
        user.setEmergencyContactName(emergencyContactName.getText().toString());
        user.setEmergencyContactNumber(emergencyContactNumber.getText().toString());
    }
}