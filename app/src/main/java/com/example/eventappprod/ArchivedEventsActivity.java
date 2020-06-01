/*
    RSVP EVENTS THAT SHOW UP FOR THE USER
 */



package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ArchivedEventsActivity extends AppCompatActivity {
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String[] eventNames;
    String[] eventDescriptions;
    //add context for the app
    private Context mContext;
    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<Event> evenList;
    ArrayList<ExampleItem> exampleList;
    //get the current user
    User currUser  = User.getInstance();
    String[] array;

    String[] images_Firestore = new String[20];
    String[] eventNames_Screenshow = new String[20];
    String[] eventStartTime_Screenshow=new String[20];
    String[] eventEndTime_Screenshow=new String[20];
    String[] eventDate_Screenshow=new String[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_events);
        evenList = new ArrayList<>();
        exampleList = new ArrayList<>();
        //set context at very top
        mContext = getApplicationContext();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewArchive);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mAdapter = new ExampleAdapter(mContext, evenList, "rsvpevent");
        mRecyclerView.setAdapter(mAdapter);

        ref = FirebaseDatabase.getInstance().getReference("/EVENT");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                evenList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    evenList.add(ds.getValue(Event.class));
                }
                if (evenList.size()!=0) retrieveData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ArchivedEventsActivity.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });


        //Logic for displaying the event-feed
        //Thus, somehow inject database information into these arrays?
//        eventNames = getResources().getStringArray(R.array.eventNames_feed);
//        eventDescriptions = getResources().getStringArray(R.array.eventNames_description);
//        ArrayList<ExampleItem> exampleList = new ArrayList<>();
//        for (int i = 0; i < eventNames.length; i++) {
//            exampleList.add(new ExampleItem(images[i], eventNames[i], eventDescriptions[i],""));
//        }
//
        mRecyclerView = findViewById(R.id.recyclerViewArchive);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, exampleList, "RSVP");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem =  menu.findItem(R.id.action_search);
        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void retrieveData(){
        // fetching data to particular array
        for (int i=0; i<evenList.size();i++) {
            eventNames_Screenshow[i] = evenList.get(i).getName();
            eventStartTime_Screenshow[i] = evenList.get(i).getStartTime();
            eventEndTime_Screenshow[i] = evenList.get(i).getEndTime();
            eventDate_Screenshow[i] = evenList.get(i).getDate();
            images_Firestore[i] = evenList.get(i).getImage();
        }

        LoadDatatoRSVPEvents();

    }

    public void LoadDatatoRSVPEvents(){
        Event event = new Event();
        array = currUser.getRSVPEvents().split(",");
        for (int i = 0; i<array.length; i++) {
            for (int j = 0; j < evenList.size(); j++) {
                if (evenList.get(j).getName().equals(array[i])) {
                    event = evenList.get(j);
                    exampleList.add(new ExampleItem(event.getName(), event.getStartTime(), event.getEndTime(),
                            event.getDate(), event.getOwner() ,event.getImage()));
                }
            }
        }

        mRecyclerView = findViewById(R.id.recyclerViewArchive);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        //mAdapter = new ExampleAdapter(this, exampleList, "RSVP");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
