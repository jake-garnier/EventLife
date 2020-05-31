package com.example.eventappprod;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private ArrayList<ExampleItem> mExampleList;
    //Copy of mExampleList used for filtering
    private ArrayList<ExampleItem> exampleListFull;
    Context context;
    private String cardType;
    User currUser  = User.getInstance();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/USER");



    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView name;
        public TextView startTime;
        public TextView userId;
        public TextView endTime;
        public TextView date;
        public ConstraintLayout mainLayout;
        public Button createEvent;
        public Button mUnfollowButton;
        public Button mRSVPButton;
        public Button mFollowButton;
        public RelativeLayout mRelativeLayout;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            //Get references from my_row.xml
            mImageView = itemView.findViewById(R.id.myImageView);
            name = itemView.findViewById(R.id.cardName);
            startTime = itemView.findViewById(R.id.cardStartTime);
            userId = itemView.findViewById(R.id.friendCardUserId);
            endTime = itemView.findViewById(R.id.cardEndTime);
            date = itemView.findViewById(R.id.cardDate);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            createEvent = itemView.findViewById(R.id.create);
            mUnfollowButton = itemView.findViewById(R.id.unfollowButton);
            mRSVPButton = itemView.findViewById(R.id.RSVPButton);
            mFollowButton = itemView.findViewById(R.id.acceptButton);
            mRelativeLayout = itemView.findViewById(R.id.friendsRL);

        }
    }

    public ExampleAdapter(Context ct, ArrayList<ExampleItem> exampleList, String type){
        context = ct;
        mExampleList = exampleList;
        //Deep copy of mExampleList;
        exampleListFull = new ArrayList<>(exampleList);
        cardType = type;
    }

    @NonNull
    @Override
    //Creates the View holder from our my_row layout
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // The view type 0 is the create event button card type
        if(viewType == 0) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.my_button_row, parent, false);
            ExampleViewHolder evh = new ExampleViewHolder(v);
            return evh;
            // The view type 1 is the regular event card type
        } else if(viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
            ExampleViewHolder evh = new ExampleViewHolder(v);
            return evh;
            // Thw view type 2 is for the friend card type
        } else if(viewType == 2){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_friend_request_row, parent, false);
            ExampleViewHolder evh = new ExampleViewHolder(v);
            return evh;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_friend_row, parent, false);
            ExampleViewHolder evh = new ExampleViewHolder(v);
            return evh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {

            int viewType = getItemViewType(position);

            //The first card is always the create event button
            if(viewType == 0) {

                holder.createEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, CreateEventActivity.class);
                        context.startActivity(intent);
                    }
                });
            } else if(viewType == 1){ // Event list

                ExampleItem currItem = mExampleList.get(position);

//                if(currItem.getImg_firestore()== "")
//                {
//                    holder.mImageView.setImageResource(currItem.getImageResource());
//                }else {
                Picasso.get().load(currItem.getImg_firestore()).into(holder.mImageView);
//                }

                holder.name.setText(currItem.getName());
                holder.startTime.setText(currItem.getStartTime());
                holder.endTime.setText(currItem.getEndTime());
                holder.date.setText(currItem.getDate());

                //This is what allows each card to be clicked and load up a new activity containing the information that goes with that card
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, EventActivity.class);
                        //Extras are what we are passing from the adapter --> EventActivity (the event page)
                        //Inside EventActivity we will use these intents to pull information
                        intent.putExtra("data1", mExampleList.get(position).getName());

                        context.startActivity(intent);
                    }
                });
                holder.mRSVPButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the clicked item label
                        String itemLabel = mExampleList.get(position).getName();
                        //add the event to the user
                        String userRSVP = currUser.getRSVPEvents();
                        String rsvp = itemLabel + "," + currUser.getRSVPEvents();
                        //currUser.addRSVPEvent(rsvp + itemLabel);
                        ref.child(currUser.getUserId()).child("rsvpevents").setValue(rsvp);
                        // Remove the item on remove/button click
                        mExampleList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mExampleList.size());
                        Toast.makeText(context,"Saved in RSVP : " + itemLabel, Toast.LENGTH_SHORT).show();
                    }
                });
            }  else if(viewType == 2){ // Friend Search
                //todo: this part is done in the other user list
                ExampleItem currItem = mExampleList.get(position);

//                if(currItem.getImg_firestore()== "")
//                {
//                    holder.mImageView.setImageResource(currItem.getImageResource());
//                }else {
                Picasso.get().load(currItem.getImg_firestore()).into(holder.mImageView);
//                }

                holder.name.setText(currItem.getName());
                holder.userId.setText(currItem.getStartTime());

                //This is what allows each card to be clicked and load up a new activity containing the information that goes with that card
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, FriendsListActivity.class);
                        //Extras are what we are passing from the adapter --> EventActivity (the event page)
                        //Inside EventActivity we will use these intents to pull information
                        intent.putExtra("data1", mExampleList.get(position).getName());

                        context.startActivity(intent);
                    }
                });
               holder.mFollowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the clicked item label
                        String itemLabel = mExampleList.get(position).getName();

                        // Add the item on accept/button click
                        mExampleList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mExampleList.size());
                        Toast.makeText(context,"Followed : " + itemLabel, Toast.LENGTH_SHORT).show();

                        //Todo: make the person add into their list below but for the other person too
                    }
                });

            } else { // Friends List
                ExampleItem currItem = mExampleList.get(position);

//                if(currItem.getImg_firestore()== "")
//                {
//                    holder.mImageView.setImageResource(currItem.getImageResource());
//                }else {
                Picasso.get().load(currItem.getImg_firestore()).into(holder.mImageView);
            //}

                holder.name.setText(currItem.getName());
                holder.userId.setText(currItem.getStartTime());

                //UnfollowButton used here
                holder.mUnfollowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the clicked item label
                        String itemLabel = mExampleList.get(position).getName();
                        //User ID
                        String userID = mExampleList.get(position).getStartTime();


                        currUser.removeFriend(userID);
                        String userFriendlist = currUser.getFriendList();
                        ref.child(currUser.getUserId()).child("friendList").setValue(userFriendlist);

                        //String rsvp = itemLabel + "," + currUser.getRSVPEvents();
                        //currUser.addRSVPEvent(rsvp + itemLabel);
                        //ref.child(currUser.getUserId()).child("rsvpevents").setValue(rsvp);

                        // Remove the item on remove/button click
                        mExampleList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mExampleList.size());
                        Toast.makeText(context,"Unfollowed : " + itemLabel, Toast.LENGTH_SHORT).show();

                        //Todo: add the part where they wont see that person anymore.
                    }
                });
            }
}

    @Override
    public int getItemViewType (int position) {
        // The first card is always the
        if(position == 0 && this.cardType.equals("event")) {
            return 0;
        } else if(this.cardType.equals("event")){
            return 1;
        } else if(this.cardType.equals("friendSearch")) {
           return 2;
        } if(this.cardType.equals("friend")) {
            return 3;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ExampleItem> filteredList = new ArrayList<>();

            //Show all results bc we aren't filtering anything
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(exampleListFull);
            } else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                //Searches for query in text1 and text2
                int i = 0;
                for(ExampleItem item : exampleListFull) {
                    if (i == 0) { // The item is the button and always should be added
                        filteredList.add(item);
                    } else {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            //|| item.getStartTime().toLowerCase().contains(filterPattern)
                            filteredList.add(item);
                        }
                    }
                    i++;
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mExampleList.clear();
            mExampleList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
