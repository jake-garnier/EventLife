package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends AppCompatActivity {

    ImageView mainImageView;
    TextView title, description;

    String data1, data2;
    int myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //Make references that connect the XML <--> the Java variables
        mainImageView = findViewById(R.id.mainImageView);
        title = findViewById(R.id.event_title);
        description = findViewById(R.id.event_description);

        //Initialize these methods
        getData();
        setData();

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

}
