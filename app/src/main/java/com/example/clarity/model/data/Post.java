package com.example.clarity.model.data;

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

    public Post(int id, int author_id, String event_start, String event_end, String image_url, String title, String location, String description, String created_at) {
        this.id = id;
        this.author_id = author_id;
        this.event_start = event_start;
        this.event_end = event_end;
        this.image_url = image_url;
        this.title = title;
        this.location = location;
        this.description = description;
        this.created_at = created_at;
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

    public String getEvent_end() {
        return event_end;
    }

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

    @Override
    public String toString() {
        return title;
    }
}
