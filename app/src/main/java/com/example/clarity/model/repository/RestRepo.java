package com.example.clarity.model.repository;


import androidx.appcompat.app.AppCompatActivity;
import com.example.clarity.databinding.ActivityMainBinding;

import android.graphics.Bitmap;

import com.example.clarity.model.data.Author;
import com.example.clarity.model.data.User;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Tag;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.lang.Runnable;

import com.example.clarity.model.util.MD5;

public class RestRepo {
    // Nested interface that is relevant to the RestRepo class.
    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }

    private final Executor executor;
    private static RestRepo instance; // single instance
    private ImageRepo imageRepo;
    private PostRepo postRepo;
    private TagsRepo tagsRepo;
    private UserRepo userRepo;
    private FavRepo favRepo;
    private GenAISummary genAISummary;


    // Singleton Design Pattern: only one instance of RestRepo across activities
    private RestRepo(Executor executor) {
        this.executor = executor;
        imageRepo = new ImageRepo(executor);
        postRepo = new PostRepo(executor);
        tagsRepo = new TagsRepo(executor);
        userRepo = new UserRepo(executor);
        favRepo = new FavRepo(executor);
        genAISummary = new GenAISummary(executor);
    }

    // Singleton Design Pattern: only one instance of RestRepo across activities
    public static RestRepo getInstance(Executor executor) {
        if (instance == null) {
            instance = new RestRepo(executor);
        }
        return instance;
    }

    //################USER Requests################/
    public void checkUserRequest(int id, RepositoryCallback<Boolean> callback) {
        userRepo.checkUserRequest(id, callback);
    }

    public void getUserRequest(String username, String password, RepositoryCallback<User> callback) {
        userRepo.getUserRequest(username, password, callback);
    }

    public void getAuthorRequest(int author_id, RepositoryCallback<Author> callback) {
        userRepo.getAuthorRequest(author_id, callback);
    }

    // Request is here because it contains both add image and add user.
    public void addUserRequest(String username, String password, String email, String role, Bitmap bm, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String filename = username + ".png";
                    String url = ImageRepo.imageBucket + '/' + filename;
                    String response = userRepo.addUser(username, MD5.getMd5(password), email, role, url);
                    if(response != null) {
                        imageRepo.addImage(bm, filename);
                    }
                    callback.onComplete(response);
                }
                catch (Exception e) {
                    callback.onComplete("error");
                }
            }
        });
    }


    //################POST REQUEST################/
    // Getting one post.
    public void getOnePostRequest(int post_id, RepositoryCallback<Post> callback) {
        postRepo.getOnePostRequest(post_id, callback);
    }

    // Getting all post.
    public void getAllPostRequest(RepositoryCallback<ArrayList<Post>> callback) {
        postRepo.getAllPostRequest(callback);
    }

    // Getting multiple post
    public void getPostsRequest(ArrayList<Integer> post_id_list, RepositoryCallback<ArrayList<Post>> callback) {
        postRepo.getPostsRequest(post_id_list, callback);
    }

    // Request is here because it contains both add image and add post.
    public void addPostRequest(int author_id, String event_start, String event_end, String title,
                               String location, String description, ArrayList<String> tags, Bitmap picture, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String item_hash = MD5.getMd5(Integer.toString(author_id) + title + description);
                    String filename = item_hash + ".png";
                    imageRepo.addImage(picture, filename);
                    String image_url = ImageRepo.imageBucket + "/" + filename;
                    String response = postRepo.addPost(author_id, event_start, event_end, image_url, title, location, description, tags);
                    callback.onComplete(response);
                }
                catch(Exception e){
                    callback.onComplete("");
                }
            }
        });
    }
    //################ END POST REQUEST ################/

    //################Favourites METHODS################/
    // Get the favourite posts of a user.
    public void getFavouritesRequest(int user_id, RepositoryCallback<ArrayList<Post>> callback) {
        favRepo.getFavouritesRequest(user_id,callback);
    }

    // Add a post to a user's favourite.
    public void addFavouritesRequest(int post_id, int user_id, RepositoryCallback<String> callback) {
        addFavouritesRequest(post_id, user_id, callback);
    }

    // Remove a favourite post from user.
    public void removeFavouritesRequest(int post_id, int user_id, RepositoryCallback<String> callback) {
        favRepo.removeFavouritesRequest(post_id, user_id, callback);
    }
    //################Favourites REQUESTS################/

    //################Tag METHODS################/
    // Get all the posts with a certain category.
    public void getPostsWithTagRequest(String category, RepositoryCallback<ArrayList<Post>> callback) {
        tagsRepo.getPostsWithTagRequest(category, callback);
    }

    // Get all the posts and their categories as Tag object.
    public void getAllPostsWithTagRequest(RepositoryCallback<ArrayList<Tag>> callback) {
        tagsRepo.getAllPostsWithTagRequest(callback);
    }

    // Get all tags of a post.
    public void getTagsWithPostIDRequest(int post_id, RepositoryCallback<ArrayList<Tag>> callback) {
        tagsRepo.getTagsWithPostIDRequest(post_id, callback);
    }

    // Add a certain tag to the database.
    public void addTagsRequest(int post_id, String category, RepositoryCallback<String> callback) {
        tagsRepo.addTagsRequest(post_id,category,callback);
    }

    // #############IMAGE METHODS##############
    // Add image into data base.
    public void postImageRequest(Bitmap bmp, String filename, RepositoryCallback<String> callback) {
        imageRepo.postImageRequest(bmp, filename, callback);
    }

    // Get the profile picture of a user. Requires interfaction of two diff repo. Left to RestRepo.
    public void getProfilePictureRequest(int userID, RepositoryCallback<Bitmap> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Author author = userRepo.getAuthor(Integer.toString(userID));
                    Bitmap bm = imageRepo.getImage(author.getProfile_pic_url());
                    callback.onComplete(bm);
                }
                catch(Exception e) {
                    callback.onComplete(null);
                }
            }
        });
    }

    // Get the bitmap according to a certain url.
    public void getImageRequest(String url, RepositoryCallback<Bitmap> callback) {
        imageRepo.getImageRequest(url, callback);
    }

    // get the summary string according to the bitmap of a poster.
    public void bitmapToTextSummaryRequest(Bitmap bm, RepositoryCallback<String> callback) {
        genAISummary.bitmapToTextSummaryRequest(bm, callback);
    }
}
