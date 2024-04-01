package com.example.clarity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;
import com.example.clarity.model.Event;
import com.example.clarity.model.Interest;

import java.util.List;

public class ProfileInterestAdapter extends RecyclerView.Adapter<ProfileInterestAdapter.InterestViewHolder>{

    private Context context;
    private List<Interest> interestList;

    public ProfileInterestAdapter(Context context, List<Interest> interestList){
        this.context = context;
        this.interestList = interestList;
    }

    public static class InterestViewHolder extends RecyclerView.ViewHolder{
        // Each individual element in the list is an EventViewHolder object
        // ViewHolder is a wrapper around the actual view (e.g. the row)
        // Instance variables access the various Views (name, time, location) in the actual view
        TextView interestCategoryNameTextView;
        public InterestViewHolder(@NonNull View itemView) {
            super(itemView);
            interestCategoryNameTextView = itemView.findViewById(R.id.interestCategoryNameTextView);
        }
    }

    @NonNull
    @Override
    public ProfileInterestAdapter.InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_interest, parent, false);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileInterestAdapter.InterestViewHolder holder, int position) {

        Interest interest = interestList.get(position); // get an Event object from our data source (eventList)

        // Bind Event object data to the EventViewHolder
        holder.interestCategoryNameTextView.setText(interest.getName());

        // Set on click listener
        // ViewHolder is not a View, so we access its root view instead
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To be set
                Toast.makeText(context, "ViewHolder itemView onClickListener", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        // RecyclerView calls this to get the size of the data source
        // This is used to determine when there are no more items that can be displayed
        return interestList.size();
    }

    interface onItemClickListener{
        void onItemClick(View view, int position);
    }

}
