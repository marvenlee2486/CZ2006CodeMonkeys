package com.CodeMonkey.saveme.Boundary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.CodeMonkey.saveme.Controller.EventController;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.Util.NotificationUtil;
import com.CodeMonkey.saveme.Controller.TCPManager;
import com.CodeMonkey.saveme.Entity.Event;
import com.CodeMonkey.saveme.Fragment.ConfigPageFrag;
import com.CodeMonkey.saveme.Fragment.NoRequestRescuePageFrag;
import com.CodeMonkey.saveme.Fragment.RegVolPageFrag;
import com.CodeMonkey.saveme.Fragment.RescuePageFrag;
import com.CodeMonkey.saveme.Fragment.SaveMePageFrag;
import com.CodeMonkey.saveme.Fragment.VolPledgePageFrag;
import com.CodeMonkey.saveme.R;

import java.util.ArrayList;
import java.util.List;


/***
 * MainPage created by Wang Tianyu 14/02/2022
 * Page when user sign in will see
 */
public class MainPage extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainPage.class.getSimpleName();

    private SaveMePageFrag saveMePageFrag = new SaveMePageFrag();
    private RegVolPageFrag regVolPageFrag = new RegVolPageFrag();
    private ConfigPageFrag configPageFrag = new ConfigPageFrag();
    private RescuePageFrag rescuePageFrag;
    private VolPledgePageFrag volPledgePageFrag = new VolPledgePageFrag();
    private NoRequestRescuePageFrag noRequestRescuePageFrag = new NoRequestRescuePageFrag();
    private Button saveMePageButton;
    private Button regVolPageButton;
    private Button configPageButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment = saveMePageFrag;
    private Handler handler;
    private List<Event> eventList = new ArrayList<>();

    private FrameLayout mainPageContent;
    private LinearLayout morePageMask;
    private LinearLayout morePageContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        checkPermissions();
        rescuePageFrag = new RescuePageFrag(this);
        init();
        NotificationUtil.createNotificationChannel(this);
    }

    private void init(){
        //Initiate fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(saveMePageFrag);
        fragmentTransaction.hide(regVolPageFrag);
        fragmentTransaction.hide(configPageFrag);
        fragmentTransaction.hide(rescuePageFrag);
        fragmentTransaction.hide(volPledgePageFrag);
        fragmentTransaction.hide(noRequestRescuePageFrag);
        fragmentTransaction.commit();
        fragmentSwitch(saveMePageFrag);

        //Initiate buttons
        saveMePageButton = findViewById(R.id.getHelpButton);
        regVolPageButton = findViewById(R.id.rescueButton);
        configPageButton = findViewById(R.id.moreButton);
        saveMePageButton.setOnClickListener(this);
        regVolPageButton.setOnClickListener(this);
        configPageButton.setOnClickListener(this);

        mainPageContent = findViewById(R.id.mainPageContent);
        morePageContainer = findViewById(R.id.morePageContainer);
        morePageMask = findViewById(R.id.morePageMask);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    String result = (String) msg.obj;
                    String[] results = result.split(";");
                    switch (results[0]) {
                        case "REQUEST":
//                        addNewEvent();
                            break;
                        case "UPDATERESCUERS":

                            break;
                    }
                }
            }
        };

        TCPManager tcpManager = TCPManager.getTCPManager(handler);

        tcpManager.sendLocation(this);

        if (getIntent().getStringExtra("type") == null){
            changeColor(Color.parseColor("#0013C2"));
            fragmentSwitch(rescuePageFrag);
        }


    }

    private void fragmentSwitch(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(!fragment.isAdded()){
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.add(R.id.mainPageContent, fragment);
            fragmentTransaction.show(fragment);
        }else{
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.show(fragment);
        }
        currentFragment = fragment;
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getHelpButton:
                fragmentSwitch(saveMePageFrag);
                changeColor(Color.parseColor("#9F0000"));
                break;
            case R.id.rescueButton:
                changeColor(Color.parseColor("#0013C2"));
                detectVolunteerStatus();
                break;
            case R.id.moreButton:
                fragmentSwitch(configPageFrag);
                changeColor(Color.parseColor("#00882E"));
                break;
        }
    }

    private void changeColor(@ColorInt int topColor){
        saveMePageButton.setBackgroundColor(topColor);
        regVolPageButton.setBackgroundColor(topColor);
        configPageButton.setBackgroundColor(topColor);
    }

    private void detectVolunteerStatus(){
        Log.e("?", UserController.getUserController().getUser().getIsVolunteer());
        switch (UserController.getUserController().getUser().getIsVolunteer()){
            case "NO":
                fragmentSwitch(regVolPageFrag);
                break;
            case "PENDING":
                fragmentSwitch(regVolPageFrag);
                setCertificate();
                break;
            case "YES":
                fragmentSwitch(rescuePageFrag);
                break;

            case "PLEDGED":
                detectEvents();
                break;

            case "REJECTED":
                fragmentSwitch(regVolPageFrag);

                break;

            default:
                detectEvents();
                break;

        }

    }


    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MainPage.this, LocaServPage.class);
                startActivity(intent);
            }

        }
    }

    private void setCertificate(){

    }

    private void detectEvents(){
        if (EventController.getEventController().getEventList().size() != 0)
            fragmentSwitch(rescuePageFrag);
        else
            fragmentSwitch(noRequestRescuePageFrag);
    }



}
