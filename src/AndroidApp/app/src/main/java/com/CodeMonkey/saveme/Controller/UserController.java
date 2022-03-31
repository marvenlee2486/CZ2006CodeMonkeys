package com.CodeMonkey.saveme.Controller;

import android.content.Intent;
import android.util.Log;

import com.CodeMonkey.saveme.Boundary.MainPage;
import com.CodeMonkey.saveme.Boundary.RegisterSubPage;
import com.CodeMonkey.saveme.Boundary.TestActivity;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.Entity.UserRsp;
import com.CodeMonkey.saveme.Util.RequestUtil;

import rx.Observer;

public class UserController{

    private User user = new User();
    private String token;
    private volatile static UserController userController;

    private UserController(){}

    public static UserController getUserController(){
        if (userController == null){
            synchronized (UserController.class){
                if (userController == null){
                    userController = new UserController();
                }
            }
        }
        return userController;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCurrentSignInUser(String phoneNumber){
        RequestUtil.getUserData(new Observer<UserRsp>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Error", e.toString());
            }

            @Override
            public void onNext(UserRsp userRsp) {
                setUser(userRsp.getBody());
                Log.e("Result", user.toString());
            }
        }, phoneNumber, token);

    }



}
