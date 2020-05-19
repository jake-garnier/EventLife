
package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {
    //https://www.youtube.com/watch?v=Nw9JF55LDzE
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference gsRef;
    String eventNames[];
    String eventDescriptions[];
    int images[] = {R.drawable.revelle, R.drawable.revelle, R.drawable.muir, R.drawable.tmc, R.drawable.warren, R.drawable.erc, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla};
    int im[];
    Uri uri[];
    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<Event> evenList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        evenList = new ArrayList<>();
        ref = database.getInstance().getReference("/EVENT");
        eventNames = getResources().getStringArray(R.array.eventNames_feed);
        eventDescriptions = getResources().getStringArray(R.array.eventNames_description);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    evenList.add(ds.getValue(Event.class));
                }
                if (evenList.size()!=0) retrieveData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoard.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });


        //Logic for displaying the event-feed
        //Thus, somehow inject database information into these arrays?

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set profile as selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                , Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext()
                                , Search.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
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
        for (int i=0; i<evenList.size();i++) {
            eventNames[i] = evenList.get(i).getName();
            eventDescriptions[i] = evenList.get(i).getDescription();
        }
        update();

    }


    public void update(){
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            exampleList.add(new ExampleItem(Uri.parse("https://firebasestorage.googleapis.com/v0/b/event-b161b.appspot.com/o/EVENT%2Facc%3D1%3Bdoc%3D21?alt=media&token=12a64f46-2cb9-40af-8ee7-077049c94e5e"),eventNames[i], eventDescriptions[i]));
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, exampleList, "event");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
    public Uri displayImage(String url){
        gsRef = storage.getReference(url);
        final Uri[] u = new Uri[1];
        gsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Log.e("Success" , "uri" + uri.toString());
                u[0] = uri;
            }
        });
        return u[0];

    }


}