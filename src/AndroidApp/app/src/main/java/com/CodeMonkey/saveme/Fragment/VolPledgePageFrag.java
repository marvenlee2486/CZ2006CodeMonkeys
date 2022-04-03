package com.CodeMonkey.saveme.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Boundary.MainPage;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.Entity.User;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.RequestUtil;

import rx.Observer;

/***
 * RegVolPageFrag created by Wang Tianyu 12/03/2022
 * Page for user to confirm registering as a volunteer
 */
public class VolPledgePageFrag extends Fragment {

    private Button button;
    private Button regButton;

    public VolPledgePageFrag(Button regButton){
        this.regButton = regButton;
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.vol_pledge_page_frag, container, false);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserController.getUserController().getUser().setIsVolunteer("PLEDGED");
                RequestUtil.putUserData(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error", e.toString());
                    }

                    @Override
                    public void onNext(User user) {
                        Log.i("OK", "Update success");
                        UserController.getUserController().getUser().setIsVolunteer("PLEDGED");
                        Toast.makeText(getContext(), "You are now a real volunteer!", Toast.LENGTH_SHORT).show();
                        regButton.performClick();
                    }
                },UserController.getUserController().getUser(), UserController.getUserController().getToken());
            }
        });
        return view;
    }
}
