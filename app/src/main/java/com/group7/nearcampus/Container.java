package com.group7.nearcampus;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


//** This Class is the Container for the 2 Fragments **//
public class Container extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Place MyPlace;
    TextView PlaceName;
    ImageButton Add2Fav;
    private ModelLocal ML;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        final int position = extras.getInt("Position");
        int IsItPlacesListCalling = getIntent().getIntExtra("IsItMain", 0);
        if(IsItPlacesListCalling==1)
            MyPlace=PlacesList.PList.get(position);
        else
            MyPlace=MyFavorites.PlacesList.get(position);

        PlaceName= (TextView) findViewById(R.id.Placename);
        PlaceName.setText(MyPlace.GetName());
        ML=ModelLocal.getInstance(getApplication());
        Add2Fav=(ImageButton) findViewById(R.id.addToFavo);

            if(ML.isItInFavorites(MyPlace))
            {
                Add2Fav.setVisibility(View.GONE);
            }

        Add2Fav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("ViewPlace", "trying to add place to my favorites : " + MyPlace.GetName());
                ML.addPlace(MyPlace);
                Add2Fav.setVisibility(View.GONE);


            }


           /* @Override
            public void onClick(View v) {
                startActivity(i);
            }*/
        });


        viewPager = (ViewPager) findViewById(R.id.view2);
        viewPager.setAdapter(new MyCustomAdapter(getFragmentManager(),getApplicationContext()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
        });
    }

    private class MyCustomAdapter extends FragmentPagerAdapter {

        private String [] fragments = {"Info","Map"};
        public MyCustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(getSupportFragmentManager());
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
                default:
                    return new Fragment1();
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        public CharSequence getPageTitle(int position){
            return fragments[position];
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.menu_favorites);
        item.setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks here.
        switch (item.getItemId()) {

            case R.id.menu_share:
                String text2BeShared = MyPlace.GetName() +" - " + MyPlace.GetPhone() +". " + '\n' + "Sent using NearCampus.";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shared using NearCampus.");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text2BeShared);
                startActivity(sharingIntent);
                return true;

            case R.id.page2:
                Log.d("HY", "Menu item page2 was clicked");
                Intent i2 = new Intent(this,AboutApp.class);
                startActivity(i2);
                finish();
                return true;

            case R.id.page3:
                Log.d("HY", "Menu item page3 was clicked");
                String text2BeShared2 = "Get NearCampus on GooglePlay. " + '\n' + "All places around campus in one app !" + '\n' + "http://NearCampus.com/AppDownload";
                Intent sharingIntent2 = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent2.setType("text/plain");
                sharingIntent2.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sharingIntent2.putExtra(android.content.Intent.EXTRA_TEXT, text2BeShared2);
                startActivity(sharingIntent2);
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
