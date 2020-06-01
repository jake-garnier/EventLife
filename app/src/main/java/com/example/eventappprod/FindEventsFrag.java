package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FindEventsFrag extends Fragment {

    String[] images_Firestore = new String[20];
    String[] eventNames_Screenshow = new String[20];
    String[] eventStartTime_Screenshow=new String[20];
    String[] eventEndTime_Screenshow=new String[20];
    String[] eventDate_Screenshow=new String[20];

    String[] friendList = new String[20];

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference ref;
    User currUser  = User.getInstance();
    ArrayList<Event> evenList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment2_find_events, container, false);
        setHasOptionsMenu(true);

        evenList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("/EVENT");

        friendList = currUser.getFriendList().split(",");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                evenList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    boolean flag = true;
                    for (String friend : friendList) {
                        if (ds.child("owner").getValue().equals(friend + ","))
                            flag = false;
                    }

                    if (ds.child("owner").getValue().equals(currUser.getUserId() + ","))
                        flag = false;

                    if(flag)
                        evenList.add(ds.getValue(Event.class));
                }
                if (evenList.size() != 0) {
                    retrieveData(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindEventsFrag.super.getContext(), "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void retrieveData(View view){

        // fetching data to particular array

        // fetching data to particular array
        for (int i=0; i<evenList.size();i++) {
            eventNames_Screenshow[i] = evenList.get(i).getName();
            eventStartTime_Screenshow[i] = evenList.get(i).getStartTime();
            eventEndTime_Screenshow[i] = evenList.get(i).getEndTime();
            eventDate_Screenshow[i] = evenList.get(i).getDate();
            images_Firestore[i] = evenList.get(i).getImage();
        }

        LoadDatatoDashBoard(view);

    }

    public void LoadDatatoDashBoard(View view){
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        for (int i = 0; i < evenList.size(); i++) {
            exampleList.add(new ExampleItem(eventNames_Screenshow[i], eventStartTime_Screenshow[i],
                    eventEndTime_Screenshow[i], eventDate_Screenshow[i], "", images_Firestore[i]));
        }

        mRecyclerView = view.findViewById(R.id.recyclerViewFindEvents);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExampleAdapter(this.getContext(), exampleList, "");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
    }
}