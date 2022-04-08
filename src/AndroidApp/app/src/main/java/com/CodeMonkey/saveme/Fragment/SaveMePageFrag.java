package com.CodeMonkey.saveme.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Controller.TCPManager;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.LocationUtils;
import com.CodeMonkey.saveme.Util.RequestUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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
    private int counter;

    public SaveMePageFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.save_page_fragment, container, false);
        button = view.findViewById(R.id.button);
        pressedButton = view.findViewById(R.id.pressedButton);
        pressButtonText = view.findViewById(R.id.pressButtonHint);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestUtil.checkValidation(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        counter = 10;
                        rescueMe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "You have used our service 5 times!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

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

    private void rescueMe() {
        pressed = true;
        button.setVisibility(View.GONE);
        pressedButton.setVisibility(View.VISIBLE);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.what == 5) {
                    if (pressed) {
                        counter--;
                        if (counter == 0) {
                            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            }
                            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    TCPManager.getTCPManager().send("RESCUEME;" + UserController.getUserController().getUser().getPhoneNumber() + ";" + location.getLatitude() + ";" + location.getLongitude());
                                    pressButtonText.setText("Signal is being broadcast.\nDon’t worry, help is on the way.");
                                }
                            });
                        }
                        else {
                            String string = "Signal will be sent to nearby rescuers in\n" + counter;
                            pressButtonText.setText(string);
                        }
                    }
                }
                return false;
            }
        });

        int time = 9;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);
            }
        };

        TCPManager.getTCPManager();

        for (; time >= 0; time--){
            handler.postDelayed(runnable, 1000 * (10 - time));
        }

    }

    private void cancelRescueMe(){
        pressed = false;
        button.setVisibility(View.VISIBLE);
        pressedButton.setVisibility(View.GONE);
        pressButtonText.setText(R.string.press_to_send_sos_signal);
        TCPManager.getTCPManager().send("CANCELRESCUEME;" + UserController.getUserController().getUser().getPhoneNumber());
    }

}
