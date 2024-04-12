package com.example.clarity.model.data;
public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String created_at;
    private String profile_pic_url;

    public User(int id, String username, String password, String role, String email, String created_at, String profile_pic_url) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.created_at = created_at;
        this.profile_pic_url = profile_pic_url;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }
}

