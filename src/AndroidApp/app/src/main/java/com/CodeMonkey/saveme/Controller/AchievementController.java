package com.CodeMonkey.saveme.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AchievementController {
    private static AchievementController instance;

    public static AchievementController getInstance()
    {
        if (instance == null)
        {
            instance = new AchievementController();
        }
        return instance;
    }

    public ArrayList<String> GetAchievement(){

        ArrayList<String> achievementNameList = new ArrayList<String>();
        Log.i("Achievement", UserController.getUserController().getUser().getIsVolunteer());
        Log.i("Achievement", String.valueOf(UserController.getUserController().getUser().getNumberOfRescue()));

        if(UserController.getUserController().getUser().getIsVolunteer().equals("YES")){
            achievementNameList.add("I am helping!");
        }
        if(UserController.getUserController().getUser().getNumberOfRescue() > 0){
            achievementNameList.add("Save Once!");
        }
        if(UserController.getUserController().getUser().getNumberOfRescue() >= 3){
            achievementNameList.add("Three Save!");
        }
        if(UserController.getUserController().getUser().getNumberOfRescue() >= 5){
            achievementNameList.add("Gold Saver!");
        }

        return achievementNameList;
    }

}
