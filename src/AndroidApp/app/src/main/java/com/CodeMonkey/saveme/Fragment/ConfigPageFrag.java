package com.CodeMonkey.saveme.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Boundary.*;
import com.CodeMonkey.saveme.R;

public class ConfigPageFrag extends Fragment {

    public ConfigPageFrag(){}

    private Context context;
    private Button userProfileButton;
    private Button helpAndFeedbackButton;
    private Button AchievementsProfileButton;
    private Button ChangeLanguageButton;
    private AlertDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.config_page_fragment, container, false);
        context = container.getContext();

        userProfileButton = view.findViewById(R.id.btnuserProfile);
        helpAndFeedbackButton = view.findViewById(R.id.btnHelpFeedback);
        AchievementsProfileButton = view.findViewById(R.id.btnAchiev);
        ChangeLanguageButton = view.findViewById(R.id.btnLanguage);

        ChangeLanguageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showChangeLanguageModal();
            }
        });

        userProfileButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), UserProfilePage.class);
                startActivity(intent);
            }
        });


        helpAndFeedbackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), FAQPage.class);
                startActivity(intent);
            }
        });

        AchievementsProfileButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), AchievementsPage.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void showChangeLanguageModal(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(new String[]{"English", "简体中文"},
                context.getSharedPreferences("language", Context.MODE_PRIVATE).getInt("language", 0),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences preferences = context.getSharedPreferences("language", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("language", i);
                        editor.apply();
                        dialog.dismiss();

                        // Re Render main page
                        Intent intent = new Intent(getActivity(), TestActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        dialog = builder.create();
        dialog.show();
    }

}
