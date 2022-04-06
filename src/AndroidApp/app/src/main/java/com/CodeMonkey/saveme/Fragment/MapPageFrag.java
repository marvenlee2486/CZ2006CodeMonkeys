package com.CodeMonkey.saveme.Fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Boundary.MainPage;
import com.CodeMonkey.saveme.Controller.EventController;
import com.CodeMonkey.saveme.Entity.Event;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.LocationUtils;
import com.CodeMonkey.saveme.Util.MarkerWindowUtil;
import com.CodeMonkey.saveme.Util.NotificationUtil;
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
import java.util.HashMap;
import java.util.Map;

/***
 * MapPageFrag created by Wang Tianyu 07/03/2022
 * Map information page
 */

public class MapPageFrag extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener
                                                    , GoogleMap.OnInfoWindowClickListener{

    private Location gps;
    private Context context;
    private boolean isEvent = false;
    private LatLng latLng;
    private KmlLayer kmlLayer;
    private boolean kmlLayerVisibility = false;
    private GoogleMap map;
    private Map<String, Marker> markers = new HashMap<>();
    private ArrayList<String> tempPhones = new ArrayList<>();



    public MapPageFrag(Context context){
        this.context = context;

}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gps = LocationUtils.getBestLocation(context, null);
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

                if (tempPhones.size() != 0){
                    for (String phoneNumber: tempPhones)
                        addNewMarker(phoneNumber);
                    tempPhones = new ArrayList<>();
                }
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationButtonClickListener(MapPageFrag.this);
                googleMap.setOnMyLocationClickListener(MapPageFrag.this);
                googleMap.setOnInfoWindowClickListener(MapPageFrag.this);
                try {
                    kmlLayer= new KmlLayer(googleMap, R.raw.aed_locations, context);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Log.e("!!!", "!!!!!");
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
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


    private void addNewMarker(String phoneNumber){
        LatLng latLng = new LatLng(EventController.getEventController().getEventList().get(phoneNumber).getLatitude(),
                EventController.getEventController().getEventList().get(phoneNumber).getLongitude());
        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(phoneNumber)
                        .snippet(EventController.getEventController().getEventList().get(phoneNumber).getUser().getName()));
        marker.showInfoWindow();
        markers.put(phoneNumber, marker);
//        map.setInfoWindowAdapter(new MarkerWindowUtil(getActivity()));
    }

    private void removeMarker(String phoneNumber){
        markers.remove(phoneNumber);
    }

    public void newEvent(String phoneNumber){
        NotificationUtil.createNotification((Activity) context, "Help!", "Someone need help!");
        isEvent = true;
        if (map != null)
            addNewMarker(phoneNumber);
        else
            tempPhones.add(phoneNumber);
    }

    public void removeEvent(String phoneNumber){
        removeMarker(phoneNumber);
        if (EventController.getEventController().getEventList().size() == 0)
            isEvent = false;
    }

    public void moveToEventMarker(String phoneNumber){
        Event event = EventController.getEventController().getEventList().get(phoneNumber);
        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        markers.get(phoneNumber).showInfoWindow();
    }


}
