package com.group7.nearcampus;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesList extends AppCompatActivity {

    private ListView lv;
    private Button b; // suggest new place button
    private Button sortB;
    public static ArrayList<Place> PList = new ArrayList<Place>();
    private ArrayList<LatLng> LocationsList = new ArrayList<LatLng>();
    private CustomAdapter adapter;
    private Model DB;
    private ProgressDialog progressDialog;
    String CategoryST;
    public static Context context;
    private MapView mMap;
    private GoogleMap googleMap;
    private int isItFirstTime; // this used to avoid loading data again every time we return to this activity



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//up button
        
        isItFirstTime=1;
        context = getApplicationContext();
        progressDialog = new ProgressDialog(PlacesList.this);
        mMap = (MapView)findViewById(R.id.mapViewInPlacesList);
        mMap.onCreate(savedInstanceState);
        mMap.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMap.getMap();

        LatLng BGU = new LatLng(31.262669, 34.803277);
        googleMap.addMarker(new MarkerOptions().position(BGU).snippet("").title("BGU").icon(BitmapDescriptorFactory.fromResource(R.drawable.bgu)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(BGU));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(BGU, 14));

        // get intent data
        final Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("Category");

        // what category the user have choose
        switch (position) {
            case 0:  CategoryST = "food";
                break;
            case 1:  CategoryST = "coffee";
                break;
            case 2:  CategoryST = "supermarket";
                break;
            case 3:  CategoryST = "pharmacy";
                break;
            case 4:  CategoryST = "entertainment";
                break;
            case 5:  CategoryST = "haircut";
                break;
            default: CategoryST = "Invalid";
                break;
        }


        DB=Model.getInstance(this);
        lv=(ListView)findViewById(R.id.listView);

        final Intent Intent4ViewPlace =new Intent(this, Container.class);

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                Intent4ViewPlace.putExtra("Position", i);
                Intent4ViewPlace.putExtra("IsItMain", 1);
                startActivity(Intent4ViewPlace);

            }
        });



        b=(Button)findViewById(R.id.button1);
        final Intent intent4addNewPlace=new Intent(this,AddNewPlace.class);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent4addNewPlace.putExtra("category", CategoryST);
                startActivity(intent4addNewPlace);
            }
        });

        sortB=(Button)findViewById(R.id.SortButton);
        sortB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new SortDataTask().execute();


            }
        });



    }


    @Override
    protected void onResume()
    {
        super.onResume();
        if(isItFirstTime==1) {
            new RemoteDataTask().execute();
            isItFirstTime=0;
        }


    }



    // Local class CustomAdapter for Places list
    class CustomAdapter extends BaseAdapter
    {

        private LayoutInflater inflater;

        public CustomAdapter()
        {

            inflater = LayoutInflater.from(getApplicationContext());


        }

        @Override
        public int getCount()
        {
            return PlacesList.PList.size();
        }

        @Override
        public Object getItem(int arg0)
        {
            return PList.get(arg0);
        }

        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        @Override
        public View getView(int location, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.row, parent, false);
            }

            Place MyPlace = PList.get(location);

            TextView name = (TextView) convertView.findViewById(R.id.PlaceNameView);
            TextView phone = (TextView) convertView.findViewById(R.id.PhoneView2);
            TextView distance = (TextView) convertView.findViewById(R.id.textViewDistance);
            final RatingBar ratingBar1= (RatingBar) convertView.findViewById(R.id.ratingBarInPlaesList);
            name.setText( MyPlace.GetName());
            phone.setVisibility(View.GONE);
            distance.setText( MyPlace.GetDistanceFromUni() + " m' from University"); // this take some time because of GetDistanceFromUni function

            ratingBar1.setRating((float) MyPlace.GetRating());
            ratingBar1.setFocusable(false);
            ratingBar1.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            return convertView;
        }

    }





    // Task to get data from parse and show progress dialog
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            // Set progressdialog title
            progressDialog.setTitle("Loading data, Please Wait :)");
            // Set progressdialog message
            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            PList= DB.getAllPlaces(CategoryST);
            adapter=new CustomAdapter();

            for (int i = 0; i < PList.size(); i++)
            {
                Place PlaceTmp= PList.get(i);
                String AddressA = PlaceTmp.GetAddress();
                LatLng PlaceLocation = getLocationFromAddress(PlacesList.context, AddressA);
                if(PlaceLocation!=null)
                    LocationsList.add(PlaceLocation);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){


            for (int i = 0; i < LocationsList.size(); i++)
            {
                Place PlaceTmp= PList.get(i);
                LatLng Loca= LocationsList.get(i);
                if(Loca!=null)
                    googleMap.addMarker(new MarkerOptions().position(Loca).snippet("").title(PlaceTmp.GetName()));
            }

            lv.setAdapter(adapter);
            progressDialog.dismiss();
        }
    }





    // Task to Sort data by distance and show progress dialog
    private class SortDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressDialog
            // Set progressialog title
            progressDialog.setTitle("Sorting , Please Wait :)");
            // Set progressdialog message
            progressDialog.setIndeterminate(false);
            // Show progressdialog
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            Collections.sort(PList); // sort places by distance from university
            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
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

            if (address.isEmpty()) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }














    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.menu_share);
        item.setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks here.
        switch (item.getItemId()) {

            case R.id.menu_favorites:
                Log.d("HY", "Menu item page1 was clicked");
                Intent i = new Intent(this,MyFavorites.class);
                startActivity(i);
                finish();
                return true;
            case R.id.page2:
                Log.d("HY", "Menu item page2 was clicked");
                Intent i2 = new Intent(this,AboutApp.class);
                startActivity(i2);
                finish();
                return true;

            case R.id.page3:
                Log.d("HY", "Menu item page3 was clicked");
                String text2BeShared = "Get NearCampus on GooglePlay. " + '\n' + "All places around campus in one app !" + '\n' + "http://NearCampus.com/AppDownload";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text2BeShared);
                startActivity(sharingIntent);
                return true;

            case R.id.page4:
                Log.d("HY", "Menu item page4 was clicked");
                Toast.makeText(this, "App will be on GooglePlay soon .. wait for NearCampus !", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }







}
