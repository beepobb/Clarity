package com.example.clarity.model.repository;

import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Tag;
import com.example.clarity.model.util.UrlUtilMethods;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;

class TagsRepo {
    private Executor executor;
    final static String endPointTags = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/tags";

    // Access limited to within repo package
    TagsRepo(Executor executor) {
        this.executor = executor;
    }

    // Get all the posts with a certain category.
    void getPostsWithTagRequest(String category, RestRepo.RepositoryCallback<ArrayList<Post>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Post> response = getPostsWithTag(category);
                callback.onComplete(response);
            }
        });
    }

    // Method to Get all the posts with a certain category.
    ArrayList<Post> getPostsWithTag(String category) {
        try {
            JSONObject tmpList = UrlUtilMethods.urlGet(endPointTags,"?tag_category="+category);
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

    // Request to Get all the posts with their category.
    void getAllPostsWithTagRequest(RestRepo.RepositoryCallback<ArrayList<Tag>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Tag> response = getAllPostsWithTag();
                callback.onComplete(response);
            }
        });
    }

    // Method to Get all the posts with their category.
    ArrayList<Tag> getAllPostsWithTag() {
        try {
            JSONObject tmpList = UrlUtilMethods.urlGet(endPointTags, "");
            ArrayList<Tag> result = new ArrayList<>();
            for (Iterator<String> it = tmpList.keys(); it.hasNext(); ) {
                JSONObject tmp = tmpList.getJSONObject(it.next());
                result.add(new Tag(tmp.getInt("post_id"), tmp.getString("tag_category")));
            }
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Request to Get all the categories of a certain post.
    void getTagsWithPostIDRequest(int post_id, RestRepo.RepositoryCallback<ArrayList<Tag>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Tag> response = getTagsWithPostID(String.valueOf(post_id));
                callback.onComplete(response);
            }
        });
    }

    // Method to Get all the categories of a certain post.
    ArrayList<Tag> getTagsWithPostID(String post_id) {
        int post_id_int = Integer.parseInt(post_id);
        try {
            JSONObject tmpList = UrlUtilMethods.urlGet(endPointTags,"?post_id="+post_id);
            ArrayList<Tag> result = new ArrayList<Tag>();
            for (Iterator<String> it = tmpList.keys(); it.hasNext(); ) {
                JSONObject tmp = tmpList.getJSONObject(it.next());
                result.add(
                        new Tag(post_id_int, tmp.getString("tag_category"))
                );
            }
            return result;
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Request to add category to a certain post.
    void addTagsRequest(int post_id, String category, RestRepo.RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addTags(String.valueOf(post_id),category);
                callback.onComplete(response);
            }
        });
    }

    // Method to add category to a certain post.
    String addTags(String post_id, String category) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("post_id", post_id);
        data.put("tag_category", category);
        try {
            return UrlUtilMethods.urlPost(endPointTags, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
