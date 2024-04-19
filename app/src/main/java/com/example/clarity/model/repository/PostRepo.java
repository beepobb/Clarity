package com.example.clarity.model.repository;

import android.graphics.Bitmap;

import com.example.clarity.model.data.Post;
import com.example.clarity.model.util.MD5;
import com.example.clarity.model.util.UrlUtilMethods;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;

class PostRepo {
    final static String endPointPost = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/post";
    private Executor executor;

    // Access limited to within repo package
    PostRepo(Executor executor) {
        this.executor = executor;
    }

    // Getting one post
    void getOnePostRequest(int post_id, RestRepo.RepositoryCallback<Post> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Post response = getOnePost(String.valueOf(post_id));
                callback.onComplete(response);
            }
        });
    }

    // Getting all the posts in database.
    void getAllPostRequest(RestRepo.RepositoryCallback<ArrayList<Post>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Post> response = getAllPost();
                callback.onComplete(response);
            }
        });
    }

    // Getting multiple posts from the given post_id_list.
    void getPostsRequest(ArrayList<Integer> post_id_list, RestRepo.RepositoryCallback<ArrayList<Post>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> casted = new  ArrayList<String>();
                for(Integer ea:post_id_list) {
                    casted.add(String.valueOf(ea));
                }
                String post_id_string = String.join( ",", casted);
                ArrayList<Post> response = getMultiplePost(post_id_string);
                callback.onComplete(response);
            }
        });
    }

    // Method for getting multiple posts from the given post_id_list.
    ArrayList<Post> getMultiplePost(String post_id_list) {
        try {
            String urlQuery = "?post_id_list="+post_id_list;
            JSONObject tmpList = UrlUtilMethods.urlGet(endPointPost,urlQuery);
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

    // Method for getting one post from the given post_id.
    Post getOnePost(String post_id) {
        try {
            String urlQuery = "?id="+post_id;
            JSONObject tmp = UrlUtilMethods.urlGet(endPointPost,urlQuery);
            return new Post(tmp.getInt("id"), tmp.getInt("author_id"),tmp.getString("event_start")
                    ,tmp.getString("event_end"),tmp.getString("image_url"),tmp.getString("title")
                    ,tmp.getString("location"),tmp.getString("description"),tmp.getString("created_at"));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Method for getting all posts from the database.
    ArrayList<Post> getAllPost() {
        try {
            JSONObject tmpList = UrlUtilMethods.urlGet(endPointPost,"");
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

    // Method to add a single post into database.
    String addPost(int author_id, String event_start, String event_end, String image_url, String title,
                           String location, String description, ArrayList<String> tags) {
        String listString = String.join(",", tags);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("author_id", String.valueOf(author_id));
        data.put("event_start", event_start);
        data.put("event_end", event_end);
        data.put("image_url", image_url);
        data.put("title", StringEscapeUtils.escapeJava(title));
        data.put("location", location);
        data.put("description", StringEscapeUtils.escapeJava(description));
        data.put("tag_list", listString);

        try {
            return UrlUtilMethods.urlPost(endPointPost, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
