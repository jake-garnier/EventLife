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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import javax.annotation.Nullable;

public class DashBoardFrag extends Fragment {

    //https://www.youtube.com/watch?v=Nw9JF55LDzE
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    private static final String TAG = "PostDetailActivity";
    private static final String CHANNEL_ID = "Channel1";

    ArrayList<String> images_Firestore = new ArrayList<>();
    ArrayList<String> eventNames_Screenshow = new ArrayList<>();
    ArrayList<String> eventStartTime_Screenshow = new ArrayList<>();
    ArrayList<String> eventEndTime_Screenshow = new ArrayList<>();
    ArrayList<String> eventDate_Screenshow = new ArrayList<>();
    ArrayList<String> creator = new ArrayList<>();

    ArrayList<String> friendList = new ArrayList<>();
    ArrayList<String> rsvpEvents = new ArrayList<>();

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference ref;
    DatabaseReference userref;

    ArrayList<Event> evenList = new ArrayList<>();
    User currUser  = User.getInstance();
    private ArrayList<User> userList;
    ArrayList<User> newUserList;

    String[] friends = new String[currUser.getFriendList().length()];
    String[] rsvp = new String[currUser.getRSVPEvents().length()];

    //friends stuff
    String friendAdd;
    int added = 0;
    private String[] array;
    String userID = currUser.getUserId();
    String[] userName = new String[20];
    String[] userIDArr = new String[20];
    String[] friendsList = new String[20];
    String[] profileImages = new String[20];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_dash_board_frag, container, false);
        setHasOptionsMenu(true);

        userref = FirebaseDatabase.getInstance().getReference("/USER");
        newUserList = new ArrayList<>();

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                newUserList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    newUserList.add(ds.getValue(User.class));
                }

                if (newUserList.size() != 0) {
                    userList = newUserList;
                    retrieveData(view);
                }

                for (int i = 0; i < newUserList.size(); i++) {
                    if (newUserList.get(i).getUserId().equals(userID)) {
                        currUser = newUserList.get(i);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoardFrag.super.getContext(), "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        array = currUser.getFriendList().split(",");

        ref = FirebaseDatabase.getInstance().getReference("/EVENT");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                friendList.clear();
                friends = currUser.getFriendList().split(",");
                friendList.addAll(Arrays.asList(friends));

                rsvpEvents.clear();
                rsvp = currUser.getRSVPEvents().split(",");
                Collections.addAll(rsvpEvents, rsvp);

                evenList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    for (String friend : friendList) {
                        if (ds.child("owner").getValue().equals(friend + ",")) {
                            evenList.add(ds.getValue(Event.class));
                        }
                    }

                    for (String event : rsvpEvents) {
                        if (ds.child("name").getValue().equals(event)) {
                            Iterator<Event> it = evenList.iterator();
                            while (it.hasNext()) {
                                Event eventIteration = it.next();
                                if (eventIteration.getName().equals(ds.child("name").getValue())) {
                                    it.remove();
                                }
                            }
                        }
                    }

                    if (ds.child("owner").getValue().equals(currUser.getUserId() + ",")) {
                        evenList.add(ds.getValue(Event.class));
                    }
                }

                retrieveData(view);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoardFrag.super.getContext(), "Error loading events", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_friend_button) {
            // do something here
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Friend Request? Add their Username below!");

            // Set up the input
            final EditText input = new EditText(getActivity());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setPaddingRelative(40,20,20,20);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Add Friend", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    friendAdd = input.getText().toString();
                    boolean flag = false;
                    array = currUser.getFriendList().split(",");
                    if(userList.size()!=0)
                    {
                        for (int i = 0; i < userList.size(); i++) {
                            //flag = false;
                            for (int j = 0; j < array.length;j++) {
                                //checks if the user exists in the database or not (aka spelling errors)
                                if ((userList.get(i).getUserId().equals(array[j]))
                                        || userList.get(i).getUserId().equals(currUser.getUserId())) {
                                    flag = true;
                                }
                                if (userList.get(i).getUserId().equals(friendAdd) && flag == false)
                                {
                                    currUser.addFriend(friendAdd);
                                    userref.child(currUser.getUserId()).child("friendList").setValue(currUser.getFriendList());
                                    //ref.child(userID).child("friendList").setValue(currUser.getFriendList());
                                    //create the ExampleItem and insert that into the friendsList
                                    //create a new row for that friend
                                    added = 1;
                                    break;
                                }
                            }
                        }

                        if (added == 1) {
                            Toast.makeText(DashBoardFrag.super.getContext(), "Added friend " + friendAdd, Toast.LENGTH_SHORT).show();
                            added = 0;
                        } else {
                            Toast.makeText(DashBoardFrag.super.getContext(), friendAdd + " does not exist or is already added", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.cancel();
                }
            });
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

    public void retrieveData(View view){
        eventNames_Screenshow.clear();
        eventStartTime_Screenshow.clear();
        eventEndTime_Screenshow.clear();
        eventDate_Screenshow.clear();
        images_Firestore.clear();
        creator.clear();

        // Blank event for the button
        eventNames_Screenshow.add("");
        eventStartTime_Screenshow.add("");
        eventEndTime_Screenshow.add("");
        eventDate_Screenshow.add("");
        images_Firestore.add("");
        creator.add("");

        for (int i=1; i<evenList.size()+1;i++) {
            eventNames_Screenshow.add(i, evenList.get(i-1).getName());
            eventStartTime_Screenshow.add(i, evenList.get(i-1).getStartTime());
            eventEndTime_Screenshow.add(i, evenList.get(i-1).getEndTime());
            eventDate_Screenshow.add(i, evenList.get(i-1).getDate());
            images_Firestore.add(i, evenList.get(i-1).getImage());
            creator.add(i,evenList.get(i-1).getOwner());
        }

        for (int i=0; i<userList.size();i++) {
            userName[i] = userList.get(i).getName();
            userIDArr[i] = userList.get(i).getUserId();
            friendsList[i] = userList.get(i).getFriendList();
            profileImages[i] = userList.get(i).getProfileImage();
            // you can get other info like date and time as well
            //Bitmap my_image;
            //Picasso.get().load(evenList.get(i).getImage()).into(my_image);

        }

        LoadDatatoDashBoard(view);

    }

    public void LoadDatatoDashBoard(View view){

        ArrayList<Card> exampleList = new ArrayList<>();
        for (int i = 0; i < evenList.size() + 1; i++) {
            exampleList.add(new Card(eventNames_Screenshow.get(i), eventStartTime_Screenshow.get(i), eventEndTime_Screenshow.get(i), eventDate_Screenshow.get(i), creator.get(i), images_Firestore.get(i)));
        }

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());


        if(evenList.size() == 0) {
            mAdapter = new CardAdapter(this.getContext(), exampleList, "empty");
        } else {
            mAdapter = new CardAdapter(this.getContext(), exampleList, "event");
        }


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        addNotification();
    }
}