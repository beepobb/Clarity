package com.example.clarity.model.data;

public class Tag {
    private int post_id;
    private String tag_category;

    public Tag(int post_id, String tag_category) {
        this.post_id = post_id;
        this.tag_category = tag_category;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getTag_category() {
        return tag_category;
    }

    @Override
    public String toString() {
        return tag_category;
    }
}
