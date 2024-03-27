package com.example.clarity.NavBarFragments;
import android.graphics.drawable.Drawable;
public class Event_model {
    String Event_name;
    String Event_time;
    String Event_description;
    int image_id;

    Event_model(String event_name, String event_time, String event_description, int image_id){
        this.Event_name = event_name;
        this.Event_time = event_time;
        this.Event_description = event_description;
        this.image_id = image_id;
    }

    public String getEvent_name() {
        return Event_name;
    }

    public String getEvent_time() {
        return Event_time;
    }

    public String getEvent_description() {
        return Event_description;
    }

    public int getImage_id() {
        return image_id;
    }
}
