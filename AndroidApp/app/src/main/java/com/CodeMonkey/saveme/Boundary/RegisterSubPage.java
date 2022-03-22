package com.CodeMonkey.saveme.Boundary;

import android.os.Bundle;
import android.widget.EditText;

import com.CodeMonkey.saveme.R;

public class RegisterSubPage extends BaseActivity {

    private EditText name;
    private EditText nric;
    private EditText homeAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_register_sub_page);
    }
}