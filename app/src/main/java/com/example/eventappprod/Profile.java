package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    //Buttons
    private Button friendsListButton;
    private Button prevEventButton;
    private Button archivedEventButton;
    private Button LogoutButton;
    private ImageButton updatePicButton;
    private ImageButton updateBackgroundButton;

    String RealTimeImagePath;

    private ImageView profilePic;
    private ImageView backgroundPic;
    private TextView profileName;
    private TextView profileUsername;
    private String userID;
    private int update = 0;
    User currUser  = User.getInstance();
    private ArrayList<User> userList;


    // authorization
    private FirebaseAuth firebaseAuth;
    //DatabaseReference ref;
    Event event;
    Uri uri;
    StorageReference imagePath;
    FirebaseStorage  storage;
    private DatabaseReference ref;

    String[] profileImages = new String[20];
    String[] friendsList = new String[20];
    String[] userName = new String[20];
    String[] userIDArr = new String[20];


    //all the intents
    Intent friendIntent = getIntent();








    //make phone select an image from their gallery
    public void openFilechooser(int i){
        // create an intent so user can jump to his phone's folder to select photo
        Intent intent = new Intent(Intent.ACTION_PICK);
        update = i;
        // only pick image
        intent.setType("image/*");
        // grab the photo
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ref = FirebaseDatabase.getInstance().getReference("/USER");



        userList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> newUserList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    newUserList.add(ds.getValue(User.class));
                }

                for (int i = 0; i < newUserList.size();i++)
                {
                    if(newUserList.get(i).getUserId().equals(userID))
                    {
                        currUser = newUserList.get(i);
                    }
                }
                userList.clear();
                if (newUserList.size()!=0) {
                    userList = newUserList;
                    retrieveData();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this, "Error loading users", Toast.LENGTH_SHORT).show();
            }
        });


        //user's profile
        profilePic = findViewById(R.id.profilePicture);
        backgroundPic = findViewById(R.id.background);
        profileName = findViewById(R.id.profileName);
        profileName.setText(currUser.getName());
        profileUsername = findViewById(R.id.profileUser);
        userID = currUser.getUserId();
        profileUsername.setText(userID);

        //mainImageView.setImageURI(Uri.parse(image));
        Picasso.get().load(currUser.getProfileImage()).into(profilePic);
        Picasso.get().load(currUser.getBackgroundImage()).into(backgroundPic);
        //create reference to the user
        //ref = FirebaseDatabase.getInstance().getReference("/USER");

        //Button Stuff
        updatePicButton = findViewById(R.id.updatePicBtn);
        updatePicButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFilechooser(0);
                currUser.setProfileImage(RealTimeImagePath);
            }
        });

        //Backgroundupdate
        updateBackgroundButton = findViewById(R.id.updateBackground);
        updateBackgroundButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFilechooser(1);
                currUser.setBackgroundImage(RealTimeImagePath);
            }
        });


        friendsListButton = findViewById(R.id.viewFriendsButton);
        friendsListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsListActivity.class);
                intent.putExtra("ProfileFriend", currUser);

                startActivity(intent);

                /*startActivity(new Intent(getApplicationContext()
                        ,FriendsListActivity.class));*/
            }

        });

        prevEventButton = findViewById(R.id.viewHistoryButton);
        prevEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext()
                        ,PreviousEventsActivity.class));

            }

        });

        archivedEventButton = findViewById(R.id.viewArchiveButton);
        archivedEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext()
                        ,ArchivedEventsActivity.class));

            }

        });

        firebaseAuth = FirebaseAuth.getInstance();
        LogoutButton = findViewById(R.id.btnLogout);
        LogoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext()
                        ,Login.class));
                Toast.makeText(Profile.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
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

        //Set profile as selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext()
                                ,DashBoard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext()
                                ,Search.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            // get the data for picture chosen
            uri = data.getData();
            if(update == 0){
            // set the chooseImage by the picture chosen
            profilePic.setImageURI(uri);
            }

            else {
            backgroundPic.setImageURI(uri);
            }
            // assign the imagePath by using uri

            String url = uri.toString();
            String filename = url.substring(url.lastIndexOf("/")+1);

            imagePath = FirebaseStorage.getInstance().getReference().child("/EVENT").child(filename);

            //  put the picture to put in Image box
            imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                // if upload success, print message
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Profile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                    Task<Uri> downloadUrl = imagePath.getDownloadUrl();
                    downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            RealTimeImagePath = uri.toString();
                            //save url into ref
                            if(update == 0) {
                                ref.child(userID).child("profileImage").setValue(RealTimeImagePath);
                            }
                            else {
                                ref.child(userID).child("backgroundImage").setValue(RealTimeImagePath);
                            }

                        }
                    });

                }
                // if upload fails, print message
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profile.this, "Not Uploaded" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // display the pic
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double process = (120.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                }
            });

        }
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
    }
}

