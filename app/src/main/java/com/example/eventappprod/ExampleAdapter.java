package com.example.eventappprod;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private ArrayList<ExampleItem> mExampleList;
    //Copy of mExampleList used for filtering
    private ArrayList<ExampleItem> exampleListFull;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            //Get references from my_row.xml
            mImageView = itemView.findViewById(R.id.myImageView);
            mTextView1 = itemView.findViewById(R.id.event_names_txt);
            mTextView2 = itemView.findViewById(R.id.event_desc_txt);
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList){
        mExampleList = exampleList;
        //Deep copy of mExampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    //Creates the View holder from our my_row layout
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currItem.getImageResource());
        holder.mTextView1.setText(currItem.getText1());
        holder.mTextView2.setText(currItem.getText2());
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
                for(ExampleItem item : exampleListFull){
                    if(item.getText1().toLowerCase().contains(filterPattern) || item.getText2().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
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
