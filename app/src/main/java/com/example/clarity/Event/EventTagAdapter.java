package com.example.clarity.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.NavBarFragments.Discover.EventTags;
import com.example.clarity.R;

import java.util.List;

//Adapter to show tags in the events page
public class EventTagAdapter extends RecyclerView.Adapter<EventTagAdapter.EventTagViewHolder> {
    private final Context context;
    private List<EventTags> categoryList;
    public EventTagAdapter(Context context, List<EventTags> categoryList){
        this.context = context;
        this.categoryList = categoryList;
    }
    //Creates a new EventTagViewHolder by inflating the layout.
    @NonNull
    @Override
    public EventTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_tag_button_model, parent, false);
        return new EventTagViewHolder(view);
    }

    //Binds the event tag data to the view holder.
    @Override
    public void onBindViewHolder(@NonNull EventTagViewHolder holder, int position) {
        EventTags tag = categoryList.get(position);
        holder.tagName.setText(tag.toString());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class EventTagViewHolder extends RecyclerView.ViewHolder{
        TextView tagName ;

        public EventTagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.category_name);
        }
    }

    public void updateTagList(List<EventTags> categoryList) {
        // Called when list of events to display on Recycler changes
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

}

