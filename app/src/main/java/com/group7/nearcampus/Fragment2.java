package com.group7.nearcampus;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

//** This is to show map for a place **//
public class Fragment2 extends Fragment {


    private MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_fragment2, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        LatLng BGU = new LatLng(31.262669, 34.803277);
        googleMap.addMarker(new MarkerOptions().position(BGU).snippet("").title("BGU").icon(BitmapDescriptorFactory.fromResource(R.drawable.bgu)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(BGU));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(BGU, 14));

        Bundle extras = getActivity().getIntent().getExtras();
        final int position = extras.getInt("Position");
        int IsItPlacesListCalling = getActivity().getIntent().getIntExtra("IsItMain", 0);

        Place MyPlace;
        if(IsItPlacesListCalling==1)
            MyPlace=PlacesList.PList.get(position);
        else
            MyPlace=MyFavorites.PlacesList.get(position);

        String AddressA = MyPlace.GetAddress();
        LatLng PlaceLocation = getLocationFromAddress(getContext(), AddressA);
        if(PlaceLocation!=null) {
            googleMap.addMarker(new MarkerOptions().position(PlaceLocation).title(MyPlace.GetName())).showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(PlaceLocation));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(PlaceLocation, 14));
        }

        return rootView;
    }



    // this function take address string and return object of LatLng type
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }



}

