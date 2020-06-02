package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    ImageView mainImageView;
    TextView title, description;

    TextView Date, StartTime, EndTime, Location, CreatedBy;

    String data1;
    String desc, sTime, eTime, loca, date, image, owner;

    int myImage;
    DatabaseReference ref;
    FirebaseDatabase database;

    Drawable drawable;

    Button AttendeesButton;
    Event myevent;
    private ArrayList<User> userList;
    User currUser  = User.getInstance();
    private String userID;

    String[] profileImages = new String[20];
    String[] friendsList = new String[20];
    String[] userName = new String[20];
    String[] userIDArr = new String[20];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ref = FirebaseDatabase.getInstance().getReference("/USER");
        userID = currUser.getEmail().substring(0, currUser.getEmail().indexOf("@"));

        //create user list and update info inside current user from database
        userList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                          ArrayList<User> newUserList = new ArrayList<>();

                                          for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                              newUserList.add(ds.getValue(User.class));
                                          }

                                          for (int i = 0; i < newUserList.size(); i++) {
                                              if (newUserList.get(i).getUserId().equals(userID)) {
                                                  currUser = newUserList.get(i);
                                              }
                                          }

                                          if (newUserList.size() != 0) {
                                              //userList = newUserList;
                                              retrieveData();
                                          }
                                      }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EventActivity.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });



        ref = database.getInstance().getReference("/EVENT");


        //Make references that connect the XML <--> the Java variables
        mainImageView = findViewById(R.id.eventFormImageView);
        title = findViewById(R.id.eventFormName);
        description = findViewById(R.id.eventFormDescription);

        description = findViewById(R.id.tvDescription);
        Date = findViewById(R.id.tvDate);
        StartTime = findViewById(R.id.tvStart);
        EndTime = findViewById(R.id.tvEnd);
        Location = findViewById(R.id.tvLocation);
        CreatedBy =findViewById(R.id.tvCreatedBy);

        AttendeesButton = findViewById(R.id.eventFormGuests);

        //Initialize these methods
        getData();
        //setData();

        AttendeesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                String peopleGoing = myevent.getUserGoing();
                //people going to the event
                String array[] = peopleGoing.split(",");

                //friends going to the event
                String friends_going = currUser.getFriendList();
                String userFriends[] = friends_going.split(",");
                String friends = "";

                //user object
                User user = new User();
                for(int i = 0; i < userFriends.length; i++ ) {
                    for(int j = 0; j < array.length; j++) {
                        //if the user is the same as the array
                        if(userFriends[i].equals(array[j])){
                            friends = userFriends[i] + "," + friends;
                        }
                    }
                }

                String final_array[] = friends.split(",");

                //String person = "";
                builder.setTitle("Attendees:");
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //builder.setTitle("Friends Going: \n" + person);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

        });


    }

    //This is what will check and grab the information and load it in
    private void getData(){
        if(getIntent().hasExtra("data1")){
            //&& getIntent().hasExtra("data2")
            //getIntent().hasExtra("images") &&
            data1 = getIntent().getStringExtra("data1");

            // make sure event name is not null
            if (data1=="" ) return;

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   // Toast.makeText(EventActivity.this, "Debug purpose", Toast.LENGTH_SHORT).show();

                        myevent = (Event) dataSnapshot.child(data1).getValue(Event.class);
                        if (myevent!=null) {
                            desc = myevent.getDescription();
                            date = myevent.getDate();
                            sTime = myevent.getStartTime();
                            eTime = myevent.getEndTime();
                            loca = myevent.getLocation();
                            image = myevent.getImage();
                            owner = myevent.getOwner().replace(",","") ;
                        }

                    // after retrieving all data, then set data to the TextViews
                    setData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            data2 = getIntent().getStringExtra("data2");
            //myImage = getIntent().getIntExtra("images", 1);
        }
        else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }

    }

    //This is actually what displays the information (pushing the data from the intent --> variables from XML)
    private void setData(){
        title.setText(data1);
        description.setText(desc);
        //mainImageView.setImageResource(myImage);
        //mainImageView.setImageURI(Uri.parse(image));
        //Picasso.get().load(image).into(mainImageView);
        Glide.with(EventActivity.this).load(image).into(mainImageView);


        Date.setText(date);
        StartTime.setText(sTime);
        EndTime.setText(eTime);
        Location.setText(loca);
        CreatedBy.setText(owner);
    }
    public void retrieveData(){

        // fetching data to particular array

        for (int i=0; i<userList.size();i++) {
            userName[i] = userList.get(i).getName();
            userIDArr[i] = userList.get(i).getUserId();
            friendsList[i] = userList.get(i).getFriendList();
            profileImages[i] = userList.get(i).getProfileImage();
            // you can get other info like date and time as well
            //Bitmap my_image;
            //Picasso.get().load(evenList.get(i).getImage()).into(my_image);
            LoadDatatoFriendsList();
        }
    }
    public void LoadDatatoFriendsList() {
        String[] array = currUser.getFriendList().split(",");
        User user = new User();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < userList.size(); j++) {
                if (userList.get(j).getUserId().equals(array[i])) {
                    user = userList.get(j);
                    //exampleList.add(new ExampleItem(user.getName(), user.getUserId(), "", "", "", user.getProfileImage()));
                    //mAdapter.notifyItemInserted(0);
                    //mAdapter.resetFull();
                    //mRecyclerView.scrollToPosition(0);
                }
            }
        }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        map.getUiSettings().setMyLocationButtonEnabled(false);
//        map.setMyLocationEnabled(true);
//    }

    }
}