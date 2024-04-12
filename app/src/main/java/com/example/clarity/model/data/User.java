package com.example.clarity.model.data;

import java.io.Serializable;

public class User implements Serializable {
    // Implements Serializable so that User object can be serialized into a session token
    private int id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String created_at;

    public User(int id, String username, String password, String role, String email, String created_at) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.created_at = created_at;
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
}

