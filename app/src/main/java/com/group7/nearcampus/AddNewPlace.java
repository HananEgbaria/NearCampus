package com.group7.nearcampus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddNewPlace extends AppCompatActivity {

    private Model DB;

    EditText name;
    EditText phone;
    EditText address;
    EditText description;
    Place Rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DB=Model.getInstance(this);

        name = (EditText) findViewById(R.id.ResturantName1ed);
        phone = (EditText) findViewById(R.id.phoneEd);
        address = (EditText) findViewById(R.id.AddressEd);
        description = (EditText) findViewById(R.id.DescriptionEd);


        Button saveButton=(Button)findViewById(R.id.saveb1);


        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if ( isNameOrPhoneEmpty() )
                { Toast.makeText(AddNewPlace.this, "Please enter Name / phone number !", Toast.LENGTH_SHORT).show();}
                else {
                    final Intent i = getIntent();
                    String category = i.getExtras().getString("category");
                    Rest = new Place(name.getText().toString(), phone.getText().toString(), address.getText().toString(), description.getText().toString(), category);

                    DB.SuggestPlace(Rest);
                    Toast.makeText(AddNewPlace.this, "Thank you, we appreciate your help ! . you can suggest more places if you want. ", Toast.LENGTH_LONG).show();
                    name.setText("");
                    phone.setText("");
                    address.setText("");
                    description.setText("");
                }

            }
        });


    }



    private boolean isNameOrPhoneEmpty() {
        return ( ( phone.getText().toString().trim().length() == 0) || ( name.getText().toString().trim().length() == 0) );
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

