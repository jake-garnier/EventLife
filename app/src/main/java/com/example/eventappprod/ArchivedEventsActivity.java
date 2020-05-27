package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ArchivedEventsActivity extends AppCompatActivity {
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String[] eventNames;
    String[] eventDescriptions;
    int[] images = {R.drawable.revelle, R.drawable.event1, R.drawable.event2, R.drawable.event3, R.drawable.event4, R.drawable.event5, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla};
    //Recycler View Needed for Event Feed
    RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> evenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_events);

        //Logic for displaying the event-feed
        //Thus, somehow inject database information into these arrays?
        eventNames = getResources().getStringArray(R.array.eventNames_feed);
        eventDescriptions = getResources().getStringArray(R.array.eventNames_description);
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        for (int i = 0; i < eventNames.length; i++) {
            exampleList.add(new ExampleItem(images[i], eventNames[i], eventDescriptions[i],""));
        }

        mRecyclerView = findViewById(R.id.recyclerViewArchive);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, exampleList, "nobutton");
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
}
