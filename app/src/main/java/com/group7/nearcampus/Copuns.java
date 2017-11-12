package com.group7.nearcampus;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;

public class Copuns extends AppCompatActivity {

    LoginButton loginButton;
    Button logOutButton;
    TextView txt;
    ImageView img;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_copuns);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt = (TextView) findViewById(R.id.textView);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        logOutButton = (Button) findViewById(R.id.LogOutButton);
        img = (ImageView) findViewById(R.id.imageView);
        img.setImageResource(R.drawable.locked);

        logOutButton.setVisibility(View.GONE);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginManager.getInstance().logOut(); // this is used to logout - you may make new button and run this code onclick
                logOutButton.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                txt.setText("you have logged out !");
                img.setImageResource(R.drawable.locked);


            }
        });


        // Ask for permission to get user public profile
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // your App code do somthing
                DoSomeWork(loginResult);

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });



        ProfileTracker fbProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // User logged in or changed profile
            }
        };        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {

            String name        = "Hello " + profile.getFirstName() + " " + profile.getLastName();
            // String id ="your FB id is : " + user.getString("id");
            txt.setText( name + " !" + '\n' + '\n' + "These are your coupons :");
            logOutButton.setVisibility(View.VISIBLE);
            new DownloadImageTask(img)
                    .execute("http://s2.postimg.org/bxvruqosp/coupon2.gif");
            //img.setImageResource(R.drawable.treasureopened);
            //loginButton.setVisibility(View.GONE);


        }
    }




    // this method forwards LoginResult callback to the callbackManager
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void DoSomeWork(LoginResult loginResult){

        loginButton.setVisibility(View.GONE);

        GraphRequest graphRequest   =   GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
        {
            @Override
            public void onCompleted(JSONObject user, GraphResponse response)
            {
                Log.d("JSON", "" + response.getJSONObject().toString());

                try
                {
                    String name        = "Hello " + user.getString("name");
                    txt.setText( name + " !" + '\n' + '\n' + "These are your coupons :");
                    logOutButton.setVisibility(View.VISIBLE);
                    new DownloadImageTask(img)
                            .execute("http://s2.postimg.org/bxvruqosp/coupon2.gif");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        graphRequest.executeAsync();
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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
                Intent i = new Intent(this, MyFavorites.class);
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

/*
    // this used to print Key hash for facebook app usage
    public static void printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }


    }
    */



}
