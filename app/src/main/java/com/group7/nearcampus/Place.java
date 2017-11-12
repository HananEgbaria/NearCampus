package com.group7.nearcampus;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by majdy on 15/01/2016.
 */
public class Place  implements Comparable<Place>
{
    private String _name;
    private String _phone;
    private String _address;
    private String _description;
    private String _category;
    private double _rate;
    private int _numOfRaters;
    private String _openHours;


    public Place(String Name,String Phone,String Address,String Description, String Category)
    {
        _name=Name;
        _phone=Phone;
        _address=Address;
        _description=Description;
        _category=Category;
        _rate=0;
        _numOfRaters=0;
        _openHours=null;
    }

    public String GetName()
    {
        return _name;
    }

    public String GetPhone()
    {
        return _phone;
    }

    public String GetAddress()
    {
        return _address;
    }

    public String GetDescription()
    {
        return _description;
    }

    public double GetRating()
    {
        return _rate;
    }

    public void SetRating(double Rate)
    {
        _rate=Rate;
    }

    public String GetOpenHours()
    {
        return _openHours;
    }

    public void SetOpenHours(String OpenHours)
    {
        _openHours=OpenHours;
    }

    public String GetCategory()
    {
        return _category;
    }

    public void SetCategory(String cat)
    {
        _category=cat;
    }

    public int GetNumOfRaters() { return _numOfRaters;}

    public  void SetNumOfRaters(int k){_numOfRaters=k;}


    public int GetDistanceFromUni()
    {
        LatLng newPlace = getLocationFromAddress(PlacesList.context,GetAddress()); // this take some time
        if(newPlace==null) return -1;

        double newLat=newPlace.latitude;
        double newLong=newPlace.longitude;
        float distance=0;
        Location UniLocation=new Location("Unilocation");
        UniLocation.setLatitude(31.262669);
        UniLocation.setLongitude(34.803277);

        Location newLocation=new Location("newlocation");
        newLocation.setLatitude(newLat);
        newLocation.setLongitude(newLong);
        distance =newLocation.distanceTo(UniLocation); // in meters

        return ((int) distance);

    }








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


    @Override
    public int compareTo(Place otherPlace) {

        if (GetDistanceFromUni() > otherPlace.GetDistanceFromUni()) {
            return 1;
        }
        else if (GetDistanceFromUni() < otherPlace.GetDistanceFromUni()) {
            return -1;
        }
        else {
            return 0;
        }

    }
}