package com.example.clarity.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.NavBarFragments.CalendarFragment;
import com.example.clarity.R;
import com.example.clarity.model.Event; // Placeholder Event Class

import java.util.Calendar;
import java.util.List;

/// OLD ADAPTER THAT USED THE PLACEHOLDER Event CLASS BEFORE DATABASE WAS IMPLEMENTED
public class CalendarEventAdapterOld extends RecyclerView.Adapter<CalendarEventAdapterOld.EventViewHolder>{
    // Take data from the source and convert it to individual views to be displayed
    private Context context;
    private List<Event> eventList;
    private String[] daysOfWeekConverter;
    private CalendarFragment.CalendarDisplayState calendarDisplayState;
    public CalendarEventAdapterOld(Context context, List<Event> eventList, CalendarFragment.CalendarDisplayState calendarDisplayState){
        this.context = context;
        this.eventList = eventList; // Placeholder: list of Event objects (to populate RecyclerView)
        // map Calendar days of week int constant to string values for agenda view
        this.daysOfWeekConverter = new String[] {"Error", "WED", "THU", "FRI", "SAT", "SUN", "MON", "TUE"};
        this.calendarDisplayState = calendarDisplayState;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{
        // Each individual element in the list is an EventViewHolder object
        // ViewHolder is a wrapper around the actual view (e.g. the row)
        // Instance variables access the various Views (name, time, location) in the actual view
        TextView eventNameTextView;
        TextView eventTimeTextView;
        TextView eventLocationTextView;
        TextView eventDayNumber;
        TextView eventDay;
        View eventDateLabel; // used in agenda view only

        public EventViewHolder(@NonNull View itemView) {
            // itemView is a reference to the layout for the list item (from LayoutInflater when executing onCreateViewHolder)
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.calendarEventNameTextView);
            eventTimeTextView = itemView.findViewById(R.id.calendarEventTimeTextView);
            eventLocationTextView = itemView.findViewById(R.id.calendarEventLocationTextView);
            eventDayNumber = itemView.findViewById(R.id.eventDayNumber);
            eventDay = itemView.findViewById(R.id.eventDay);
            eventDateLabel = itemView.findViewById(R.id.eventDateLabel);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // RecyclerView calls this to create a new ViewHolder
        // Creates an initialises an EventViewHolder
        // BUT does not fill in the contents i.e. does NOT bind it to specific data yet (this is done by onBindViewHolder)

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_event, parent, false);
        // Inflating calendar_event xml essentially creates a reference to the layout in memory
        // 'view' is a reference to the layout
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // RecyclerView calls this to bind a EventViewHolder to data

        Event event = eventList.get(position); // get an Event object from our data source (eventList)

        // Bind Event object data to the EventViewHolder ie fill in data
        holder.eventNameTextView.setText(event.getName());
        holder.eventTimeTextView.setText(event.getTime());
        holder.eventLocationTextView.setText(event.getPlace());
        holder.eventDayNumber.setText(String.valueOf(event.getDate().get(Calendar.DAY_OF_MONTH)));
        holder.eventDay.setText(daysOfWeekConverter[event.getDate().get(Calendar.DAY_OF_MONTH)]);

        // hide elements based on monthly or agenda view
        if (this.calendarDisplayState == CalendarFragment.CalendarDisplayState.MONTHLY_VIEW) {
            Log.d("onBindViewHolder", CalendarFragment.CalendarDisplayState.MONTHLY_VIEW.name() + " " + holder.eventDateLabel);
            holder.eventDateLabel.setVisibility(View.GONE);
            holder.eventDay.setVisibility(View.GONE);
            holder.eventDayNumber.setVisibility(View.GONE);
        } else if (this.calendarDisplayState == CalendarFragment.CalendarDisplayState.AGENDA_VIEW) {
            Log.d("onBindViewHolder", CalendarFragment.CalendarDisplayState.AGENDA_VIEW.name() + " " + holder.eventDateLabel);
            holder.eventDateLabel.setVisibility(View.VISIBLE);
            holder.eventDay.setVisibility(View.VISIBLE);
            holder.eventDayNumber.setVisibility(View.VISIBLE);
        }

        // Set on click listener
        // ViewHolder is not a View, so we access its root view instead
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To be set
                Toast.makeText(context, holder.eventNameTextView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // RecyclerView calls this to get the size of the data source
        // This is used to determine when there are no more items that can be displayed
        return eventList.size();
    }

    public void setData(List<Event> eventList){
        // Update the items in the RecyclerView
        this.eventList = eventList;
        notifyDataSetChanged();
    }

}




