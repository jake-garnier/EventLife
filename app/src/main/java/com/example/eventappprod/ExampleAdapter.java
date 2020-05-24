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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private ArrayList<ExampleItem> mExampleList;
    //Copy of mExampleList used for filtering
    private ArrayList<ExampleItem> exampleListFull;
    Context context;
    private String cardType;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ConstraintLayout mainLayout;
        public Button createEvent;
        public Button mUnfollowButton;
        public Button mDeclineButton;
        public Button mAcceptButton;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            //Get references from my_row.xml
            mImageView = itemView.findViewById(R.id.myImageView);
            mTextView1 = itemView.findViewById(R.id.event_names_txt);
            mTextView2 = itemView.findViewById(R.id.event_desc_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            createEvent = (Button) itemView.findViewById(R.id.create);
            mUnfollowButton = (Button) itemView.findViewById(R.id.unfollowButton);
            mDeclineButton = (Button) itemView.findViewById(R.id.declineButton);
            mAcceptButton = (Button) itemView.findViewById(R.id.acceptButton);

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
            } else if(viewType == 1){

                ExampleItem currItem = mExampleList.get(position);

                //holder.mImageView.setImageURI(currItem.getImageResource());
                holder.mImageView.setImageResource(currItem.getImageResource());


                holder.mTextView1.setText(currItem.getText1());
                holder.mTextView2.setText(currItem.getText2());

                //This is what allows each card to be clicked and load up a new activity containing the information that goes with that card
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, EventActivity.class);
                        //Extras are what we are passing from the adapter --> EventActivity (the event page)
                        //Inside EventActivity we will use these intents to pull information
                        intent.putExtra("data1", mExampleList.get(position).getText1());
                        intent.putExtra("data2", mExampleList.get(position).getText2());
                        intent.putExtra("images", mExampleList.get(position).getImageResource());
                        context.startActivity(intent);
                    }
                });
            }  else if(viewType == 2){
                //todo: this part is done in the other user list
                ExampleItem currItem = mExampleList.get(position);

                //holder.mImageView.setImageURI(currItem.getImageResource());
                holder.mImageView.setImageResource(currItem.getImageResource());
                holder.mTextView1.setText(currItem.getText1());
                holder.mTextView2.setText(currItem.getText2());

                //This is what allows each card to be clicked and load up a new activity containing the information that goes with that card
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, EventActivity.class);
                        //Extras are what we are passing from the adapter --> EventActivity (the event page)
                        //Inside EventActivity we will use these intents to pull information
                        intent.putExtra("data1", mExampleList.get(position).getText1());
                        intent.putExtra("data2", mExampleList.get(position).getText2());
                        intent.putExtra("images", mExampleList.get(position).getImageResource());
                        context.startActivity(intent);
                    }
                });
               /* holder.mAcceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the clicked item label
                        String itemLabel = mExampleList.get(position).getText1();

                        // Add the item on accept/button click
                        mExampleList.add(0,new ExampleItem(, itemLabel, friendBios[0]);
                        notifyItemInserted(position);
                        notifyItemRangeChanged(position,mExampleList.size());

                        Toast.makeText(context,"Added : " + itemLabel,Toast.LENGTH_SHORT).show();

                        //Todo: make the person add into their list below but for the other person too
                    }
                });
                */
                //decline the person button oop
                holder.mDeclineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the clicked item label
                        String itemLabel = mExampleList.get(position).getText1();

                        // Remove the item on remove/button click
                        mExampleList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,mExampleList.size());
                        Toast.makeText(context,"Declined : " + itemLabel, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                ExampleItem currItem = mExampleList.get(position);

                holder.mImageView.setImageResource(currItem.getImageResource());
                holder.mTextView1.setText(currItem.getText1());
                holder.mTextView2.setText(currItem.getText2());
                //UnfollowButton used here
                holder.mUnfollowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Get the clicked item label
                        String itemLabel = mExampleList.get(position).getText1();

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
        } else if(position == 0 && this.cardType.equals("friend")) {
            return 2;
        } if(this.cardType.equals("nobutton")) {
            return 1;
        }
        return 3;
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
                        if (item.getText1().toLowerCase().contains(filterPattern) || item.getText2().toLowerCase().contains(filterPattern)) {
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
