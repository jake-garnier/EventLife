package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.os.Bundle;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.squareup.picasso.Picasso.*;


public class CreateEventPage2 extends AppCompatActivity implements OnMapReadyCallback {

    // Declare instance variables
    EditText Description;
    ImageButton chooseImage;
    MapView GeoTag;
    Button Create;
    Button Cancel;
    Button Previous;
    FirebaseDatabase database;
    DatabaseReference ref;
    Event event;
    Uri uri;
    StorageReference imagePath;
    FirebaseStorage  storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_page2);

        Description = (EditText) findViewById(R.id.Description);
        chooseImage = (ImageButton) findViewById(R.id.btnImage);
        GeoTag = (MapView) findViewById(R.id.mapView);

        Create = (Button) findViewById(R.id.btnCreate);
        Previous = (Button) findViewById(R.id.btnPrevious);
        Cancel = (Button) findViewById(R.id.btnCancel);
        ref = database.getInstance().getReference("EVENT");


        GeoTag = findViewById(R.id.mapView);
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


        //storageReference = FirebaseStorage.getInstance().getReference();
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechooser();
            }
        });

        //
        // User wants to change his event's info, go to the previous page
        //
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
            }
        });

        //
        // User changes his mind, click on CANCEL button
        //
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });

        //
        // User wants to create an event, click on CREATE button
        //
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
                Toast.makeText(CreateEventPage2.this, "Event created", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });
    }


    public void addEvent(){

        //
        // This block is to grab all the data from CreateEventActivity page
        //
        Intent bn = getIntent();
        String EventName = bn.getStringExtra("iName");
        String Location = bn.getStringExtra("iLocation");
        String Day = bn.getStringExtra("iDay");
        String StartTime = bn.getStringExtra("iStartTime");
        String EndTime = bn.getStringExtra("iEndTime");
        String Tag = bn.getStringExtra("iTag");

        // data in CreateEventPage2
        String Des = Description.getText().toString();
        String id = ref.push().getKey();

        //
        // check if all instances filled
        //
        if (!TextUtils.isEmpty(EventName) && !TextUtils.isEmpty(Day) && !TextUtils.isEmpty(Location) &&
                !TextUtils.isEmpty(StartTime) && !TextUtils.isEmpty(EndTime) && !TextUtils.isEmpty(Tag)
                && !TextUtils.isEmpty(Des)) {
            // make an event object with contructor
            event = new Event(id, EventName, Day, Location, StartTime, EndTime, Tag, Des);

            // store image uploaded to the event object
            event.setImage(imagePath.toString());

            // make an event with an unique id
            //ref.child(event.getId()).setValue(event);
            ref.child(event.getName()).setValue(event);

            // start the next page
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }

        // if one of the field is empty, prompt the user to input data again
        else {
            Toast.makeText(CreateEventPage2.this, "Please fill all the necessary info ", Toast.LENGTH_LONG).show();
        }

    }

    //
    // select an image in the user phone's Gallery
    //
    public void openFilechooser(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        // only pick image
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    //
    // get the result of choosing picture in Gallery
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            // get the data for picture chosen
            uri = data.getData();
            // set the chooseImage by the picture chosen
            chooseImage.setImageURI(uri);
            // assign the imagePath by using uri
            imagePath = FirebaseStorage.getInstance().getReference().child("EVENT").child(uri.getLastPathSegment());
            //  put the picture to put in Image box
            imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                // if upload success, print message
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateEventPage2.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
                // if upload fails, print message
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateEventPage2.this, "Not Uploaded" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // display the pic
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double process = (120.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                }
            });

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
