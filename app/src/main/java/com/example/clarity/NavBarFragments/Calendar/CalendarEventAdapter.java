package com.example.clarity.NavBarFragments.Calendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.Event.EventsPageActivity;
import com.example.clarity.PostParcelable;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.util.CardFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.EventViewHolder>{
    // Take data from the source and convert it to individual views to be displayed
    private Context context;
    private List<Post> eventList;
    private String[] daysOfWeekConverter;
    private CalendarFragment.CalendarDisplayState calendarDisplayState;

    public CalendarEventAdapter(Context context, CalendarFragment.CalendarDisplayState calendarDisplayState){
        this.context = context;
        this.eventList = new ArrayList<>(); // list of Post objects (to populate RecyclerView)

        // map Calendar days of week int constant to string values for agenda view:
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

        Post event = eventList.get(position); // only get events from the shownEventsList

        // Bind Event object data to the EventViewHolder i.e. fill in data
        holder.eventNameTextView.setText(event.getTitle());
        holder.eventTimeTextView.setText(CardFormatter.formatTimeWithDate(event.getEvent_start(), event.getEvent_end()));
        holder.eventLocationTextView.setText(event.getLocation());
        holder.eventDayNumber.setText(String.valueOf(event.getEventStart().get(Calendar.DAY_OF_MONTH)));
        holder.eventDay.setText(daysOfWeekConverter[event.getEventStart().get(Calendar.DAY_OF_WEEK)]);

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

        // Set on click listener for each item
        // ViewHolder is not a View, so we access its root view instead (itemView)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Go to corresponding events page
                Intent intent = new Intent(context, EventsPageActivity.class); // Intent for going to events page activity

                // To pass in Post object, we need to wrap it first (Parcelable)
                PostParcelable postParcelable = new PostParcelable(event); // can be serialized into Parcel
                intent.putExtra("POST", postParcelable); // pass in wrapped Post
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // RecyclerView calls this to get the size of the data source
        // This is used to determine when there are no more items that can be displayed
        return eventList.size();
    }

    public void updateEventList(List<Post> eventList) {
        // Called when list of events to display on Recycler changes
        this.eventList = eventList;
        notifyDataSetChanged();
    }

}



