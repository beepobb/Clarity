package com.example.clarity.NavBarFragments;

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

public class Event_adapter extends RecyclerView.Adapter<Event_adapter.MyViewHolder> {
    Context context;
    ArrayList<Event_model> eventlist;
    public Event_adapter(Context context, ArrayList<Event_model> event_list){
        this.context = context;
        this.eventlist = event_list;
    }
    @NonNull
    @Override
    public Event_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new Event_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Event_adapter.MyViewHolder holder, int position) {
        holder.name.setText(eventlist.get(position).getEvent_name());
        holder.time.setText(eventlist.get(position).getEvent_time());
        holder.des.setText(eventlist.get(position).getEvent_description());
        holder.imageview.setImageResource(eventlist.get(position).getImage_id());


    }

    @Override
    public int getItemCount() {
        return eventlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView name, time, des;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageView2);
            name = itemView.findViewById(R.id.textView1);
            time = itemView.findViewById(R.id.textView2);
            des = itemView.findViewById(R.id.textView3);

        }
    }
}
