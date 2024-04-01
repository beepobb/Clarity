package com.example.clarity.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;
import com.example.clarity.model.Event; // Placeholder Event Class

import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder>{
    // Take data from the source and convert it to individual views to be displayed
    private Context context;
    private List<Event> eventList;
    public CalendarEventAdapter(Context context, List<Event> eventList){
        this.context = context;
        this.eventList = eventList; // Placeholder: list of Event objects (to populate RecyclerView)
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{
        // Each individual element in the list is an EventViewHolder object
        // ViewHolder is a wrapper around the actual view (e.g. the row)
        // Instance variables access the various Views (name, time, location) in the actual view
        TextView eventNameTextView;
        TextView eventTimeTextView;
        TextView eventLocationTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.calendarEventNameTextView);
            eventTimeTextView = itemView.findViewById(R.id.calendarEventTimeTextView);
            eventLocationTextView = itemView.findViewById(R.id.calendarEventLocationTextView);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // RecyclerView calls this to create a new ViewHolder
        // Creates an initialises an EventViewHolder
        // BUT does not fill in the contents i.e. does NOT bind it to specific data yet (this is done by onBindViewHolder)

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // RecyclerView calls this to bind a EventViewHolder to data

        Event event = eventList.get(position); // get an Event object from our data source (eventList)

        // Bind Event object data to the EventViewHolder
        holder.eventNameTextView.setText(event.getName());
        holder.eventTimeTextView.setText(event.getTime());
        holder.eventLocationTextView.setText(event.getPlace());

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
        return eventList.size();
    }

    interface onItemClickListener{
        void onItemClick(View view, int position);
    }

}



