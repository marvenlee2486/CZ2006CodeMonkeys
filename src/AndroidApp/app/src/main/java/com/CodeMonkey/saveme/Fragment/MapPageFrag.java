package com.CodeMonkey.saveme.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.CodeMonkey.saveme.Controller.EventController;
import com.CodeMonkey.saveme.Entity.Event;
import com.CodeMonkey.saveme.R;
import com.CodeMonkey.saveme.Util.LocationUtils;
import com.CodeMonkey.saveme.Util.NotificationUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MapPageFrag extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

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
                Location location = LocationUtils.getBestLocation(getContext(), null);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
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
            }
        });


        return view;
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
        Event event = EventController.getEventController().getEventList().get(phoneNumber);
        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(event.getUser().getName())
                        .snippet(getEventInfo(event)));
        setMarkerAdapter();
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
        setMarkerAdapter();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        markers.get(phoneNumber).showInfoWindow();
    }

    private String getEventInfo(Event event){
        String result = "";
        result += ("Phone number: " + event.getUser().getPhoneNumber() + "\n");
        result += ("Currently " + event.getRescueNumber() + " volunteers are coming\n");
        String[] addressLocation = event.getUser().getHomeLocation().split(",");
        if (Math.abs(Double.parseDouble(addressLocation[0]) - event.getLatitude() )+ Math.abs(Double.parseDouble(addressLocation[1]) - event.getLongitude()) < 0.001){
            result += "User is now near his/her home!\n";
            result += "Home address: " + event.getUser().getHomeAddress() + "\n";
        }
        else {
            addressLocation = event.getUser().getWorkLocation().split(",");
            if (Math.abs(Double.parseDouble(addressLocation[0]) - event.getLatitude()) + Math.abs(Double.parseDouble(addressLocation[1]) - event.getLatitude()) < 0.001) {
                result += "User is now near his/her work place!\n";
                result += "Work address: " + event.getUser().getWorkAddress() + "\n";
            }
        }
        if (event.getUser().getAge() != null && !event.getUser().getAge().equals(""))
            result += "Age: " + event.getUser().getAge() + "\n";
        if (event.getUser().getEmergencyContactName() != null && !event.getUser().getEmergencyContactName().equals(""))
            result += "Emergency contact name: " + event.getUser().getEmergencyContactName() + "\n";
        if (event.getUser().getEmergencyContactNumber() != null && !event.getUser().getEmergencyContactNumber().equals(""))
            result += "Emergency contact phone number: " + event.getUser().getEmergencyContactNumber() + "\n";
        result += "Temperature: " + event.getTemperature() + "℃\n";
        result += "Humidity: " + event.getHumidity() + "%\n";
        if (event.getHumidity() < 50)
            result += "Very low probably rain";
        else if (event.getHumidity() >= 50 && event.getHumidity() < 80)
            result += "Low probably rain";
        else if (event.getHumidity() >= 80 && event.getHumidity() < 100)
            result += "High probably rain";
        else if (event.getHumidity() == 100)
            result += "Raining";
        return result;
    }

    private void setMarkerAdapter(){
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

    }

    public void updateRescueNumber(Event event){
        markers.get(event.getUser().getPhoneNumber()).remove();
        addNewMarker(event.getUser().getPhoneNumber());
    }


}
