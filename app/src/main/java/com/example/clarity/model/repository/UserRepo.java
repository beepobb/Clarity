package com.example.clarity.model.repository;

import com.example.clarity.model.data.Author;
import com.example.clarity.model.data.User;
import com.example.clarity.model.util.MD5;
import com.example.clarity.model.util.UrlUtilMethods;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;

class UserRepo {
    final static String endPointUser = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/user";

    private Executor executor;

    // Access limited to within repo package
    UserRepo(Executor executor) {
        this.executor = executor;
    }

    // Request to check if user is in database.
    void checkUserRequest(int id, RestRepo.RepositoryCallback<Boolean> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Boolean response = checkUser(Integer.toString(id));
                callback.onComplete(response);
            }
        });
    }

    // Request to get User information from database.
    void getUserRequest(String username, String password, RestRepo.RepositoryCallback<User> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                User response = getUser(username, MD5.getMd5(password));
                callback.onComplete(response);
            }
        });
    }

    // Method to get User information from database.
    User getUser(String username, String password) {
        try {
            String urlQuery = "?username="+username+"&password="+password;
            JSONObject tmp = UrlUtilMethods.urlGet(endPointUser,urlQuery);
            System.out.println(tmp);
            return new User(tmp.getInt("id"), tmp.getString("username"),
                    tmp.getString("password"),tmp.getString("role"),tmp.getString("email"),
                    tmp.getString("created_at"), tmp.getString("profile_pic_url"));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Request to get Author of post information from database.
    void getAuthorRequest(int author_id, RestRepo.RepositoryCallback<Author> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Author response = getAuthor(Integer.toString(author_id));
                callback.onComplete(response);
            }
        });
    }

    // Method to get Author of post information from database.
    Author getAuthor(String author_id) {
        try {
            String urlQuery = "?author_id="+author_id;
            JSONObject tmp = UrlUtilMethods.urlGet(endPointUser,urlQuery);
            System.out.println(tmp);
            return new Author(tmp.getString("username"),tmp.getString("role"),tmp.getString("profile_pic_url"));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Request to check if user is in database.
    Boolean checkUser(String id) {
        try {
            String urlQuery = "?id="+id;
            JSONObject tmp = UrlUtilMethods.urlGet(endPointUser,urlQuery);
            String result = tmp.getString("body");
            if(result.equals("True")) {
                return true;
            }
            return false;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Method to add user into database.
    String addUser(String username, String password, String email, String role, String profile_pic_url) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("password", password);
        data.put("email", email);
        data.put("role", role);
        data.put("profile_pic_url", profile_pic_url);
        try {
            JSONObject res = new JSONObject(UrlUtilMethods.urlPost(endPointUser, new JSONObject(data)));
            if(res.getString("body").equals("Error adding data.")) {
                return null;
            }
            return res.getString("body");
        }
        catch(Exception e) {
            return null;
        }
    }
}
