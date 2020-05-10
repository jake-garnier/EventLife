package com.example.eventappprod;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import android.os.Bundle;

import java.util.Map;

import static com.squareup.picasso.Picasso.*;


public class CreateEventPage2 extends AppCompatActivity {
    EditText Description;
    ImageButton chooseImage;
    MapView GeoTag;
    Button Create;
    Button Cancel;
    Button Previous;
    FirebaseDatabase database;
    DatabaseReference ref;
    Event event;
    public Uri ImageUri;
    StorageReference imagePath;

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

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechooser();
            }
        });


        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
            }
        });

        // cancel event clicked
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });


        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addEvent();
                Toast.makeText(CreateEventPage2.this, "Event created", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });
    }
    public void openFilechooser(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            ImageUri = data.getData();
            chooseImage.setImageURI(ImageUri);

        }
    }

    public void addEvent(){
        CreateEventActivity page1 = new CreateEventActivity();
        String eventname = page1.Name.getText().toString();
        String day = page1.Day.getText().toString();
        String location = page1.Location.getText().toString();
        String starttime = page1.StartTime.getText().toString();
        String endtime = page1.EndTime.getText().toString();
        String tag = page1.Tag.getText().toString();
        String description = Description.getText().toString();

        if (!TextUtils.isEmpty(eventname) && !TextUtils.isEmpty(day) && !TextUtils.isEmpty(location) &&
                !TextUtils.isEmpty(starttime) && !TextUtils.isEmpty(endtime) && !TextUtils.isEmpty(tag)
                && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(tag)) {
            String id = ref.push().getKey();
            Event event = new Event(id, eventname, day, location, starttime, endtime, tag, description);
            //Event event = new Event(id, eventname, day, location, starttime, endtime, tag);
            ref.child(id).setValue(event);

            //page1.Name.setText("");
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
        } else {
            Toast.makeText(CreateEventPage2.this, "Please fill all info", Toast.LENGTH_LONG).show();
        }




    }
}
