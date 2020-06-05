/*
    CREATED EVENTS FOR THE USER
 */
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

import java.util.ArrayList;

public class PreviousEventsActivity extends AppCompatActivity {
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String eventNames[];
    String eventDescriptions[];
    int images[] = {R.drawable.revelle, R.drawable.muir, R.drawable.tmc, R.drawable.warren, R.drawable.erc, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla};

    //Recycler View Needed for Event Feed
    RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference ref;
    ArrayList<Event> evenList;
    //get the current user
    User currUser  = User.getInstance();
    String[] array = new String[20];

    String[] images_Firestore = new String[20];
    String[] eventNames_Screenshow = new String[20];
    String[] eventStartTime_Screenshow=new String[20];
    String[] eventEndTime_Screenshow=new String[20];
    String[] eventDate_Screenshow=new String[20];
    ArrayList<ExampleItem> exampleList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_events);

//        mRecyclerView = findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new ExampleAdapter(this, exampleList, "event");
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);


        //set context at very top
        //mContext = getApplicationContext();

        //testUser = new User();
        //create list to make the cards
        //exampleList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.prevEventRecycler);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ExampleAdapter(this, exampleList, "previous");
        mRecyclerView.setAdapter(mAdapter);


        evenList= new ArrayList<Event>();
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
                Toast.makeText(PreviousEventsActivity.this, "Error loading events", Toast.LENGTH_SHORT).show();
            }
        });

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
        LoadDatatoCreatedEvents();
    }



    public void LoadDatatoCreatedEvents(){
        //ArrayList<ExampleItem> exampleList = new ArrayList<>();
        Event event = new Event();
        array = currUser.getCreatedEvents().split(",");
        for (int i = 0; i<array.length; i++) {
            for (int j = 0; j < evenList.size(); j++) {
                if (evenList.get(j).getName().equals(array[i])) {
                    event = evenList.get(j);
                    exampleList.add(0,new ExampleItem(event.getName(), event.getStartTime(), event.getEndTime(),
                            event.getDate(), event.getOwner() ,event.getImage()));
                }
            }
        }
          //  mAdapter.notifyItemInserted(1);
          //  mRecyclerView.scrollToPosition(0);
//        mRecyclerView = findViewById(R.id.recyclerView);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new ExampleAdapter(this, exampleList, "event");
//     mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyItemRangeChanged(0,exampleList.size());
     //   mAdapter = new ExampleAdapter(this, exampleList, "friend");
        mRecyclerView.setAdapter(mAdapter);

    }

}