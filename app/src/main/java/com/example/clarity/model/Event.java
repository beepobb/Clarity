package com.example.clarity.model;
// PURELY PLACEHOLDER UNTIL DATABASE IS SET-UP (FOR CALENDAR)
// JUST A CLASS THAT REPRESENTS AN EVENT

public class Event {
    // Placeholder model class to represent an Event object
    private String name;
    private String time;
    private String place;

    public Event(String name, String time, String place) {
        this.name = name;
        this.time = time;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }
}
