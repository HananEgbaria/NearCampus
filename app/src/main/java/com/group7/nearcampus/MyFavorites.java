package com.group7.nearcampus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyFavorites extends AppCompatActivity {

    private ModelLocal _modelLocal;
    private ListView myListView;
    public static ArrayList<Place> PlacesList = new ArrayList<Place>();
    private CustomAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // this is to display up button in action bar

        _modelLocal=ModelLocal.getInstance(this);
        PlacesList = _modelLocal.getAllPlaces();
        myListView=(ListView)findViewById(R.id.listView2);
        adapter=new CustomAdapter();
        myListView.setAdapter(adapter);
        registerForContextMenu(myListView);

        final Intent viewPlace =new Intent(this, Container.class);
        myListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                viewPlace.putExtra("Position", i);
                startActivity(viewPlace);

            }
        });

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        PlacesList= _modelLocal.getAllPlaces();
        myListView.setAdapter(adapter);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.popup_menu_list, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int position = (int) info.id;
                Log.d("HY", "position=" + position);
                Place MyPlace= PlacesList.get(position);
                _modelLocal.deletePlace(MyPlace.GetPhone());
                PlacesList.clear();
                PlacesList= _modelLocal.getAllPlaces();
                adapter.notifyDataSetChanged();

                return true;
        }
        return super.onContextItemSelected(item);
    }




    private class CustomAdapter  extends BaseAdapter
    {

        private LayoutInflater inflater;

        public CustomAdapter()
        {

            inflater = LayoutInflater.from(getApplicationContext());


        }

        @Override
        public int getCount()
        {
            return PlacesList.size();
        }

        @Override
        public Object getItem(int arg0)
        {
            return PlacesList.get(arg0);
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

            Place _Place = PlacesList.get(location);

            TextView name = (TextView) convertView.findViewById(R.id.PlaceNameView);
            TextView phone = (TextView) convertView.findViewById(R.id.PhoneView2);
            TextView distance = (TextView) convertView.findViewById(R.id.textViewDistance);
            final RatingBar ratingBar1= (RatingBar) convertView.findViewById(R.id.ratingBarInPlaesList);


            name.setText( _Place.GetName());
            phone.setText(_Place.GetPhone());
            distance.setVisibility(View.GONE);
            ratingBar1.setVisibility(View.GONE);

            return convertView;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.menu_favorites);
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.menu_share);
        item2.setVisible(false);
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
