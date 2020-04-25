package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ArchivedEventsActivity extends AppCompatActivity {
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String eventNames[];
    String eventDescriptions[];
    int images[] = {R.drawable.revelle, R.drawable.muir, R.drawable.tmc, R.drawable.warren, R.drawable.erc, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla};

    //Recycler View Needed for Event Feed
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_events);

        //Logic for displaying the event-feed
        //Thus, somehow inject database informaiton into these arrays?
        recyclerView = findViewById(R.id.archiveEventRecycler);
        eventNames = getResources().getStringArray(R.array.eventNames_feed);
        eventDescriptions = getResources().getStringArray(R.array.eventNames_description);
        MyAdapter myAdapter = new MyAdapter(this, eventNames, eventDescriptions, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
