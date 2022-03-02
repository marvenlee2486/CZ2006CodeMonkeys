package com.CodeMonkey.saveme.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.R;

/***
 * RegSignPage created by Wang Tianyu 14/02/2022
 * Page for configs
 */
public class ConfigPageFrag extends Fragment {

    public ConfigPageFrag(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.config_page_fragment, container, false);
        return view;
    }
}