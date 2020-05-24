package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FindFriendsFrag extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference gsRef;
    String eventNames[];
    String eventDescriptions[];


    int images[] = {R.drawable.revelle, R.drawable.revelle, R.drawable.muir, R.drawable.tmc, R.drawable.warren, R.drawable.erc, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla,  R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla,  R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla, R.drawable.revelle, R.drawable.revelle, R.drawable.muir, R.drawable.tmc, R.drawable.warren, R.drawable.erc, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla,  R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla, R.drawable.sixth, R.drawable.samoyed, R.drawable.khosla};
    int im[];
    Uri myuri[];


    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<Event> evenList;

    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment1_find_friends, container, false);

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
                if (evenList.size()!=0) retrieveData(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void retrieveData(View view){

        // fetching data to particular array
        for (int i=0; i<evenList.size();i++) {
            eventNames[i] = evenList.get(i).getName();
            eventDescriptions[i] = evenList.get(i).getDescription();

            // you can get other info like date and time as well

        }
        LoadDatatoDashBoard(view);

    }


    public void LoadDatatoDashBoard(View view){
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        for (int i = 0; i <= evenList.size(); i++) {
            exampleList.add(new ExampleItem(images[1],eventNames[1], eventDescriptions[1]));
        }

        mRecyclerView = view.findViewById(R.id.recyclerViewFindFriends);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExampleAdapter(this.getContext(), exampleList, "event");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}
