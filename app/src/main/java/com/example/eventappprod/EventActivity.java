package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView mainImageView;
    TextView title, description;

    TextView Date, StartTime, EndTime, Location;
    MapView GeoTag;
    GoogleMap map;

    String data1, data2;
    String sTime, eTime, loca, date, image;

    int myImage;
    DatabaseReference ref;
    FirebaseDatabase database;

    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ref = database.getInstance().getReference("/EVENT");


        //Make references that connect the XML <--> the Java variables
        mainImageView = findViewById(R.id.eventFormImageView);
        title = findViewById(R.id.eventFormName);
        description = findViewById(R.id.eventFormDescription);

        Date = findViewById(R.id.tvDate);
        StartTime = findViewById(R.id.tvStart);
        EndTime = findViewById(R.id.tvEnd);
        Location = findViewById(R.id.tvLocation);

        //Initialize these methods
        getData();
        //setData();


    }

    //This is what will check and grab the information and load it in
    private void getData(){
        if(getIntent().hasExtra("images") && getIntent().hasExtra("data1") && getIntent().hasExtra("data2")){
            data1 = getIntent().getStringExtra("data1");

            // make sure event name is not null
            if (data1=="" ) return;

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // Toast.makeText(EventActivity.this, "Debug purpose", Toast.LENGTH_SHORT).show();

                        Event myevent = (Event) dataSnapshot.child(data1).getValue(Event.class);
                        if (myevent!=null) {
                            date = myevent.getDate();
                            sTime = myevent.getStartTime();
                            eTime = myevent.getEndTime();
                            loca = myevent.getLocation();
                            image = myevent.getImage();
                        }

                    // after retrieving all data, then set data to the TextViews
                    setData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            data2 = getIntent().getStringExtra("data2");
            //myImage = getIntent().getIntExtra("images", 1);
        }
        else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

    }

    //This is actually what displays the information (pushing the data from the intent --> variables from XML)
    private void setData(){
        title.setText(data1);
        description.setText(data2);
        //mainImageView.setImageResource(myImage);
        //mainImageView.setImageURI(Uri.parse(image));
        //Picasso.get().load(image).into(mainImageView);
        Glide.with(EventActivity.this).load(image).into(mainImageView);


        Date.setText(date);
        StartTime.setText(sTime);
        EndTime.setText(eTime);
        Location.setText(loca);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
    }
}
