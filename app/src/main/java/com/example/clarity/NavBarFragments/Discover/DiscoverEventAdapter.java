package com.example.clarity.NavBarFragments.Discover;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.util.CardFormatter;

import java.util.List;

public class DiscoverEventAdapter extends  RecyclerView.Adapter<DiscoverEventAdapter.DiscoverEventViewHolder> {
    private Context context;
    private List<Post> eventList;

    public DiscoverEventAdapter(Context context, List<Post> eventList) {
        this.context = context;
        this.eventList = eventList;
    }
    public static class DiscoverEventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName;
        TextView eventDate;
        TextView eventTime;
        TextView eventLocation;

        public DiscoverEventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById((R.id.eventImage));
            eventName = itemView.findViewById((R.id.eventName));
            eventDate = itemView.findViewById((R.id.eventDate));
            eventTime = itemView.findViewById((R.id.eventTime));
            eventLocation = itemView.findViewById((R.id.eventLocation));
        }
    }

    @NonNull
    @Override
    public DiscoverEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("DiscoverEventAdapter", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_model, parent, false);
        return new DiscoverEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverEventViewHolder holder, int position) {
        Log.d("DiscoverEventAdapter", "onBindViewHolder");
        Post event_details = eventList.get(position);

        // bind all content to UI
//        holder.eventImage.setImageResource(1);
        // String formatting for CardView
        String rawName = event_details.getTitle();
        String rawStartTime = event_details.getEvent_start();
        String rawEndTime = event_details.getEvent_end();
        String formattedName = CardFormatter.formatTitleDiscover(rawName);
        String formattedDate = CardFormatter.formatDate(rawStartTime);
        String formattedTime = CardFormatter.formatTime(rawStartTime, rawEndTime);

        // Set text in UI
        holder.eventName.setText(formattedName);
        holder.eventDate.setText(formattedDate);
        holder.eventTime.setText(formattedTime);
        holder.eventLocation.setText(event_details.getLocation());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEventList(List<Post> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }
}
