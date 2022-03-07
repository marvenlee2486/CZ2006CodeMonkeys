package com.CodeMonkey.saveme.Boundary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.CodeMonkey.saveme.Fragment.ConfigPageFrag;
import com.CodeMonkey.saveme.Fragment.RegVolPageFrag;
import com.CodeMonkey.saveme.Fragment.RescuePageFrag;
import com.CodeMonkey.saveme.Fragment.SaveMePageFrag;
import com.CodeMonkey.saveme.R;


/***
 * MainPage created by Wang Tianyu 14/02/2022
 * Page when user sign in will see
 */
public class MainPage extends BaseActivity implements View.OnClickListener {

    private SaveMePageFrag saveMePageFrag = new SaveMePageFrag();
    private RegVolPageFrag regVolPageFrag = new RegVolPageFrag();
    private ConfigPageFrag configPageFrag = new ConfigPageFrag();
    private RescuePageFrag rescuePageFrag = new RescuePageFrag();
    private Button saveMePageButton;
    private Button regVolPageButton;
    private Button configPageButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment = saveMePageFrag;

    private FrameLayout mainPageContent;
    private LinearLayout morePageMask;
    private LinearLayout morePageContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        init();

    }

    private void init(){
        //Initiate fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(saveMePageFrag);
        fragmentTransaction.hide(regVolPageFrag);
        fragmentTransaction.hide(configPageFrag);
        fragmentTransaction.hide(rescuePageFrag);
        fragmentTransaction.commit();

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
                stateDetect();
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

    private void stateDetect(){
//        fragmentSwitch(regVolPageFrag);
        fragmentSwitch(rescuePageFrag);
    }
}
