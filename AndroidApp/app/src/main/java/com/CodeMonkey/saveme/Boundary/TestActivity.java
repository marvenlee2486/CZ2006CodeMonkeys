package com.CodeMonkey.saveme.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.Entity.NewsRspAll;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;

import rx.Observer;


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
    private Button registerPageButton;
    private Button registerSubPageButton;
    private Button signInPageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_page);

        regSignButton = findViewById(R.id.regSignPageButton);
        locaServButton = findViewById(R.id.locaServPage);
        mainPageButton = findViewById(R.id.mainPage);
        OTPPageButton = findViewById(R.id.otp);
        contactServButton = findViewById(R.id.contactServPage);
        registerPageButton = findViewById((R.id.registerPage));
        registerSubPageButton = findViewById((R.id.registerSubPage));
        signInPageButton = findViewById((R.id.sigInPage));

        regSignButton.setOnClickListener(this);
        locaServButton.setOnClickListener(this);
        mainPageButton.setOnClickListener(this);
        OTPPageButton.setOnClickListener(this);
        contactServButton.setOnClickListener(this);
        registerPageButton.setOnClickListener(this);
        registerSubPageButton.setOnClickListener(this);
        signInPageButton.setOnClickListener(this);

        initData();
//        try {
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Amplify.configure(getApplicationContext());
//            Log.i("MyAmplifyApp", "Initialized Amplify");
//        } catch (AmplifyException error) {
//            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
//        }
//        Amplify.Auth.fetchAuthSession(
//                result -> Log.i("AmplifyQuickstart", result.toString()),
//                error -> Log.e("AmplifyQuickstart", error.toString())
//        );
//        AuthSignUpOptions options = AuthSignUpOptions.builder()
//                .userAttribute(AuthUserAttributeKey.phoneNumber(), "+6500000000")
//                .build();
//        Amplify.Auth.signUp("+6500000000", "test1234", options,
//                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
//                error -> Log.e("AuthQuickStart", "Sign up failed", error)
//        );
//        Amplify.Auth.confirmSignUp(
//                "+6500000000",
//                "t1234",
//                result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
//                error -> Log.e("AuthQuickstart", error.toString())
//        );
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
            case R.id.registerPage:
                intent = new Intent(TestActivity.this, RegisterMainPage.class);
                break;
            case R.id.registerSubPage:
                intent = new Intent(TestActivity.this, RegisterSubPage.class);
                break;
            case R.id.sigInPage:
                intent = new Intent(TestActivity.this, SignInPage.class);
                break;
        }
        startActivity(intent);
    }


    private void initData() {
        RequestUtil.getNews(new Observer<NewsRspAll>() {
            @Override
            public void onCompleted() {
                //完成
            }

            @Override
            public void onError(Throwable e) {
                //失败
                Log.i("retrofit==111=", "Error："+e.getMessage());
            }

            @Override
            public void onNext(NewsRspAll newsRspAll) {
                //成功
                Toast.makeText(TestActivity.this,  newsRspAll.getTotalResults()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
