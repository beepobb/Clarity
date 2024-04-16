package com.example.clarity.model.data;

import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Post {
    private int id;
    private int author_id;
    private String event_start;
    private String event_end;
    private String image_url;
    private String title;
    private String location;
    private String description;
    private String created_at;

    // Calendar object attributes (for ease of use):
    private Calendar eventStartCal;
    private Calendar eventEndCal;
    private Calendar createdAtCal;

    public Post(int id, int author_id, String event_start, String event_end, String image_url, String title, String location, String description, String created_at) {
        this.id = id;
        this.author_id = author_id;
        this.event_start = event_start;
        this.event_end = event_end;
        this.image_url = image_url;
        this.title = StringEscapeUtils.unescapeJava(title);
        this.location = location;
        this.description = StringEscapeUtils.unescapeJava(description);
        this.created_at = created_at;

        // Initialize Calendar objects for event_start, event_end, and created_at attributes
        // We expect Strings in the ISO 8601 format ("yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            eventStartCal = Calendar.getInstance();
            eventStartCal.setTime(dateFormat.parse(event_start));

            eventEndCal = Calendar.getInstance();
            eventEndCal.setTime(dateFormat.parse(event_end));

            createdAtCal = Calendar.getInstance();
            createdAtCal.setTime(dateFormat.parse(created_at));
        } catch (ParseException e) {
            Log.d("RYAN TEST", "Post: Parse Exception " + event_end);
        }
    }

    public int getId() {
        return id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public String getEvent_start() {
        return event_start;
    }
    public Calendar getEventStart() {return eventStartCal;}

    public String getEvent_end() {
        return event_end;
    }
    public Calendar getEventEnd() {return eventEndCal;}

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }
    public Calendar getCreatedAt() {return createdAtCal;}

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        // Two Post objects are considered equal if id and title are the same

        if (this == o) return true; // same object in memory
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && Objects.equals(title, post.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

}
