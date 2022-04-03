package com.CodeMonkey.saveme.Util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CodeMonkey.saveme.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerWindowUtil implements GoogleMap.InfoWindowAdapter {

    private Activity activity;
    private View view;

    public MarkerWindowUtil(Activity activity){
        this.activity = activity;
        view = activity.getLayoutInflater().inflate(R.layout.event_marker_info, null);

    }

    private void rendowWindowText(Marker marker, View view){
        TextView name = view.findViewById(R.id.name);
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("!!!!!!!", "112345");


            }
        });
        name.setText("!!!!!!!!");
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        rendowWindowText(marker, view);
        return view;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        rendowWindowText(marker, view);
        return view;
    }
}
