package com.CodeMonkey.saveme.Fragment;

import android.app.Activity;
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
import com.CodeMonkey.saveme.Controller.TCPManager;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.Event;
import com.CodeMonkey.saveme.Entity.GovDataRsp;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.NotificationUtil;
import com.CodeMonkey.saveme.Util.RequestUtil;
import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/***
 * RescuePageFrag created by Wang Tianyu 07/03/2022
 * RescuePage when user is already a rescuer
 */

public class RescuePageFrag extends Fragment implements View.OnClickListener{

    private MapPageFrag mapPageFrag;
    private ChatRoomPageFrag chatRoomPageFrag;
    private RequestListPageFrag requestListPageFrag;
//    private BusPageFrag busPageFrag;
//    private TrainPageFrag trainPageFrag;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment;
    private RelativeLayout mapButton;
    private RelativeLayout chatButton;
    private RelativeLayout listButton;
//    private RelativeLayout busButton;
//    private RelativeLayout trainButton;
    private RelativeLayout instrucButton;
    private ImageView currentLine;
    private Context context;
    private Handler handler;
    private boolean isMap = true;

    private FrameLayout mainPageContent;

    public RescuePageFrag(Context context){
        this.context = context;
        mapPageFrag = new MapPageFrag(context);
        chatRoomPageFrag = new ChatRoomPageFrag();
        requestListPageFrag = new RequestListPageFrag();
//        busPageFrag = new BusPageFrag();
//        trainPageFrag = new TrainPageFrag();
        currentFragment = mapPageFrag;

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                switch (message.what){
                    case 2:
                        mapPageFrag.newEvent((String) message.obj);
                        requestListPageFrag.getAdapter().addNewEvent((String) message.obj);
                        break;
                    case 3:
                        mapPageFrag.removeEvent((String) message.obj);
                        requestListPageFrag.getAdapter().removeEvent((String) message.obj);
                        break;
                    case 4:
                        mapPageFrag.removeKmlLayer();
                        if (!isMap) {
                            fragmentSwitch(mapPageFrag);
                            isMap = true;
                        }
                        currentLine.setVisibility(View.GONE);
                        currentLine = getView().findViewById(R.id.mapLine);
                        currentLine.setVisibility(View.VISIBLE);
                        EventController.getEventController().getWeather();
                        mapPageFrag.moveToEventMarker((String) message.obj);
                        break;
                    case 6:
                        mapPageFrag.updateRescueNumber((Event) message.obj);
                        break;
                }
                return false;
            }
        });

        EventController.getEventController().setHandler(handler);
        requestListPageFrag.setHandler(handler);
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
        fragmentTransaction.add(R.id.rescuePageMainContent, chatRoomPageFrag);
        fragmentTransaction.add(R.id.rescuePageMainContent, requestListPageFrag);
//        fragmentTransaction.add(R.id.rescuePageMainContent, busPageFrag);
//        fragmentTransaction.add(R.id.rescuePageMainContent, trainPageFrag);
        fragmentTransaction.show(mapPageFrag);
        fragmentTransaction.hide(chatRoomPageFrag);
        fragmentTransaction.hide(requestListPageFrag);
//        fragmentTransaction.hide(busPageFrag);
//        fragmentTransaction.hide(trainPageFrag);
        fragmentTransaction.commit();

        //Initiate buttons
        mapButton = getView().findViewById(R.id.mapButton);
//        trainButton = getView().findViewById(R.id.trainButton);
//        busButton = getView().findViewById(R.id.busButton);
        instrucButton = getView().findViewById(R.id.instructionButton);
        listButton = getView().findViewById(R.id.requestListButton);
        chatButton = getView().findViewById(R.id.discussButton);
        mapButton.setOnClickListener(this);
        listButton.setOnClickListener(this);
        chatButton.setOnClickListener(this);
//        trainButton.setOnClickListener(this);
//        busButton.setOnClickListener(this);
        instrucButton.setOnClickListener(this);

        mainPageContent = getView().findViewById(R.id.rescuePageMainContent);
        currentLine = getView().findViewById(R.id.mapLine);

    }

    private void fragmentSwitch(Fragment fragment){
        fragmentManager = getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currentFragment);
        fragmentTransaction.show(fragment);
        currentFragment = fragment;
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.mapButton:
                mapPageFrag.removeKmlLayer();
                if (!isMap) {
                    fragmentSwitch(mapPageFrag);
                    isMap = true;
                }
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.mapLine);
                currentLine.setVisibility(View.VISIBLE);
                EventController.getEventController().getWeather();
                break;
            case R.id.requestListButton:
                isMap = false;
                fragmentSwitch(requestListPageFrag);
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.requestLine);
                currentLine.setVisibility(View.VISIBLE);
                break;
            case R.id.discussButton:
                isMap = false;
                fragmentSwitch(chatRoomPageFrag);
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.discussLine);
                currentLine.setVisibility(View.VISIBLE);
                if (EventController.getEventController().getAcceptEvent() == null)
                    chatRoomPageFrag.notAccept();
                else
                    chatRoomPageFrag.hasAccept();
                break;
//            case R.id.trainButton:
//                isMap = false;
//                fragmentSwitch(trainPageFrag);
//                currentLine.setVisibility(View.GONE);
//                currentLine = getView().findViewById(R.id.trainLine);
//                currentLine.setVisibility(View.VISIBLE);

//                break;
//            case R.id.busButton:
//                isMap = false;
//                fragmentSwitch(busPageFrag);
//                currentLine.setVisibility(View.GONE);
//                currentLine = getView().findViewById(R.id.busLine);
//                currentLine.setVisibility(View.VISIBLE);
//                break;
            case R.id.instructionButton:
                mapPageFrag.setKmlLayer();
                if (!isMap) {
                    fragmentSwitch(mapPageFrag);
                    isMap = true;
                }
                currentLine.setVisibility(View.GONE);
                currentLine = getView().findViewById(R.id.instructionLine);
                currentLine.setVisibility(View.VISIBLE);
                break;
        }

    }

    public void newMessage(String name, String message){
        chatRoomPageFrag.newMessage(name, message);
    }


}
