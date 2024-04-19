package com.example.clarity.model.repository;

import com.example.clarity.model.data.Post;
import com.example.clarity.model.util.UrlUtilMethods;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;


// FavRepo class is responsible to interact with the AWSAPI endpoint to get favourite information only.
class FavRepo {
    final String endPointFavourites = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/favourites";
    Executor executor;

    // Access limited to within repo package
    FavRepo(Executor executor) {
        this.executor = executor;
    }

    // Request to get ArrayList<Post> liked for the user.
    void getFavouritesRequest(int user_id, RestRepo.RepositoryCallback<ArrayList<Post>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Post> response = getFavourites(String.valueOf(user_id));
                callback.onComplete(response);
            }
        });
    }

    // method to Get ArrayList<Post> liked for the user.
    ArrayList<Post> getFavourites(String user_id) {
        try {
            JSONObject tmpList = UrlUtilMethods.urlGet(endPointFavourites,"?user_id="+user_id);
            ArrayList<Post> result = new ArrayList<Post>();
            for (Iterator<String> it = tmpList.keys(); it.hasNext(); ) {
                JSONObject tmp = tmpList.getJSONObject(it.next());
                result.add(
                        new Post(tmp.getInt("id"), tmp.getInt("author_id"),tmp.getString("event_start")
                                ,tmp.getString("event_end"),tmp.getString("image_url"),tmp.getString("title")
                                ,tmp.getString("location"),tmp.getString("description"),tmp.getString("created_at"))
                );

            }
            return result;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Request to add the favourite data into database.
    void addFavouritesRequest(int post_id, int user_id, RestRepo.RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addFavourites(String.valueOf(post_id), String.valueOf(user_id));
                callback.onComplete(response);
            }
        });
    }

    // Method to add the favourite data into database.
    String addFavourites(String post_id, String user_id) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("post_id", post_id);
        data.put("user_id", user_id);
        try {
            return UrlUtilMethods.urlPost(endPointFavourites, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Request to remove the favourite data from database.
    void removeFavouritesRequest(int post_id, int user_id, RestRepo.RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = removeFavourites(String.valueOf(post_id), String.valueOf(user_id));
                callback.onComplete(response);
            }
        });
    }

    // Method to remove the favourite data from database.
    String removeFavourites(String post_id, String user_id) {
        String urlParameter = String.format("?post_id=%s&user_id=%s",post_id,user_id);
        try {
            return UrlUtilMethods.urlDelete(endPointFavourites, urlParameter);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
