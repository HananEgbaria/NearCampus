package com.group7.nearcampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
/**
 * Created by majdy on 16/01/2016.
 */
public class ModelLocal {

    private DatabaseHelper myDb;

    // class member
    private static ModelLocal instance;
    // private constructor
    private ModelLocal(Context context){
        myDb = new DatabaseHelper(context);

    }
    //public accessor
    public static ModelLocal getInstance(Context context){
        if (instance == null) {
            instance = new ModelLocal(context);
        }
        return instance;
    }



    public boolean addPlace(Place rs){
        Log.d("HY", "ModelLocal addPlace" );
        ContentValues values = new ContentValues();

        values.put("name", rs.GetName());
        values.put("phone", rs.GetPhone());
        values.put("address", rs.GetAddress());
        values.put("description", rs.GetDescription());
        values.put("category", rs.GetCategory());
        values.put("openhours", rs.GetOpenHours());

        SQLiteDatabase db = myDb.getWritableDatabase();
        //Get confirmation
        long rowId = db.insert("Place", null, values);
        if (rowId <= 0) return false;
        return true;
    }


    public boolean addPlaceToRated(Place rs){
        Log.d("HY", "ModelLocal addPlace" );
        ContentValues values = new ContentValues();

        values.put("name", rs.GetName());
        values.put("phone", rs.GetPhone());
        SQLiteDatabase db = myDb.getWritableDatabase();
        //Get confirmation
        long rowId = db.insert("RatedPlaces", null, values);
        if (rowId <= 0) return false;
        return true;
    }

    public ArrayList<Place> getAllPlaces(){
        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from Place ", null);
        ArrayList<Place> list = new ArrayList<Place>();
        if (cur.moveToFirst()) {

            int nameIndex = cur.getColumnIndex("name");
            int phoneIndex = cur.getColumnIndex("phone");
            int addressIndex = cur.getColumnIndex("address");
            int descriptionIndex = cur.getColumnIndex("description");
            int categoryIndex = cur.getColumnIndex("category");
            int rateIndex = cur.getColumnIndex("rate");
            int openhoursIndex = cur.getColumnIndex("openhours");


            do{
                Place rs = new Place(cur.getString(nameIndex),cur.getString(phoneIndex),cur.getString(addressIndex),cur.getString(descriptionIndex),cur.getString(categoryIndex));
                rs.SetRating(cur.getDouble(rateIndex));
                rs.SetOpenHours(cur.getString(openhoursIndex));
                list.add(rs);
            } while (cur.moveToNext());
        }
        return list;
    }

    public Place getPlaceByPhone(String phone){
        SQLiteDatabase db = myDb.getReadableDatabase();
        String[] columns = {"name","phone","category"};
        String selection = "phone = ?" ;
        String[] args = {phone};
        Cursor cur = db.query("Place", columns, selection, args, null, null, null);
        if (cur.moveToFirst()) {

            int nameIndex = cur.getColumnIndex("name");
            int phoneIndex = cur.getColumnIndex("phone");
            Place rs = new Place(cur.getString(nameIndex),cur.getString(phoneIndex),null,null,null);

            return rs;

        }
        return null;
    }


    public boolean IsPlaceRated(String phone){
        SQLiteDatabase db = myDb.getReadableDatabase();
        String[] columns = {"name","phone"};
        String selection = "phone = ?" ;
        String[] args = {phone};
        Cursor cur = db.query("RatedPlaces", columns, selection, args, null, null, null);
        if (cur.moveToFirst()) {
            return true;

        }
        return false;
    }

    void deletePlace(String phone){
        SQLiteDatabase db = myDb.getWritableDatabase();
        db.execSQL("delete from Place where phone= '" + phone + "';" );
    }



    public boolean isItInFavorites(Place x){
        Place tmp=getPlaceByPhone(x.GetPhone());
        if(tmp==null) return false;
        else return true;

    }


    final int version = 1;

    class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context){

            super(context, "datamodel.db", null , version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table Place (name TEXT,phone TEXT PRIMARY KEY,address TEXT,description TEXT, category TEXT, rate DOUBLE, openhours TEXT);");
            db.execSQL("create table RatedPlaces (name TEXT,phone TEXT PRIMARY KEY);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS Place ;");
            db.execSQL("create table Place (name TEXT,phone TEXT PRIMARY KEY,address TEXT,description TEXT, category TEXT, rate DOUBLE, openhours TEXT);");
            db.execSQL("DROP TABLE IF EXISTS RatedPlaces ;");
            db.execSQL("create table RatedPlaces (name TEXT,phone TEXT PRIMARY KEY);");
        }


    }
}
