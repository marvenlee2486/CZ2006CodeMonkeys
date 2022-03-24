package com.CodeMonkey.saveme.Fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.LocationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/***
 * MapPageFrag created by Wang Tianyu 07/03/2022
 * Map information page
 */

public class MapPageFrag extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener{

    private Location gps;
    private Context context;
    private LatLng latLng;
    private KmlLayer kmlLayer;
    private boolean kmlLayerVisibility = false;
    private GoogleMap map;
    private ArrayList<Marker> markers = new ArrayList<>();


    public MapPageFrag(){ };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        gps = LocationUtils.getBestLocation(context);
        latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.map_page_fragment, container, false);
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationButtonClickListener(MapPageFrag.this);
                googleMap.setOnMyLocationClickListener(MapPageFrag.this);
                try {
                    kmlLayer= new KmlLayer(googleMap, R.raw.aed_locations, context);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setEmergencyEvent(new LatLng(latLng.latitude+0.1, latLng.longitude+0.1));
            }
        });


        return view;
    }




    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void setKmlLayer(){
        if (!kmlLayerVisibility)
            kmlLayer.addLayerToMap();
        kmlLayerVisibility = true;
    }

    public void removeKmlLayer(){
        if (kmlLayerVisibility)
            kmlLayer.removeLayerFromMap();
        kmlLayerVisibility = false;
    }

    private void setEmergencyEvent(LatLng latLng){
        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        markers.add(marker);
    }


}
