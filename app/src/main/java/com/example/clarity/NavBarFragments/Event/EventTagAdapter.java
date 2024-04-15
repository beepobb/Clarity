package com.example.clarity.NavBarFragments.Event;

import android.content.Context;
import android.media.metrics.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.NavBarFragments.Calendar.CalendarEventAdapter;
import com.example.clarity.NavBarFragments.Calendar.CalendarFragment;
import com.example.clarity.NavBarFragments.Discover.EventTags;
import com.example.clarity.NavBarFragments.Discover.TagButtonUpdateEventsClickListener;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Tag;

import java.util.ArrayList;
import java.util.List;

public class EventTagAdapter extends RecyclerView.Adapter<EventTagAdapter.EventTagViewHolder> {
    private Context context;
    private List<Tag> categoryList;
    public EventTagAdapter(Context context, List<Tag> categoryList){
        this.context = context;
        this.categoryList = categoryList;
    }
    @NonNull
    @Override
    public EventTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_tag_button_model, parent, false);
        return new EventTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTagViewHolder holder, int position) {
        Tag tag = categoryList.get(position);
        holder.tagName.setText(tag.getTag_category());
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

    public void updateTagList(List<Tag> categoryList) {
        // Called when list of events to display on Recycler changes
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

}

