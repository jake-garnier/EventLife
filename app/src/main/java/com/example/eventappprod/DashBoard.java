package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;

import android.content.res.ColorStateList;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class DashBoard<user> extends AppCompatActivity {
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
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference userref;

    ArrayList<Event> evenList = new ArrayList<>();
    User currUser  = User.getInstance();
    private ArrayList<User> userList;
    ArrayList<User> newUserList;

    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    String email = currentuser.getEmail();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

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
                    retrieveData();
                }

                for (int i = 0; i < newUserList.size(); i++) {
                    if (newUserList.get(i).getUserId().equals(userID)) {
                        currUser = newUserList.get(i);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoard.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "EventLife";
            String description = "EventLife";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        ref = FirebaseDatabase.getInstance().getReference("/EVENT");
        //eventNames_Screenshow = getResources().getStringArray(R.array.eventNames_feed);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                friendList.clear();
                friends = currUser.getFriendList().split(",");
                for (String friend : friends) {
                    friendList.add(friend);
                }

                rsvpEvents.clear();
                rsvp = currUser.getRSVPEvents().split(",");
                for (String rsvpEvent : rsvp) {
                    rsvpEvents.add(rsvpEvent);
                }

                evenList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("owner").getValue().equals(currUser.getUserId() + ",")) {
                        evenList.add(ds.getValue(Event.class));
                    }

                    for (String friend : friendList) {
                        if (ds.child("owner").getValue().equals(friend + ",")) {
                            evenList.add(ds.getValue(Event.class));
                        }
                    }

                    for(String event : rsvpEvents) {
                        if(ds.child("name").getValue().equals(event)) {
                            Iterator<Event> it = evenList.iterator();
                            while(it.hasNext()) {
                                Event eventIteration = it.next();
                                if(eventIteration.getName().equals(ds.child("name").getValue())) {
                                    it.remove();
                                }
                            }
                        }
                    }
                }

                retrieveData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoard.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // The navigation text/symbols will change color when you are on that page
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        int[] colors = new int[] {
                Color.LTGRAY,
                Color.WHITE,
        };

        int [][] states = new int [][]{
                new int[] { android.R.attr.state_enabled, -android.R.attr.state_checked},
                new int[] {android.R.attr.state_enabled, android.R.attr.state_checked}
        };

        bottomNavigationView.setItemTextColor(new ColorStateList(states, colors));
        bottomNavigationView.setItemIconTintList(new ColorStateList(states, colors));

        //Set dashboard as selected
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        return true;
                    case R.id.profile:

                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_friend_button) {
            // do something here
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Friend Request? Add their Username below and zoom!");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setPaddingRelative(40,20,20,20);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("ZOOM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    friendAdd = input.getText().toString();
                    boolean flag = false;
                    if(userList.size()!=0)
                    {
                        for (int i = 0; i < userList.size(); i++) {
                            flag = false;
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
                            Toast.makeText(DashBoard.this, "Added : " + friendAdd, Toast.LENGTH_SHORT).show();
                            added = 0;
                        } else {
                            Toast.makeText(DashBoard.this, friendAdd + " : Does not exist or Already added", Toast.LENGTH_SHORT).show();
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
        array = currUser.getFriendList().split(",");
        LoadDatatoDashBoard();

    }

    public void LoadDatatoDashBoard(){
        ArrayList<ExampleItem> exampleList = new ArrayList<>();
        for (int i = 0; i < evenList.size() + 1; i++) {
            exampleList.add(new ExampleItem(eventNames_Screenshow.get(i), eventStartTime_Screenshow.get(i), eventEndTime_Screenshow.get(i), eventDate_Screenshow.get(i), creator.get(i), images_Firestore.get(i)));
        }


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);


        if(evenList.size() == 0) {
            mAdapter = new ExampleAdapter(this, exampleList, "empty");
        } else {
            mAdapter = new ExampleAdapter(this, exampleList, "event");
        }


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        addNotification();
    }

//    private void addNotification() {
//        Intent intent = new Intent(this, EventActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        for(int i=0; i<evenList.size(); i++) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.notification_icon)
//                    .setContentTitle(evenList.get(i).getName())
//                    .setContentText(evenList.get(i).getStartTime())
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    // Set the intent that will fire when the user taps the notification
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(i, builder.build());
//        }
//    }

}