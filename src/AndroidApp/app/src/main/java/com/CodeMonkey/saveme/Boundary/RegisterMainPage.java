package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CodeMonkey.saveme.R;

/***
 * RegisterMainPage created by Luo Yihang 12/02/2022
 *
 */

public class RegisterMainPage extends BaseActivity implements View.OnClickListener{

    private EditText phoneNum;
    private EditText psw;
    private EditText confirmPsw;
    private Button next;
    private TextView signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_register_main_page);
        init();
    }

    private void init(){
        phoneNum = findViewById(R.id.phoneNum);
        psw = findViewById(R.id.psw);
        confirmPsw = findViewById(R.id.confirmPsw);
        next = findViewById(R.id.registerNextButton);
        signIn = findViewById(R.id.goToSignPageButton);

        next.setOnClickListener(this);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.registerNextButton:
                if (checkInput()) {
                    intent = new Intent(RegisterMainPage.this, OTPPage.class);
                    startActivity(intent);
                }

                break;
            case R.id.goToSignPageButton:
                intent = new Intent(RegisterMainPage.this, SignInPage.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean checkInput(){
        if (phoneNum.getText().toString().length() != 8) {
            Toast.makeText(this, R.string.phoneNumNotCorrect, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (psw.getText().toString().length() < 6) {
            Toast.makeText(this, R.string.pswTooShort, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!psw.getText().toString().equals(confirmPsw.getText().toString())){
            Toast.makeText(this, R.string.pswNotTheSameAsConfirmPassword, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}