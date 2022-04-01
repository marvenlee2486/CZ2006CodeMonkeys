package com.CodeMonkey.saveme.Boundary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.CodeMonkey.saveme.R;


/***
 * RegSignPage created by Wang Tianyu 13/02/2022
 * The page that asks users to give permission to location service
 */
public class LocaServPage extends BaseActivity {

    private Button allowButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loca_serv_page);
        allowButton = findViewById(R.id.allowButton);
        allowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPermission();
            }
        });
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else{
                Intent intent = new Intent(LocaServPage.this, RegSignPage.class);
                intent.putExtra("type", "common");
                finishAll();
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                initPermission();
            else{
                Intent intent = new Intent(LocaServPage.this, RegSignPage.class);
                intent.putExtra("type", "common");
                finish();
                startActivity(intent);
            }
        }
    }
}
