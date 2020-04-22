package com.example.eventappprod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//This is part of the Event Feed
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    //Data1 = Names
    //Data2 = Description
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(data1[position]);
        holder.description.setText(data2[position]);
        holder.myImg.setImageResource(images[position]);


    }

    @Override
    //Pass number of items we have
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        ImageView myImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById((R.id.event_names_txt));
            description = itemView.findViewById((R.id.event_desc_txt));
            myImg = itemView.findViewById(R.id.myImageView);
        }
    }
}
