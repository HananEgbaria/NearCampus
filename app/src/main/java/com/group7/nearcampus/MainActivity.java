package com.group7.nearcampus;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {


   private ImageButton copunButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        copunButton=(ImageButton)findViewById(R.id.copuns_area);
        final Intent i=new Intent(this,Copuns.class);
        copunButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

    }

    // this is used to know what category the user have clicked
    public void OpenCategory(View v) {
        ImageButton IB = (ImageButton) v;
        Intent OpenPlacesList= new Intent(this, PlacesList.class);
        switch (IB.getId()) {
            case R.id.Coffee:
                OpenPlacesList.putExtra("Category",1);
                startActivity(OpenPlacesList);
                break;

            case R.id.SuperMarket:
                OpenPlacesList.putExtra("Category",2);
                startActivity(OpenPlacesList);
                break;

            case R.id.Food:
                OpenPlacesList.putExtra("Category",0);
                startActivity(OpenPlacesList);
                break;

            case R.id.Entertaiment:
                OpenPlacesList.putExtra("Category",4);
                startActivity(OpenPlacesList);
                break;

            case R.id.HairCut:
                OpenPlacesList.putExtra("Category",5);
                startActivity(OpenPlacesList);
                break;

            case R.id.Pharmacy:
                OpenPlacesList.putExtra("Category",3);
                startActivity(OpenPlacesList);
                break;
        }
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
                return true;

            case R.id.page2:
                Log.d("HY", "Menu item page2 was clicked");
                Intent i2 = new Intent(this,AboutApp.class);
                startActivity(i2);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
