
package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import android.net.Uri;

import android.content.res.ColorStateList;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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

public class DashBoard<user> extends AppCompatActivity {
    //https://www.youtube.com/watch?v=Nw9JF55LDzE
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    private static final String TAG = "PostDetailActivity";
    private static final String CHANNEL_ID = "Channel1";



    String[] images_Firestore = new String[20];
    String[] eventNames_Screenshow = new String[20];
    String[] eventStartTime_Screenshow=new String[20];
    String[] eventEndTime_Screenshow=new String[20];
    String[] eventDate_Screenshow=new String[20];
    String[] creator=new String[20];



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

    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
    String email = currentuser.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //Use the  User.getInstance() get the current user don't use the Intent
        //Intent ib = getIntent();
        //currUser = (User) ib.getSerializableExtra("LoginDash");

        userref = FirebaseDatabase.getInstance().getReference("/USER");

        //Getting the updated user
        /*userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userList.add(ds.getValue(User.class));
                }
                for (int i = 0; i < userList.size();i++)
                {
                    if(userList.get(i).getUserId().equals(currUser.getUserId()))
                    {
                        currUser = userList.get(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashBoard.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });*/

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

        // Blank event for the button
        eventNames_Screenshow[0] = "";
        eventStartTime_Screenshow[0] = "";
        eventEndTime_Screenshow[0] = "";
        eventDate_Screenshow[0] = "";
        images_Firestore[0] = "";
        creator[0] = "";

        for (int i=1; i<evenList.size();i++) {
            eventNames_Screenshow[i] = evenList.get(i-1).getName();
            eventStartTime_Screenshow[i] = evenList.get(i-1).getStartTime();
            eventEndTime_Screenshow[i] = evenList.get(i-1).getEndTime();
            eventDate_Screenshow[i] = evenList.get(i-1).getDate();
            images_Firestore[i] = evenList.get(i-1).getImage();
            creator[i] = evenList.get(i-1).getOwner().replace(",","");
        }

        LoadDatatoDashBoard();

    }

    public void LoadDatatoDashBoard(){
        ArrayList<ExampleItem> exampleList = new ArrayList<>();

        for (int i = 0; i<evenList.size(); i++) {
            if(i<9)
            {
                exampleList.add(new ExampleItem(eventNames_Screenshow[i], eventStartTime_Screenshow[i],
                        eventEndTime_Screenshow[i], eventDate_Screenshow[i], creator[i], images_Firestore[i]));
            }else
            {
                exampleList.add(new ExampleItem(eventNames_Screenshow[5], eventStartTime_Screenshow[i],
                        eventEndTime_Screenshow[i], eventDate_Screenshow[i], creator[i], images_Firestore[i]));
            }


        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, exampleList, "event");
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