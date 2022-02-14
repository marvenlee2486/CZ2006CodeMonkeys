package com.CodeMonkey.saveme.Boundary;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.CodeMonkey.saveme.Fragment.ConfigPageFrag;
import com.CodeMonkey.saveme.Fragment.RegVolPageFrag;
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
    private Button saveMePageButton;
    private Button regVolPageButton;
    private Button configPageButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment = saveMePageFrag;

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
        fragmentTransaction.commit();

        //Initiate buttons
        saveMePageButton = findViewById(R.id.getHelpButton);
        regVolPageButton = findViewById(R.id.rescueButton);
        configPageButton = findViewById(R.id.moreButton);
        saveMePageButton.setOnClickListener(this);
        regVolPageButton.setOnClickListener(this);
        configPageButton.setOnClickListener(this);
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
                break;
            case R.id.rescueButton:
                fragmentSwitch(regVolPageFrag);
                break;
            case R.id.moreButton:
                fragmentSwitch(configPageFrag);
                break;
        }
    }
}
