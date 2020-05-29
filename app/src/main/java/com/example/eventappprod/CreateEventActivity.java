package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CreateEventActivity extends AppCompatActivity {
    EditText Name, Location, StartTime, EndTime, Date, Description;
    Button Create;
    String RealTimeImagePath;

    ImageButton chooseImage;
    Event event;
    Uri uri;
    StorageReference imagePath;
    FirebaseStorage storage;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //
        // create instance variable from front-end objects
        //
        chooseImage = (ImageButton) findViewById(R.id.btnImage);
        Name = (EditText) findViewById(R.id.createEventTvName);
        Location = (EditText) findViewById(R.id.createEventTvLocation);
        Date = (EditText) findViewById(R.id.createEventTvDate);
        StartTime = (EditText) findViewById(R.id.createEventTvStartTime);
        EndTime = (EditText) findViewById(R.id.createEventTvEndTime);
        Create = (Button) findViewById(R.id.btnCreateEvent);
        Description =(EditText) findViewById(R.id.createEventTvDescription);

        ref =FirebaseDatabase.getInstance().getReference("/EVENT");


        //
        // create event button clicked
        //
//        Next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //
//                // transfer all data from CreateEventEactivity  to CreateEventPage2
//                //
//                Intent intent = new Intent(CreateEventActivity.this, CreateEventPage2.class);
//                intent.putExtra("iName", Name.getText().toString());
//                intent.putExtra("iLocation", Location.getText().toString());
//                intent.putExtra("iDay", Date.getText().toString());
//                intent.putExtra("iStartTime", StartTime.getText().toString());
//                intent.putExtra("iEndTime", EndTime.getText().toString());
//                intent.putExtra("iDescription2",Description.getText().toString());
//                intent.putExtra("iTag", Name.getText().toString());
//
//                // go to CreateEventPage2
//                startActivity(intent);
//            }
//        });

        //
        // user changes his mind, click CANCEL, then go back to DashBoard page
        //
//        Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), DashBoard.class));
//            }
//        });

        StartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String addZero = "";
                        if(selectedMinute < 10)
                            addZero = "0";

                        String _24HourTime = selectedHour + ":" + addZero + selectedMinute;
                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                        String date = "";
                        try {
                            java.util.Date _24HourDt = _24HourSDF.parse(_24HourTime);
                            date = _12HourSDF.format(_24HourDt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date.substring(0,1).equals("0"))
                            date = date.substring(1);

                        StartTime.setText(date);
                    }
                }, hour, minute, false);
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        EndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEventActivity.this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String addZero = "";
                        if(selectedMinute < 10)
                            addZero = "0";

                        String _24HourTime = selectedHour + ":" + addZero + selectedMinute;
                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                        String date = "";
                        try {
                            java.util.Date _24HourDt = _24HourSDF.parse(_24HourTime);
                            date = _12HourSDF.format(_24HourDt);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(date.substring(0,1).equals("0"))
                            date = date.substring(1);

                        EndTime.setText(date);
                    }
                }, hour, minute, false);
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechooser();
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RealTimeImagePath.isEmpty())
                {
                    Toast.makeText(CreateEventActivity.this, "Debug adding purpose", Toast.LENGTH_LONG).show();
                    addEvent();
                    Toast.makeText(CreateEventActivity.this, "Event created", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), DashBoard.class));
                }
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
        String Des = bn.getStringExtra("iDescription2");

        String id = ref.push().getKey();

        //
        // check if all instances filled
        //
        if (!TextUtils.isEmpty(EventName) && !TextUtils.isEmpty(Day) && !TextUtils.isEmpty(Location) &&
                !TextUtils.isEmpty(StartTime) && !TextUtils.isEmpty(EndTime) && !TextUtils.isEmpty(Tag)
                && !TextUtils.isEmpty(Des)) {
            // make an event object with contructor
            //event = new Event(id, EventName, Day, Location, StartTime, EndTime, Tag, Des);
            event = new Event(id, EventName, Day, Location, StartTime, EndTime, Tag, Des, "", "", "");


            // store image uploaded to the event object
            // event.setImage(imagePath.toString());
            event.setImage(RealTimeImagePath);

            // make an event with the event's name
            ref.child(event.getName()).setValue(event);

            // back to Dashboard
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
        }

        // if one of the field is empty, prompt the user to input data again
        else {
            Toast.makeText(CreateEventActivity.this, "Please fill all the necessary info ", Toast.LENGTH_LONG).show();
        }

    }

    //
    // select an image in the user phone's Gallery
    //
    public void openFilechooser(){
        // create an intent so user can jump to his phone's folder to select photo
        Intent intent = new Intent(Intent.ACTION_PICK);
        // only pick image
        intent.setType("image/*");
        // grab the photo
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

            String url = uri.toString();
            String filename = url.substring(url.lastIndexOf("/")+1);

            imagePath = FirebaseStorage.getInstance().getReference().child("/EVENT").child(filename);
            //  put the picture to put in Image box

            imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                // if upload success, print message
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateEventActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                    Task<Uri> downloadUrl = imagePath.getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            RealTimeImagePath = uri.toString();
                            Create.setEnabled(true);
                        }
                    });

                }
                // if upload fails, print message
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateEventActivity.this, "Not Uploaded" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

}
