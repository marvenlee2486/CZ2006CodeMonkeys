package com.CodeMonkey.saveme.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Boundary.*;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;

public class ConfigPageFrag extends Fragment {

    public ConfigPageFrag(){}

    private Context context;
    private Button userProfileButton;
    private Button helpAndFeedbackButton;
    private Button AchievementsProfileButton;
    private Button ChangeLanguageButton;
    private Button CertificateButton;
    private Button SignOutButton;

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
        SignOutButton = view.findViewById(R.id.btnSignOut);
        CertificateButton = view.findViewById(R.id.btnuserCertificate);

        if (UserController.getUserController().getUser().getIsVolunteer().equals("PLEDGED") || UserController.getUserController().getUser().getIsVolunteer().equals("YES"))
            CertificateButton.setVisibility(View.VISIBLE);
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

        CertificateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), CertificatePage.class);
                startActivity(intent);
            }
        });

        SignOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Amplify.Auth.signOut(
                                        AuthSignOutOptions.builder().globalSignOut(true).build(),
                                        () -> {
                                            Log.i("Auth", "Signed out successfully");
                                            getActivity().finish();
                                        },
                                        error -> Log.e("Auth", error.toString())
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
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