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
import android.widget.EditText;
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

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference ref;
    DatabaseReference userRef;
    ArrayList<Event> evenList;

    //Current user information
    ArrayList<User> userList;
    User currUser  = User.getInstance();
    String userID = currUser.getUserId();
    String[] friendList;
    String[] userIDList = new String[20];

    //Current user's friendslist
    String[] friendArr;
    String friendAdd;
    int added = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment2_find_events, container, false);
        setHasOptionsMenu(true);

        evenList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("/EVENT");

        userList = new ArrayList<>();
        userRef = FirebaseDatabase.getInstance().getReference("/USER");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                //fetch data from snap shots from database to populate array
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    userList.add(ds.getValue(User.class));
                }
                for (int i=0; i < userList.size() ;i++) {
                    if(userList.get(i).getUserId().equals(userID)){
                        currUser = userList.get(i);
                    }
                }
                //check if data is fully fetched
                if (userList.size()!=0) {
                    retrieveData(view);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindEventsFrag.super.getContext(), "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });

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

                    if (ds.child("owner").getValue().equals(currUser.getUserId() + ",")) {
                        flag = false;
                    }
                    String[] rsvpevents = currUser.getRSVPEvents().split(",");

                    for (String e : rsvpevents) {
                        if (ds.child("name").getValue().equals(e)) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        evenList.add(ds.getValue(Event.class));
                    }
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

        // fetching data to arrays relating to users
        for (int i=0; i < userList.size();i++) {
            userIDList[i] = userList.get(i).getUserId();
        }

        // fetching data to arrays relating to events
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

        friendArr = currUser.getFriendList().split(",");

        mRecyclerView = view.findViewById(R.id.recyclerViewFindEvents);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExampleAdapter(this.getContext(), exampleList, "");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
                                    userRef.child(currUser.getUserId()).child("friendList").setValue(currUser.getFriendList());
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