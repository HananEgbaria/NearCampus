package com.group7.nearcampus;

import android.content.Context;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by majdy on 15/01/2016.
 */
public class Model {
    // class member
    private static Model instance;
    // private constructor
    private Model(Context context){
        init(context);

    }
    //public accessor
    public static Model getInstance(Context context){
        if (instance == null) {
            instance = new Model(context);
        }
        return instance;
    }

    private void init(Context context){
        Log.d("Model", "Initializing DB ");
        //Parse.initialize(context, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        Parse.initialize(context, "TKibtG32vIxB90RFCeCZ9RNFo2pT96FGXIK3t8yf", "lylZv8hGSEeaTY0p2c4apdwDUlYhOqATE4dVDouz");

        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    e.printStackTrace();

                }
            }
        });

    }



    public boolean SuggestPlace(Place rs){
        Log.d("HY", "Model addPlace" + rs);
        ParseObject newPlace=PlaceToJsonForSuggested(rs);
        try {
            newPlace.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;

    }

    public ArrayList<Place> getAllPlaces(String category){
        Log.d("HY", "Model - Getting all Places");
        ArrayList<Place> Places = new ArrayList<Place>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Place");
        query.whereEqualTo("category", category);
        try{
            List<ParseObject> objects=query.find() ;
            if(objects!=null){
                Log.d("HY", "Model - Getting all Places - done (), objects.size()=" +objects.size() );

                for(ParseObject o: objects){
                    Places.add(jsonToPlace(o));
                }
                Log.d("HY", "Model - after coversion Places.size()=" +Places.size());
            }
        }
        catch(ParseException e){
            e.printStackTrace();
            Log.e("HY", "Model - query.find() exeption"+e.toString());
        }
        Log.d("HY", "Model - Getting all Places finished" );
        return Places;
    }



    public boolean editPlace(Place rs)
    {
        final Place s1=rs;
        ParseObject PlaceToEdit=null;
        Log.d("HY", "Model.editPlace index= " +s1.GetName());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereEqualTo("phone", rs.GetPhone());
        try{
            List<ParseObject> PlaceList=query.find();
            if (PlaceList.size()>0) {
                Log.d("HY", "Model.editPlace Retrieved " + PlaceList.size() + " Places");
                PlaceToEdit=PlaceList.get(0);
                double OldRating = PlaceToEdit.getDouble("rate");
                int OldNumOfRaters = PlaceToEdit.getInt("numOfRaters");
                double NewRating = (OldRating*OldNumOfRaters + s1.GetRating())/(OldNumOfRaters+1);
                PlaceToEdit.put("numOfRaters", OldNumOfRaters+1);
                PlaceToEdit.put("rate",NewRating);
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }


        PlaceToEdit.saveInBackground();
        return true;
    }


    ////////////Helper method to covert from ParseObject to Place //////////////
    public Place jsonToPlace(ParseObject p) {
        Place s = new Place(p.getString("name"),p.getString("phone"),p.getString("address"),p.getString("description"),p.getString("category"));
        s.SetRating(p.getDouble("rate"));
        s.SetOpenHours(p.getString("openhours"));
        s.SetNumOfRaters(p.getInt("numOfRaters"));
        Log.d("HY", "Model - jsonToPlace" + s);
        return s;
    }


    ////////////Helper method to covert from Place to ParseObject , for Suggested places table  /////////////
    public ParseObject PlaceToJsonForSuggested(Place rs){
        ParseObject po = new ParseObject("Suggested");
        po.put("name", rs.GetName());
        po.put("phone", rs.GetPhone());
        po.put("address", rs.GetAddress());
        po.put("description", rs.GetDescription());
        po.put("rate", rs.GetRating());
        po.put("category", rs.GetCategory());

        return po;
    }



}