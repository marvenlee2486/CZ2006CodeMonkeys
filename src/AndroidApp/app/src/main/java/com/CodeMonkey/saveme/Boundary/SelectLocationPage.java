package com.CodeMonkey.saveme.Boundary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.CodeMonkey.saveme.Controller.TCPManager;
import com.CodeMonkey.saveme.Controller.UserController;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.LocationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class SelectLocationPage extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback, View.OnClickListener{

    private GoogleMap mMap;
    private String type;
    private double latitude = 0;
    private double longitude = 0;
    private boolean flag = false;
    private Button back;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_location_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.selectLocationMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        back = findViewById(R.id.backButton);
        next = findViewById(R.id.nextButton);

        back.setOnClickListener(this);
        next.setOnClickListener(this);

        Toast.makeText(this, "Please tap on map to select your " + type, Toast.LENGTH_LONG).show();
        mMap = googleMap;

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(String.format("%.4f", latLng.latitude) + " : " + String.format("%.4f", latLng.longitude));
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                        flag = true;
                        mMap.clear();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                        mMap.addMarker(markerOptions);

                    }
                });


                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(SelectLocationPage.this);
                mMap.setOnMyLocationClickListener(SelectLocationPage.this);

            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextButton:
                if (flag){
                    Intent intent = new Intent();
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    setResult(0, intent);
                    finish();
                }
                else{
                    Toast.makeText(this, "You have not selected location!", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
