package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateEventActivity extends AppCompatActivity {
    EditText Name, Location, StartTime, EndTime, Day, Tag;
    Button Cancel;
    Button Next;
    FirebaseDatabase database;
    DatabaseReference ref;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        Name = (EditText) findViewById(R.id.Description);
        Location = (EditText) findViewById(R.id.Location);
        Day = (EditText) findViewById(R.id.Day);
        StartTime = (EditText) findViewById(R.id.StartTime);
        EndTime = (EditText) findViewById(R.id.EndTime);
        //Description = (EditText) findViewById(R.id.Description);
        Tag = (EditText) findViewById(R.id.Tag);
        //Create = (Button) findViewById(R.id.btnNext);
        Next = (Button) findViewById(R.id.btnNext);
        Cancel = (Button) findViewById(R.id.btnCancel);
        ref= database.getInstance().getReference("EVENT");

        // create event button clicked
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });

        // cancel event clicked
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            }
        });




    }
}
