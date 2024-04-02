package com.example.clarity.model;
// PURELY PLACEHOLDER UNTIL DATABASE IS SET-UP (FOR CALENDAR)
// JUST A CLASS THAT REPRESENTS AN EVENT

import java.util.Calendar;

public class Event {
    // Placeholder model class to represent an Event object
    private String name;
    private String time;
    private String place;
    private Calendar date;



    public Event(String name, String time, String place, Calendar date) {
        this.name = name;
        this.time = time;
        this.place = place;
        this.date = date;
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

    public Calendar getDate() {
        return date;
    }
}
