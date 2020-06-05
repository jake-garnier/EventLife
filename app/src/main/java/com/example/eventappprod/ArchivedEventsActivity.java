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

    ArrayList<String> images_Firestore = new ArrayList<>();
    ArrayList<String> eventNames_Screenshow = new ArrayList<>();
    ArrayList<String> eventStartTime_Screenshow=new ArrayList<>();
    ArrayList<String> eventEndTime_Screenshow=new ArrayList<>();
    ArrayList<String> eventDate_Screenshow=new ArrayList<>();
    ArrayList<String> creator = new ArrayList<>();

    String[] rsvp = new String[currUser.getRSVPEvents().length()];


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

        mRecyclerView.setAdapter(mAdapter);

        ref = FirebaseDatabase.getInstance().getReference("/EVENT");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rsvp = currUser.getRSVPEvents().split(",");
                evenList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(String rsvpEvent : rsvp) {
                        if(ds.child("name").getValue().equals(rsvpEvent)) {
                            evenList.add(ds.getValue(Event.class));
                        }
                    }
                }

                retrieveData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ArchivedEventsActivity.this, "Error loading RSVP list", Toast.LENGTH_SHORT).show();
            }
        });


        //Logic for displaying the event-feed
        //Thus, somehow inject database information into these arrays?
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
        eventNames_Screenshow.clear();
        eventStartTime_Screenshow.clear();
        eventEndTime_Screenshow.clear();
        eventDate_Screenshow.clear();
        images_Firestore.clear();
        creator.clear();
        for (int i=0; i<evenList.size();i++) {
            eventNames_Screenshow.add(i, evenList.get(i).getName());
            eventStartTime_Screenshow.add(evenList.get(i).getStartTime());
            eventEndTime_Screenshow.add(evenList.get(i).getEndTime());
            eventDate_Screenshow.add(evenList.get(i).getDate());
            images_Firestore.add(evenList.get(i).getImage());
            creator.add(evenList.get(i).getOwner());
        }

        LoadDatatoRSVPEvents();

    }

    public void LoadDatatoRSVPEvents(){
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        for (int i = 0; i < evenList.size(); i++) {

            exampleList.add(new ExampleItem(eventNames_Screenshow.get(i), eventStartTime_Screenshow.get(i), eventEndTime_Screenshow.get(i), eventDate_Screenshow.get(i), creator.get(i), images_Firestore.get(i)));


        }


        mRecyclerView = findViewById(R.id.recyclerViewArchive);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, exampleList, "RSVP");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
