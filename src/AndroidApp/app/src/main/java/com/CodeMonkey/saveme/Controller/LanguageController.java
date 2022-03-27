package com.CodeMonkey.saveme.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageController{
    private static LanguageController instance;

    public static LanguageController getInstance()
    {
        if (instance == null)
        {
            instance = new LanguageController();
        }
        return instance;
    }

    public void setLanguage(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        int language = preferences.getInt("language", 0);

        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();

        switch (language){
            case 0:
                configuration.setLocale(Locale.ENGLISH);
                break;
            case 1:
                configuration.setLocale(Locale.CHINESE);
                break;
            default:
                break;
        }

        resources.updateConfiguration(configuration,displayMetrics);

    }
}