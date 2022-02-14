package com.CodeMonkey.saveme.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Boundary.RegSignPage;
import com.CodeMonkey.saveme.R;

/***
 * RegSignPage created by Wang Tianyu 14/02/2022
 * Page for user to register to be a rescuer
 */
public class RegVolPageFrag extends Fragment {

    public RegVolPageFrag(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.reg_vol_page_fragment, container, false);
        return view;
    }
}
