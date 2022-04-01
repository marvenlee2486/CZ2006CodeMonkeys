package com.CodeMonkey.saveme.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.CodeMonkey.saveme.Controller.EventController;
import com.CodeMonkey.saveme.R;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

/***
 * RescuePageFrag created by Wang Tianyu 07/03/2022
 * RescuePage when user is already a rescuer
 */

public class RescuePageFrag extends Fragment implements View.OnClickListener{

    private MapPageFrag mapPageFrag;
    private BusPageFrag busPageFrag = new BusPageFrag();
    private TrainPageFrag trainPageFrag = new TrainPageFrag();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment = mapPageFrag;
    private RelativeLayout mapButton;
    private RelativeLayout busButton;
    private RelativeLayout trainButton;
    private RelativeLayout instrucButton;
    private ImageView currentLine;
    private Context context;

    private FrameLayout mainPageContent;

    public RescuePageFrag(Context context){
        this.context = context;
        mapPageFrag = new MapPageFrag(context);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rescue_page_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        //Initiate fragments
        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.rescuePageMainContent, mapPageFrag);
        fragmentTransaction.show(mapPageFrag);
        fragmentTransaction.hide(busPageFrag);
        fragmentTransaction.hide(trainPageFrag);
//        fragmentTransaction.hide(instrucPageFrag);
        fragmentTransaction.commit();

        //Initiate buttons
        mapButton = getView().findViewById(R.id.mapButton);
        trainButton = getView().findViewById(R.id.trainButton);
        busButton = getView().findViewById(R.id.busButton);
        instrucButton = getView().findViewById(R.id.instructionButton);
        mapButton.setOnClickListener(this);
        trainButton.setOnClickListener(this);
        busButton.setOnClickListener(this);
        instrucButton.setOnClickListener(this);

        mainPageContent = getView().findViewById(R.id.rescuePageMainContent);
        currentLine = getView().findViewById(R.id.mapLine);

    }

    private void fragmentSwitch(Fragment fragment){
        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(!fragment.isAdded()){
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.add(R.id.rescuePageMainContent, fragment);
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
            case R.id.mapButton:
                mapPageFrag.removeKmlLayer();
                fragmentSwitch(mapPageFrag);
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.mapLine);
                currentLine.setVisibility(View.VISIBLE);
                break;
            case R.id.trainButton:
                fragmentSwitch(trainPageFrag);
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.trainLine);
                currentLine.setVisibility(View.VISIBLE);
                break;
            case R.id.busButton:
                fragmentSwitch(busPageFrag);
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.busLine);
                currentLine.setVisibility(View.VISIBLE);
                break;
            case R.id.instructionButton:
                mapPageFrag.setKmlLayer();
                fragmentSwitch(mapPageFrag);
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.instructionLine);
                currentLine.setVisibility(View.VISIBLE);
                break;
        }

    }

}
