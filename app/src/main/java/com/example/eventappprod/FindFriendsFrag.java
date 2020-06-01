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

    String[] profilePic = new String[20];
    String[] userName = new String[20];
    String[] userIDList = new String[20];

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<User> userList;
    ArrayList<ExampleItem> exampleList = new ArrayList<>();;

    //current User stuff
    String[] friendArr;
    User currUser = User.getInstance();
    String userID = currUser.getUserId();

    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment1_find_friends, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = view.findViewById(R.id.recyclerViewFindFriends);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExampleAdapter(this.getContext(), exampleList, "friendSearch");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        userList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("/USER");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> newUserList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    newUserList.add(ds.getValue(User.class));
                }
                if (newUserList.size()!=0) {
                    userList = newUserList;
                    retrieveData(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindFriendsFrag.super.getContext(), "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void retrieveData(View view){

        // fetching data to particular array

        for (int i=0; i < userList.size();i++) {
            userIDList[i] = userList.get(i).getUserId();
            userName[i] = userList.get(i).getName();
            profilePic[i] = userList.get(i).getProfileImage();
            // you can get other info like date and time as well
            //Bitmap my_image;
            //Picasso.get().load(evenList.get(i).getImage()).into(my_image);
        }
        for (int i=0; i < userList.size() ;i++) {
            if(userList.get(i).getUserId().equals(userID)){
                currUser = userList.get(i);
            }
        }

        LoadDatatoDashBoard(view);

    }

    public void LoadDatatoDashBoard(View view){
        friendArr = currUser.getFriendList().split(",");
        User userCompare = new User();
        /*for (int i = 0; i < userList.size(); i++)
        {
            if ()
            {
                userCompare = userList.get(j);
                exampleList.add(new ExampleItem(userCompare.getName(), userCompare.getUserId(), "", "", userCompare.getProfileImage()));
                mAdapter.notifyItemInserted(0);
                // mAdapter.resetFull();
                mRecyclerView.scrollToPosition(0);
            }
        }*/

        boolean flag = false;

        for (int i = 0; i < userList.size(); i++) {
            flag = false;
            for(int j = 0; j < friendArr.length; j++) {
                if ((userList.get(i).getUserId().equals(friendArr[j]))
                        || userList.get(i).getUserId().equals(currUser.getUserId())) {
                    flag = true;
                }
            }
            if (flag == false) {
                userCompare = userList.get(i);
                exampleList.add(new ExampleItem(userCompare.getName(), userCompare.getUserId(),
                        "", "", "", userCompare.getProfileImage()));
                mAdapter.notifyItemInserted(0);
                mAdapter.resetFull();
                mRecyclerView.scrollToPosition(0);
            }


        }
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