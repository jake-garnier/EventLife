package com.example.eventappprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity {
    //https://www.youtube.com/watch?v=Nw9JF55LDzE
    //https://www.youtube.com/watch?v=18VcnYN5_LM
    //Event Feed String Arrays
    String friendNames[];
    String friendBios[];
    int images[] = {R.drawable.friend1, R.drawable.friend2, R.drawable.friend3, R.drawable.friend4, R.drawable.friend5, R.drawable.friend6, R.drawable.friend7, R.drawable.samoyed, R.drawable.khosla};

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //Thus, somehow inject database information into these arrays?
        friendNames = getResources().getStringArray(R.array.friendNames_feed);
        friendBios = getResources().getStringArray(R.array.friendBios_feed);
        ArrayList<ExampleItem> friendList = new ArrayList<>();
        for(int i = 0; i < friendNames.length; i++){

            //friendList.add(new ExampleItem(images[i], friendNames[i], ""));

           friendList.add(new ExampleItem(images[i], friendNames[i], friendBios[i]));


        }

        mRecyclerView =  findViewById(R.id.friendListRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(this, friendList, "friend");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
}