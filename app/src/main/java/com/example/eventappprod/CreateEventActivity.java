package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateEventActivity extends AppCompatActivity {
    EditText Name, Location, StartTime, EndTime, Day, Tag;
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
        Name = (EditText) findViewById(R.id.Description);
        Location = (EditText) findViewById(R.id.Location);
        Day = (EditText) findViewById(R.id.Day);
        StartTime = (EditText) findViewById(R.id.StartTime);
        EndTime = (EditText) findViewById(R.id.EndTime);
        Tag = (EditText) findViewById(R.id.Tag);
        Next = (Button) findViewById(R.id.btnPrevious);
        Cancel = (Button) findViewById(R.id.btnCancel);



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
                intent.putExtra("iDay", Day.getText().toString());
                intent.putExtra("iStartTime", StartTime.getText().toString());
                intent.putExtra("iEndTime", EndTime.getText().toString());
                intent.putExtra("iTag", Tag.getText().toString());

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

