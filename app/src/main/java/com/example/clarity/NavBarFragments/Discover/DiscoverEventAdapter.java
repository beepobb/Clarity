package com.example.clarity.NavBarFragments.Discover;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.Event.EventsPageActivity;
import com.example.clarity.PostParcelable;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.util.CardFormatter;

import java.util.HashMap;
import java.util.List;

public class DiscoverEventAdapter extends  RecyclerView.Adapter<DiscoverEventAdapter.DiscoverEventViewHolder> {
    private Context context;
    private List<Post> eventList;
    private HashMap<Integer, Bitmap> eventImageMapping;

    public DiscoverEventAdapter(Context context, List<Post> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.eventImageMapping = new HashMap<>();
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
        Log.i("DiscoverEventAdapter", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_model, parent, false);
        return new DiscoverEventViewHolder(view);
    }

    // bind data for each cardView in Event Recycler
    @Override
    public void onBindViewHolder(@NonNull DiscoverEventViewHolder holder, int position) {
        Log.i("DiscoverEventAdapter", "onBindViewHolder");
        Post eventPost = eventList.get(position);
        if (eventPost != null) {
            Integer post_id = eventPost.getId();
            Bitmap bitmap = eventImageMapping.get(post_id);

            if (bitmap != null) {
                Log.d("DiscoverEventAdapter", String.valueOf(bitmap.getHeight()) + bitmap.getWidth());
            } else {
                Log.d("DiscoverEventAdapter", "bitmap null");
            }

            // set image to UI
            holder.eventImage.setImageBitmap(bitmap);

            // String formatting for CardView
            String rawName = eventPost.getTitle();
            String rawStartTime = eventPost.getEvent_start();
            String rawEndTime = eventPost.getEvent_end();
            String formattedName = CardFormatter.formatTitleDiscover(rawName, CardFormatter.EventCardType.DISCOVER);
            String formattedDate = CardFormatter.formatDate(rawStartTime, rawEndTime);
            String formattedTime = CardFormatter.formatTime(rawStartTime, rawEndTime);

            // Set text in UI
            holder.eventName.setText(formattedName);
            holder.eventDate.setText(formattedDate);
            holder.eventTime.setText(formattedTime);
            holder.eventLocation.setText(eventPost.getLocation());

            // Set on click listener for each item
            // ViewHolder is not a View, so we access its root view instead (itemView)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Go to corresponding events page
                    Intent intent = new Intent(context, EventsPageActivity.class); // Intent for going to events page activity

                    // To pass in Post object, we need to wrap it first (Parcelable)
                    PostParcelable postParcelable = new PostParcelable(eventPost); // can be serialized into Parcel
                    intent.putExtra("POST", postParcelable); // pass in wrapped Post (serializable)
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEventList(List<Post> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    public void updateEventImageMapping(HashMap<Integer, Bitmap> eventImageMapping) {
        this.eventImageMapping = eventImageMapping;
        notifyDataSetChanged();
    }
}