package com.example.clarity.model.repository;

import com.example.clarity.model.data.user;
import com.example.clarity.model.data.post;
import com.example.clarity.model.data.tag;
import com.example.clarity.model.data.favourite;

class restrepo {
    //################STATIC METHODS################/
    // provide image in a specific type given the url.
    // UPDATE FROM UI/UX side regarding which type to display.
    public static void get_image(String url) {

    }

    //################GET METHODS################/
    // Get all the user data given as a user object
    // Have to provide username and password.
    // Null indicates failed authentication.
    public user getUser(String username, String password) {
        return null;
    }

    public post getPost(int post_id) {
        return null;
    }

    public favourite[] getFavourites(int user_id) {
        return new favourite[5];
    }

    //################POST METHODS################/
    // Creating a newpost in data base.
    public boolean createPost(post new_post) {
        return false;
    }

    public boolean createUser(user new_user) {
        return false;
    }






}
