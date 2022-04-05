package com.CodeMonkey.saveme.Boundary;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.CodeMonkey.saveme.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/***
 * RegSignPage created by Wang Tianyu 13/02/2022
 * Basic activity
 */
public class BaseActivity extends AppCompatActivity {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static int getTotalActivities(){
        return activities.size();
    }

    public static void finishAll(){
        for (Activity activity: activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy(){
        removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getTotalActivities() == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.exitpopup))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                          dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
            finish();
    }

}
