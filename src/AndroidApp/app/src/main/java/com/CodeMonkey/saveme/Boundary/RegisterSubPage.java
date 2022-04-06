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

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observer;


/***
 * RegisterSubPage created by Luo Yihang 12/02/2022
 *
 */

public class RegisterSubPage extends BaseActivity implements View.OnClickListener{


    private static final String TAG = RegisterSubPage.class.getSimpleName();
    private EditText name;
    private EditText homeAddress;
    private TextView homeAddressLocation;
    private EditText workAddress;
    private TextView workAddressLocation;
    private String exactHomeAddress;
    private String exactWorkAddress;
    private EditText age;
    private EditText emergencyContactName;
    private EditText emergencyContactNumber;
    private Button backButton;
    private Button allowButton;
    private User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_register_sub_page);
        init();
    }

    private void init(){

        name = findViewById(R.id.name);
        homeAddress = findViewById(R.id.homeAddress);
        homeAddressLocation = findViewById(R.id.homeAddressLocation);
        workAddress = findViewById(R.id.workAddress);
        workAddressLocation = findViewById(R.id.workAddressLocation);
        age = findViewById(R.id.age);
        emergencyContactName = findViewById(R.id.emergencyContactName);
        emergencyContactNumber = findViewById(R.id.emergencyContactNum);
        backButton = findViewById(R.id.backButton);
        allowButton = findViewById(R.id.allowButton);

        homeAddressLocation.setOnClickListener(this);
        workAddressLocation.setOnClickListener(this);
        backButton.setOnClickListener(this);
        allowButton.setOnClickListener(this);


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
            case R.id.backButton:
                finish();
                break;
            case R.id.allowButton:
                if (check()){
                    storeData();
                    UserController.getUserController().setUser(user);
                    Log.e("user", user.toString());
                    RequestUtil.postUserData(new Observer<User>() {
                        @Override
                        public void onCompleted() {
                            Intent intent = new Intent(RegisterSubPage.this, MainPage.class);
                            intent.putExtra("type", "common");
                            startActivity(intent);
                            finishAll();
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

    private void storeData(){
        user = UserController.getUserController().getUser();
        user.setName(name.getText().toString());
        user.setHomeAddress(homeAddress.getText().toString());
        user.setHomeLocation(exactHomeAddress);
        user.setWorkAddress(workAddress.getText().toString());
        user.setWorkLocation(exactWorkAddress);
        user.setIsVolunteer("NO");
        if (!age.getText().toString().equals(""))
            user.setAge(age.getText().toString());
        if (!emergencyContactName.getText().toString().equals(""))
            user.setEmergencyContactName(emergencyContactName.getText().toString());
        if (!emergencyContactNumber.getText().toString().equals(""))
            user.setEmergencyContactNumber(emergencyContactNumber.getText().toString());
    }
}