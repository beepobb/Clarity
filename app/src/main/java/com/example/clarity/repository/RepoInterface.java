package com.example.clarity.repository;

import com.example.clarity.model.Favourite;
import com.example.clarity.model.Post;
import com.example.clarity.model.User;

public interface RepoInterface {
    // Database class

    // provide image in a specific type given the url.
    // UPDATE FROM UI/UX side regarding which type to display.
    public static void get_image(String url) {}

    //################GET METHODS################/
    // Get all the user data given as a user object
    // Have to provide username and password.
    // Null indicates failed authentication.
    public User getUser(String username, String password);

    public Post getPost(int post_id);

    public Favourite[] getFavourites(int user_id);

    //################POST METHODS################/
    // Creating a new post in data base.
    public boolean createPost(Post new_post);

    public boolean createUser(User new_user);
}
