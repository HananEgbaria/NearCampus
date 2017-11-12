package com.group7.nearcampus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


//** This Fragment is to show Details for a place **//
public class Fragment1 extends Fragment {


    private TextView phone;
    private TextView address;
    private  TextView description;
    private  TextView ratingDescription;
    private  TextView openHours;
    private  Bundle extras;
    private Model DB;
    private ModelLocal ML;
    private Place MyPlace;
    private ImageButton CallButton;
    private TextView NumOfReviews;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_fragment1, container, false);
        DB=Model.getInstance(getActivity().getApplicationContext());
        ML=ModelLocal.getInstance(getActivity().getApplicationContext());
        extras = getActivity().getIntent().getExtras();
        final int position = extras.getInt("Position");
        int IsItPlacesListCalling = getActivity().getIntent().getIntExtra("IsItMain", 0);
        if(IsItPlacesListCalling==1)
            MyPlace=PlacesList.PList.get(position);
        else
            MyPlace=MyFavorites.PlacesList.get(position);
        CallButton=(ImageButton)rootView.findViewById(R.id.CallB);
        phone = (TextView) rootView.findViewById(R.id.PhoneView);
        address = (TextView) rootView.findViewById(R.id.AddressView);
        description = (TextView) rootView.findViewById(R.id.DescriptionView);
        ratingDescription = (TextView) rootView.findViewById(R.id.RatingDescription);
        openHours = (TextView) rootView.findViewById(R.id.openhourstxt);
        final Button saveButton=(Button)rootView.findViewById(R.id.Saveb2);
        final RatingBar ratingBar1= (RatingBar) rootView.findViewById(R.id.ratingBar);
        NumOfReviews=(TextView) rootView.findViewById(R.id.numOfReviews);

        phone.setText(MyPlace.GetPhone());
        address.setText(MyPlace.GetAddress());
        description.setText(MyPlace.GetDescription());
        openHours.setText("Open Hours:" + '\n' + MyPlace.GetOpenHours().replace(',','\n'));

        if(IsItPlacesListCalling==1)
        {
            ratingBar1.setRating((float) MyPlace.GetRating());
            String s= String.valueOf(MyPlace.GetNumOfRaters());
            NumOfReviews.setText("(" + s + " Reviews)");
        }
        else{
            ratingBar1.setVisibility(View.GONE);
            NumOfReviews.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
            ratingDescription.setVisibility(View.GONE);
        }


        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MyPlace.SetRating((double) ratingBar1.getRating());
                DB.editPlace(MyPlace);
                ML.addPlaceToRated(MyPlace);
                Toast.makeText(getContext(), String.valueOf(MyPlace.GetRating()), Toast.LENGTH_LONG).show();
                saveButton.setVisibility(View.GONE);
                ratingDescription.setText("You have already rated this place");
                PlacesList.PList.set(position, MyPlace);
                ratingBar1.setFocusable(false);
                ratingBar1.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

            }
        });

        if(ML.IsPlaceRated(MyPlace.GetPhone())) {
            // this is to disable rating ability if the user have already rated this place
            saveButton.setVisibility(View.GONE);
            ratingBar1.setFocusable(false);
            ratingBar1.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            if(IsItPlacesListCalling==1)
            ratingDescription.setText("You have already rated this place");
        }






        CallButton.setOnClickListener(new View.OnClickListener() {

                                  @Override
                                  public void onClick(View arg0) {
                                      Log.d("ViewPlace", "trying to Call " + MyPlace.GetName());
                                      String phone = MyPlace.GetPhone();
                                      Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                      startActivity(intent);
                                      Toast.makeText(getContext(), String.valueOf("Called it!"), Toast.LENGTH_LONG).show();


                                  }
        });

        return rootView;
    }



}
