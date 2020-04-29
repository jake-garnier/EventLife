package com.example.eventappprod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    //Buttons
    private Button friendsListButton;
    private Button prevEventButton;
    private Button archivedEventButton;
    private Button LogoutButton;

    // authorization
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Button Stuff
        friendsListButton = (Button) findViewById(R.id.viewFriendsButton);
        friendsListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext()
                        ,FriendsListActivity.class));

            }

        });

        prevEventButton = (Button) findViewById(R.id.viewHistoryButton);
        prevEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext()
                        ,PreviousEventsActivity.class));

            }

        });

        archivedEventButton = (Button) findViewById(R.id.viewArchiveButton);
        archivedEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext()
                        ,ArchivedEventsActivity.class));

            }

        });

        firebaseAuth = FirebaseAuth.getInstance();
        LogoutButton = (Button) findViewById(R.id.btnLogout);
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
}
