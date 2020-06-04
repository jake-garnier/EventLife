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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FindFriendsFrag extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference gsRef;

    String[] profilePic = new String[20];
    String[] userName = new String[20];
    String[] userIDList = new String[20];

    //Recycler View Needed for Event Feed
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<User> userList;
    ArrayList<ExampleItem> exampleList = new ArrayList<>();;
    ArrayList<User> newUserList;

    //current User stuff
    String[] friendArr;
    User currUser = User.getInstance();
    String userID = currUser.getUserId();

    //friends stuff
    String friendAdd;
    int added = 0;
    private String[] array;


    private Button btnTEST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment1_find_friends, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = view.findViewById(R.id.recyclerViewFindFriends);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExampleAdapter(this.getContext(), exampleList, "friendSearch");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        userList = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("/USER");
        newUserList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                newUserList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    newUserList.add(ds.getValue(User.class));
                }
                if (newUserList.size()!=0) {
                    userList = newUserList;
                    retrieveData(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindFriendsFrag.super.getContext(), "Error on Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void retrieveData(View view){

        // fetching data to particular array

        for (int i=0; i < userList.size();i++) {
            userIDList[i] = userList.get(i).getUserId();
            userName[i] = userList.get(i).getName();
            profilePic[i] = userList.get(i).getProfileImage();
            // you can get other info like date and time as well
            //Bitmap my_image;
            //Picasso.get().load(evenList.get(i).getImage()).into(my_image);
        }
        for (int i=0; i < userList.size() ;i++) {
            if(userList.get(i).getUserId().equals(userID)){
                currUser = userList.get(i);
            }
        }

        LoadDatatoDashBoard(view);

    }

    public void LoadDatatoDashBoard(View view){
        friendArr = currUser.getFriendList().split(",");
        User userCompare = new User();

        boolean flag = false;

        for (int i = 0; i < userList.size(); i++) {
            flag = false;
            for(int j = 0; j < friendArr.length; j++) {
                if ((userList.get(i).getUserId().equals(friendArr[j]))
                        || userList.get(i).getUserId().equals(currUser.getUserId())) {
                    flag = true;
                }
            }
            if (flag == false) {
                userCompare = userList.get(i);
                exampleList.add(new ExampleItem(userCompare.getName(), userCompare.getUserId(),
                        "", "", "", userCompare.getProfileImage()));
                mAdapter.notifyItemInserted(0);
                mAdapter.resetFull();
                mRecyclerView.scrollToPosition(0);
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_friend_button) {
            // do something here
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Friend Request? Add their Username below and zoom!");

            // Set up the input
            final EditText input = new EditText(getActivity());
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
                            for (int j = 0; j < friendArr.length;j++) {
                                //checks if the user exists in the database or not (aka spelling errors)
                                if ((userList.get(i).getUserId().equals(friendArr[j]))
                                        || userList.get(i).getUserId().equals(currUser.getUserId())) {
                                    flag = true;
                                }
                                if (userList.get(i).getUserId().equals(friendAdd) && flag == false)
                                {
                                    currUser.addFriend(friendAdd);
                                    ref.child(currUser.getUserId()).child("friendList").setValue(currUser.getFriendList());
                                    //ref.child(userID).child("friendList").setValue(currUser.getFriendList());
                                    //create the ExampleItem and insert that into the friendsList
                                    //create a new row for that friend
                                    added = 1;
                                    break;
                                }
                            }
                        }

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
    }
}