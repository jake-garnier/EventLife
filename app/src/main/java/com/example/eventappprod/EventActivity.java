package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView mainImageView;
    TextView title, description;
    MapView GeoTag;

    String data1, data2;
    int myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //Make references that connect the XML <--> the Java variables
        mainImageView = findViewById(R.id.eventFormImageView);
        title = findViewById(R.id.eventFormName);
        description = findViewById(R.id.eventFormDescription);

        //Initialize these methods
        getData();
        setData();

        GeoTag = findViewById(R.id.eventFormMapView);
        GeoTag.onCreate(savedInstanceState);
        GeoTag.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng coordinates = new LatLng(32.8851, -117.2392);
                googleMap.addMarker(new MarkerOptions().position(coordinates).title("RIMAC"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                GeoTag.onResume();
            }
        });

    }

    //This is what will check and grab the information and load it in
    private void getData(){
        if(getIntent().hasExtra("images") && getIntent().hasExtra("data1") && getIntent().hasExtra("data2")){
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            myImage = getIntent().getIntExtra("images", 1);

        }
        else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

    }

    //This is actually what displays the information (pushing the data from the intent --> variables from XML)
    private void setData(){
        title.setText(data1);
        description.setText(data2);
        mainImageView.setImageResource(myImage);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
