package com.example.clarity.NavBarFragments.Tag_fragments;

public class Event_model {
    String Event_name;
    String Event_time;
    String Event_description;
    String Location;
    int image_id;

    Event_model(String event_name, String event_time, String event_description, String location, int image_id){
        this.Event_name = event_name;
        this.Event_time = event_time;
        this.Event_description = event_description;
        this.image_id = image_id;
        this.Location = location;
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

    public String getEvent_location(){return Location;}

    public int getImage_id() {
        return image_id;
    }
}
