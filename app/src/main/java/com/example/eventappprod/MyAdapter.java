package com.example.eventappprod;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

//This is part of the Event Feed
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    //All this information is passed through the adapter and loaded into the arrays
    //Data1 = Names
    //Data2 = Description
    //Images = Images
    String data1[], data2[];
    int images[];
    Context context;

    public MyAdapter(Context ct, String eventNames[], String eventDescriptions[], int img[]){
        context = ct;
        data1 = eventNames;
        data2 = eventDescriptions;
        images = img;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //This is also what talks to the XML
        holder.name.setText(data1[position]);
        holder.description.setText(data2[position]);
        holder.myImg.setImageResource(images[position]);

        //This is what allows each card to be clicked and load up a new activity containing the information that goes with that card
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view){
                Intent intent = new Intent(context, EventActivity.class);
                //Extras are what we are passing from the adapter --> EventActivity (the event page)
                //Inside EventActivity we will use these intents to pull information
                intent.putExtra("data1", data1[position]);
                intent.putExtra("data2", data2[position]);
                intent.putExtra("images", images[position]);
                context.startActivity(intent);
            }
        });


    }

    @Override
    //Pass number of items we have
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //These are the visual representations
        TextView name, description;
        ImageView myImg;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //This is what talks to the XML files
            name = itemView.findViewById((R.id.event_names_txt));
            description = itemView.findViewById((R.id.event_desc_txt));
            myImg = itemView.findViewById(R.id.myImageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
