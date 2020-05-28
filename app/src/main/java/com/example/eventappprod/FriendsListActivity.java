package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity {
    //Used Alert Dialogue with this website : https://stackoverflow.com/questions/10903754/input-text-dialog-android
    private ImageButton mButtonAdd;
    private String friendAdd;

    //add context for the app
    private Context mContext;

    //https://www.youtube.com/watch?v=Nw9JF55LDzE
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String[] friendNames;
    String[] friendBios;
    int[] images = {R.drawable.friend1, R.drawable.friend2, R.drawable.friend3, R.drawable.friend4, R.drawable.friend5, R.drawable.friend6, R.drawable.friend7, R.drawable.samoyed, R.drawable.khosla};

    //private FriendsList
    private ArrayList<ExampleItem> friendList;

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //set the add button to the image button(code from the link above)
        mButtonAdd = (ImageButton) findViewById(R.id.addFriendBtn);


        //Thus, somehow inject database information into these arrays?
        friendNames = getResources().getStringArray(R.array.friendNames_feed);
        friendBios = getResources().getStringArray(R.array.friendBios_feed);
        friendList = new ArrayList<>();
        for (int i = 0; i < friendNames.length; i++) {

            //friendList.add(new ExampleItem(images[i], friendNames[i], ""));

            friendList.add(new ExampleItem(images[i], friendNames[i], friendBios[i],""));

        }

        mContext = getApplicationContext();
        mRecyclerView = findViewById(R.id.friendListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, friendList, "friend");
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
                        //add word to friendRequestList
                        //Todo: fix the temp images and bios to the user's
                        friendList.add(0, new ExampleItem(images[0], friendAdd, friendBios[0],""));
                        //create a new row for that friend
                        mAdapter.notifyItemInserted(0);
                        mRecyclerView.scrollToPosition(0);
                        Toast.makeText(mContext,"Added : " + friendAdd,Toast.LENGTH_SHORT).show();

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
}