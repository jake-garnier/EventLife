package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;


public class CreateEventActivity extends AppCompatActivity {
    EditText Name, Location, StartTime, EndTime, Date, Tag, Description;
    Button Cancel;
    Button Next;

    //DatabaseReference ref;
    FirebaseDatabase database;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //
        // create instance variable from front-end objects
        //
        Name = (EditText) findViewById(R.id.createEventTvName);
        Location = (EditText) findViewById(R.id.createEventTvLocation);
        Date = (EditText) findViewById(R.id.createEventTvDate);
        StartTime = (EditText) findViewById(R.id.createEventTvStartTime);
        EndTime = (EditText) findViewById(R.id.createEventTvEndTime);
//        Tag = (EditText) findViewById(R.id.Tag);
        Next = (Button) findViewById(R.id.btnPrevious);
        Cancel = (Button) findViewById(R.id.btnCancel);
        Description =(EditText) findViewById(R.id.createEventTvDescription);



        //
        // create event button clicked
        //
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                // transfer all data from CreateEventEactivity  to CreateEventPage2
                //
                Intent intent = new Intent(CreateEventActivity.this, CreateEventPage2.class);
                intent.putExtra("iName", Name.getText().toString());
                intent.putExtra("iLocation", Location.getText().toString());
                intent.putExtra("iDay", Date.getText().toString());
                intent.putExtra("iStartTime", StartTime.getText().toString());
                intent.putExtra("iEndTime", EndTime.getText().toString());
                intent.putExtra("iDescription2",Description.getText().toString());
                intent.putExtra("iTag", Name.getText().toString());

                // go to CreateEventPage2
                startActivity(intent);
            }
        });

        //
        // user changes his mind, click CANCEL, then go back to DashBoard page
        //
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });
    }

}
