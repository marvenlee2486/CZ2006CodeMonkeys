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
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.CodeMonkey.saveme.Controller.EventController;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.GovDataRsp;
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
import com.CodeMonkey.saveme.Util.RequestUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;


/***
 * MainPage created by Wang Tianyu 14/02/2022
 * Page when user sign in will see
 */
public class MainPage extends BaseActivity implements View.OnClickListener{

    private static final String TAG = MainPage.class.getSimpleName();

    private SaveMePageFrag saveMePageFrag = new SaveMePageFrag();
    private RegVolPageFrag regVolPageFrag = new RegVolPageFrag();
    private ConfigPageFrag configPageFrag = new ConfigPageFrag();
    private RescuePageFrag rescuePageFrag;
    private VolPledgePageFrag volPledgePageFrag;
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
        //Initiate buttons
        saveMePageButton = findViewById(R.id.getHelpButton);
        regVolPageButton = findViewById(R.id.rescueButton);
        configPageButton = findViewById(R.id.moreButton);
        saveMePageButton.setOnClickListener(this);
        regVolPageButton.setOnClickListener(this);
        configPageButton.setOnClickListener(this);

        volPledgePageFrag = new VolPledgePageFrag(regVolPageButton);
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

        mainPageContent = findViewById(R.id.mainPageContent);
        morePageContainer = findViewById(R.id.morePageContainer);
        morePageMask = findViewById(R.id.morePageMask);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.what == 1) {
                    String result = (String) message.obj;
                    String[] results = result.split(";");
                    switch (results[0]) {
                        case "REQUEST":
                            if (!EventController.getEventController().getEventList().containsKey(results[1]) && UserController.getUserController().getUser().getIsVolunteer().equals("PLEDGED") && !UserController.getUserController().getUser().getPhoneNumber().equals(results[1]))
                                EventController.getEventController().addNewEvent(results[1], results[2], results[3]);
                            break;
                        case "CANCELRESCUEME":
                            EventController.getEventController().removeEvent(results[1]);
                            break;
                        case "UPDATERESCUERS":
                            if (!UserController.getUserController().getUser().getPhoneNumber().equals(results[1]))
                                EventController.getEventController().setRescueNumber(results[1], Integer.parseInt(results[2]));
                            break;
                        case "MSG":
                            String msg = "";
                            for (int i = 3; i < results.length; i++)
                                msg += results[i];
                            rescuePageFrag.newMessage(results[2], msg);
                            break;
                    }
                }
                return false;
            }
        });
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
        switch (UserController.getUserController().getUser().getIsVolunteer()){
            case "NO":
                fragmentSwitch(regVolPageFrag);
                break;
            case "PENDING":
            case "REJECTED":
                fragmentSwitch(regVolPageFrag);
                regVolPageFrag.setCertificate();
                break;
            case "YES":
                fragmentSwitch(volPledgePageFrag);
                break;

            case "PLEDGED":
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


    public void detectEvents(){
        if (EventController.getEventController().getEventList().size() != 0)
            fragmentSwitch(rescuePageFrag);
        else
            fragmentSwitch(noRequestRescuePageFrag);
    }



}
