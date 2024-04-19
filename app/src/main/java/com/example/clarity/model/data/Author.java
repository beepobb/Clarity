package com.example.clarity.model.data;

public class Author {
    // Implements Serializable so that User object can be serialized into a session token
    private String username;
    private String role;
    private String profile_pic_url;

    public Author(String username, String role, String profile_pic_url) {
        this.username = username;
        this.role = role;
        this.profile_pic_url = profile_pic_url;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    @Override
    public String toString() {
        return username + ":" + role;
    }
}
