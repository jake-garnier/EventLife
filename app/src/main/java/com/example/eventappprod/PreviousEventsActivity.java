package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PreviousEventsActivity extends AppCompatActivity {
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String eventNames[];
    String eventDescriptions[];
    int images[] = {R.drawable.revelle, R.drawable.muir, R.drawable.tmc, R.drawable.warren, R.drawable.erc, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla};

    String[] eventImages = new String[20];
    String[] eventStartTime = new String[20];
    String[] eventName= new String[20];
    //String[] userIDArr = new String[20];

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<ExampleItem> exampleList;

    //Recycler View Needed for Event Feed
    RecyclerView recyclerView;
    DatabaseReference userref;
    User user = User.getInstance();
    ArrayList<Event> eventList;
    String c;
    String[] arr = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_events);

        //Logic for displaying the event-feed
        //Thus, somehow inject database informaiton into these arrays?
        recyclerView = findViewById(R.id.prevEventRecycler);
//        eventNames = getResources().getStringArray(R.array.eventNames_feed);
//        eventDescriptions = getResources().getStringArray(R.array.eventNames_description);
//        MyAdapter myAdapter = new MyAdapter(this, eventNames, eventDescriptions, images);
//        recyclerView.setAdapter(myAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        eventList = new ArrayList<Event>();
//        userref = FirebaseDatabase.getInstance().getReference("/USER");
//        String userId = user.getEmail().substring(0, user.getEmail().indexOf("@"));
//
//        //Getting the updated user
//        userref.child(userId).child("createdEvents").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    eventList.add(ds.getValue(Event.class));
//                }
//
//                if (eventList.size()!=0) retrieveData();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(PreviousEventsActivity.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    public void retrieveData(){
//        c = user.getCreatedEvents();
//        for (int i=0; i<eventList.size();i++) {
//            eventName[i] = eventList.get(i).getName();
//            eventStartTime[i] = eventList.get(i).getStartTime();
//            //friendsList[i] = eventList.get(i).getFriendList();
//            eventImages[i] = eventList.get(i).getImage();
//        }
//        LoadDatatoCreatedEventList();
//    }
//
//
//    public void LoadDatatoCreatedEventList(){
//        arr = user.getCreatedEvents().split(",");
//        for(int i = 0; i < arr.length; i++ ) {
//            for(int j = 0; j < eventList.size(); j++) {
//                if(eventList.get(j).getName().equals(arr[i])){
//
//                    exampleList.add(0, new ExampleItem(0, eventList.get(j).getName(),
//                            eventList.get(j).getStartTime(), eventList.get(j).getImage()));
//                }
//            }
//        }
//
//        mRecyclerView = findViewById(R.id.friendListRecycler);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new ExampleAdapter(this, exampleList, "event");
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//    }

}}