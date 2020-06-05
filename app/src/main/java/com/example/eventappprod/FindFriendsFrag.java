package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //Database variables and lists needed to pull data from firebase
    DatabaseReference ref;
    ArrayList<User> userList;
    ArrayList<ExampleItem> exampleList = new ArrayList<>();;
    ArrayList<User> newUserList;

    //arrays used to make sure database populates the arrays
    String[] profilePic = new String[20];
    String[] userName = new String[20];
    String[] userIDList = new String[20];

    //Current user information
    User currUser = User.getInstance();
    String userID = currUser.getUserId();

    //Current user's friendslist
    String[] friendArr;
    String friendAdd;
    int added = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment1_find_friends, container, false);
        setHasOptionsMenu(true);

        //setting up the recycler view and adapter
        mRecyclerView = view.findViewById(R.id.recyclerViewFindFriends);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExampleAdapter(this.getContext(), exampleList, "friendSearch");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //populate the information from database to local arraylist
        userList = new ArrayList<>();
        newUserList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("/USER");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                newUserList.clear();
                //fetch data from snap shots from database to populate array
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    newUserList.add(ds.getValue(User.class));
                }
                //check if data is fully fetched
                if (newUserList.size()!=0) {
                    userList = newUserList;
                    retrieveData(view);
                }
            }
            // checks if there is an error retrieving data from database
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindFriendsFrag.super.getContext(), "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //to allow data to be extracted fully from database
    public void retrieveData(View view){
        // fetching data to particular array
        for (int i=0; i < userList.size();i++) {
            userIDList[i] = userList.get(i).getUserId();
            userName[i] = userList.get(i).getName();
            profilePic[i] = userList.get(i).getProfileImage();
        }
        //fetching current user from the list of users in database
        for (int i=0; i < userList.size() ;i++) {
            if(userList.get(i).getUserId().equals(userID)){
                currUser = userList.get(i);
            }
        }
        LoadDatatoDashBoard(view);

    }

    //populate the fragment with users that aren't your friend
    public void LoadDatatoDashBoard(View view){
        //get user's friends list
        friendArr = currUser.getFriendList().split(",");
        User userCompare = new User();
        boolean flag = false;
        //iterate through the entire user list from database
        for (int i = 0; i < userList.size(); i++) {
            flag = false;
            //iterate through the current user's friendlist
            for(int j = 0; j < friendArr.length; j++) {
                //check if the user is already in the friends list or the user itself
                if ((userList.get(i).getUserId().equals(friendArr[j]))
                        || userList.get(i).getUserId().equals(currUser.getUserId())) {
                    flag = true;
                }
            }
            //if the user isn't the current user or part of the current user's friend list display it
            if (flag == false) {
                userCompare = userList.get(i);
                //populate the card and add it in the fragment
                exampleList.add(new ExampleItem(userCompare.getName(), userCompare.getUserId(),
                        "", "", "", userCompare.getProfileImage()));
                mAdapter.notifyItemInserted(0);
                mAdapter.resetFull();
                mRecyclerView.scrollToPosition(0);
            }
        }
    }

    //the method for adding friends in friend fragment based on the button in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_friend_button) {
            //create the alert box
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Friend Request? Add their Username below and zoom!");

            // Set up the input
            final EditText input = new EditText(getActivity());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setPaddingRelative(40,20,20,20);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Used when current user presses the add button in the alert box
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    friendAdd = input.getText().toString();
                    boolean flag = false;
                    if(userList.size()!=0)
                    {
                        for (int i = 0; i < userList.size(); i++) {
                            flag = false;
                            for (int j = 0; j < friendArr.length;j++) {
                                //checks if the user exists in the database or not (aka spelling errors)
                                if ((userList.get(i).getUserId().equals(friendArr[j]))
                                        || userList.get(i).getUserId().equals(currUser.getUserId())) {
                                    flag = true;
                                }
                                //checks if the friend can be added to the friend list
                                if (userList.get(i).getUserId().equals(friendAdd) && flag == false)
                                {
                                    currUser.addFriend(friendAdd);
                                    //sets new friends list into the database
                                    ref.child(currUser.getUserId()).child("friendList").setValue(currUser.getFriendList());
                                    added = 1;
                                    break;
                                }
                            }
                        }
                        //displays the correct toast message depending on if the person was added or not
                        if (added == 1) {
                            Toast.makeText(getActivity(), "Added : " + friendAdd, Toast.LENGTH_SHORT).show();
                            added = 0;
                        } else {
                            Toast.makeText(getActivity(), friendAdd + " : Does not exist or Already added", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.cancel();
                }
            });
            //used when current user decides to stop the process of adding the friend
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //this method makes sure we are using the correct menu display on the top of the app
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //select the correct menu layout
        inflater.inflate(R.menu.example_menu, menu);

        //utilizes the search action
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