package com.CodeMonkey.saveme.Fragment;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Controller.TCPManager;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.LocationUtils;
import com.CodeMonkey.saveme.Util.RequestUtil;

import okhttp3.ResponseBody;
import rx.Observer;

/***
 * SaveMePageFrag created by Wang Tianyu 14/02/2022
 * Save me page
 */
public class SaveMePageFrag extends Fragment {

    private Button button;
    private Button pressedButton;
    private boolean pressed = false;
    private TextView pressButtonText;

    public SaveMePageFrag(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.save_page_fragment, container, false);
        button = view.findViewById(R.id.button);
        pressedButton = view.findViewById(R.id.pressedButton);
        pressButtonText = view.findViewById(R.id.pressButtonHint);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestUtil.checkValidation(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "You have used our service 5 times!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.e("test", responseBody.toString());
                        rescueMe();
                    }
                }, UserController.getUserController().getUser().getPhoneNumber(), UserController.getUserController().getToken());
            }
        });
        pressedButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cancelRescueMe();
                return false;
            }
        });
        return view;
    }

    private void rescueMe(){
        pressed = true;
        button.setVisibility(View.GONE);
        pressedButton.setVisibility(View.VISIBLE);
        pressButtonText.setText("An ambulance will be called. Signal will be sent to nearby rescuers in\n10");
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.what == 5){
                    if (pressed)
                        pressButtonText.setText("An ambulance will be called. Signal will be sent to nearby rescuers in\n" + message.obj);
                }
                else{
                    if (pressed){
                        Location location = LocationUtils.getBestLocation(getContext(), null);
                        TCPManager.getTCPManager().send("RESCUEME;" + UserController.getUserController().getUser().getPhoneNumber() + ";" + location.getLatitude() + ";" + location.getLongitude());
                        pressButtonText.setText("Signal is being broadcast.\nDon’t worry, help is on the way.");
                    }
                }
                return false;
            }
        });

        new Thread(){
            @Override
            public void run() {
                Message msg;
//                int time = 10;
//                for (; time > 0; time--){
//                    try {
//                        msg = new Message();
//                        msg.what = 5;
//                        msg.obj = time;
//                        handler.sendMessage(msg);
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                msg = new Message();
                msg.what = 6;
                msg.obj = "Completed";
                handler.sendMessage(msg);
            }
        }.run();

    }

    private void cancelRescueMe(){
        pressed = false;
        button.setVisibility(View.VISIBLE);
        pressedButton.setVisibility(View.GONE);
        pressButtonText.setText(R.string.press_to_send_sos_signal);
        TCPManager.getTCPManager().send("CANCELRESCUEME;" + UserController.getUserController().getUser().getPhoneNumber());
    }

}
