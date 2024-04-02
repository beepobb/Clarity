package com.example.clarity.NavBarFragments.Tag_fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;
import java.util.ArrayList;

public class Event_adapter extends RecyclerView.Adapter<Event_adapter.Event_holder> {
    Context context;
    ArrayList<Event_model> eventlist;
    public Event_adapter(Context context, ArrayList<Event_model> event_list){
        this.context = context;
        this.eventlist = event_list;
    }
    @NonNull
    @Override
    public Event_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item_model, parent, false);
        return new Event_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Event_holder holder, int position) {
        holder.name.setText(eventlist.get(position).getEvent_name());
        holder.time.setText(eventlist.get(position).getEvent_time());
        holder.des.setText(eventlist.get(position).getEvent_description());
        holder.imageview.setImageResource(eventlist.get(position).getImage_id());
        holder.loc.setText(eventlist.get(position).getEvent_location());


    }

    @Override
    public int getItemCount() {
        return eventlist.size();
    }

    public static class Event_holder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView name, time, des, loc;
        public Event_holder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageView2);
            name = itemView.findViewById(R.id.eventName);
            time = itemView.findViewById(R.id.eventDate);
            des = itemView.findViewById(R.id.eventTime);
            loc = itemView.findViewById(R.id.eventLocation);

        }
    }
}
