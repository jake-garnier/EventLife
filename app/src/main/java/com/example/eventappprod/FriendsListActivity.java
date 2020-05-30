package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity {
    //Used Alert Dialogue with this website : https://stackoverflow.com/questions/10903754/input-text-dialog-android
    private ImageButton mButtonAdd;
    private String friendAdd;

    //currUser Stuff
    private String userID;

    //Firebase variables
    private DatabaseReference ref;

    //add context for the app
    private Context mContext;

    //https://www.youtube.com/watch?v=Nw9JF55LDzE
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String[] profileImages = new String[20];
    String[] friendsList = new String[20];
    String[] userName = new String[20];
    String[] userIDArr = new String[20];
    //int[] images = {R.drawable.friend2, R.drawable.friend2, R.drawable.friend4, R.drawable.friend4, R.drawable.friend5, R.drawable.friend6, R.drawable.friend6, R.drawable.samoyed, R.drawable.khosla};

    //private FriendsList
    private ArrayList<ExampleItem> friendList;
    private ArrayList<User> userList;
    ArrayList<ExampleItem> exampleList;


    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    User currUser;
    int added = 0;
    private String[] array;
    //todo: delete later
    User testUser;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        testUser = new User();
        //create list to make the cards
        exampleList = new ArrayList<>();

        //release the user info
        Intent ib = getIntent();
        currUser = (User) ib.getSerializableExtra("ProfileFriend");
        ref = FirebaseDatabase.getInstance().getReference("/USER");
        userID = currUser.getEmail().substring(0, currUser.getEmail().indexOf("@"));

        //create user list and update info inside current user from database
        userList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userList.add(ds.getValue(User.class));
                }
                if (userList.size()!=0) retrieveData();
                for (int i = 0; i < userList.size();i++)
                {
                    if(userList.get(i).getUserId().equals(userID));
                    {
                       currUser = userList.get(i);
                    }
                }
                if (userList.size()!=0) retrieveData();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendsListActivity.this, "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });
        if(userList.contains(currUser))
        {
            Toast.makeText(FriendsListActivity.this, "Debug", Toast.LENGTH_SHORT).show();
        }

        //set the add button to the image button(code from the link above)
        mButtonAdd = findViewById(R.id.addFriendBtn);
       // friendList = new ArrayList<ExampleItem>();

/*
        User user = new User();
        for (int i = 0; i < array.length;i++)
        {
            for (int j = 0; j < userList.size();j++)
            {
                if(userList.get(i).getUserId().equals(array[i]))
                {
                    user = userList.get(i);
                    friendList.add(new ExampleItem(0, array[i], user.getUserId(),user.getProfileImage()));
                }
            }


        }
        */

        //friendList.add(new ExampleItem(images[1], friendNames[1], friendBios[1], ""));


        //Thus, somehow inject database information into these arrays?
        /*friendNames = getResources().getStringArray(R.array.friendNames_feed);
        friendBios = getResources().getStringArray(R.array.friendBios_feed);
        friendList = new ArrayList<>();
        for (int i = 0; i < friendNames.length; i++) {

            //friendList.add(new ExampleItem(images[i], friendNames[i], ""));

            friendList.add(new ExampleItem(images[i], friendNames[i], friendBios[i], ""));

        }*/

        mContext = getApplicationContext();
        mRecyclerView = findViewById(R.id.friendListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, exampleList, "friend");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendsListActivity.this);
                builder.setTitle("Found a Friend? Add their username below!");

                // Set up the input
                final EditText input = new EditText(FriendsListActivity.this);
                //have padding
                input.setPaddingRelative(40, 20, 20, 20);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);


                // Set up the buttons
                builder.setPositiveButton("ZOOM", new DialogInterface.OnClickListener() {
                    //todo: this happens in the other person's friends list with a notification heh
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        friendAdd = input.getText().toString();
                        for (int i = 0; i < userList.size(); i++) {
                            //checks if the user exists in the database or not (aka spelling errors)
                            if (userList.get(i).getUserId().equals(friendAdd)) {
                                currUser.addFriend(friendAdd);
                                ref.child(userID).child("friendList").setValue(currUser.getFriendList());
                                //create the ExampleItem and insert that into the friendsList
                                friendList.add(0, new ExampleItem(0, userList.get(i).getName(), userList.get(i).getUserId(), userList.get(i).getProfileImage()));
                                //create a new row for that friend
                                mAdapter.notifyItemInserted(0);
                                mRecyclerView.scrollToPosition(0);
                                added = 1;
                                //break;
                            }
                        }

                        if (added == 1) {
                            Toast.makeText(mContext, "Added : " + friendAdd, Toast.LENGTH_SHORT).show();
                            added = 0;
                           /* Intent intent = new Intent(getApplicationContext(), FriendsListActivity.class);
                            intent.putExtra("FriendProfile", currUser);

                            startActivity(intent);*/

                        } else {
                            Toast.makeText(mContext, friendAdd + " : Does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //todo: delete
                        friendAdd = input.getText().toString();
                        //todo: delete later
                        testUser.addFriend(friendAdd);
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
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

        // fetching data to particular array

        for (int i=0; i<userList.size();i++) {
            userName[i] = userList.get(i).getName();
            userIDArr[i] = userList.get(i).getUserId();
            friendsList[i] = userList.get(i).getFriendList();
            profileImages[i] = userList.get(i).getProfileImage();
            // you can get other info like date and time as well
            //Bitmap my_image;
            //Picasso.get().load(evenList.get(i).getImage()).into(my_image);

        }

        LoadDatatoFriendsList();
    }
    public void LoadDatatoFriendsList(){
        array = currUser.getFriendList().split(",");
        User user = new User();
        //ArrayList<ExampleItem> exampleList = new ArrayList<>();
        for(int i = 0; i < array.length; i++ ) {
            for(int j = 0; j < userList.size(); j++) {
                if(userList.get(j).getUserId().equals(array[i])){
                    user = userList.get(j);
                    exampleList.add(0, new ExampleItem(0, user.getName(), user.getUserId(), user.getProfileImage()));
                }
            }
        }

        mRecyclerView = findViewById(R.id.friendListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, exampleList, "friend");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}