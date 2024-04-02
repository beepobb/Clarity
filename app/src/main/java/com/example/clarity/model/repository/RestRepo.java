package com.example.clarity.model.repository;

import com.example.clarity.model.data.User;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Favourite;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.lang.Runnable;

public class RestRepo {
    //################STATIC METHODS################/
    static String endPointUser = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/user";
    static String endPointPost = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/post";
    private final Executor executor;

    public RestRepo(Executor executor) {
        this.executor = executor;
    }

    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }

    public static JSONObject urlGet(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL+urlParameters);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoInput(true);
            connection.connect();
            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            System.out.println(response);
            rd.close();
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String urlPost(String targetURL, JSONObject data) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(data.toString());
            wr.flush();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    //################USER METHODS################/
    // NULL indicates failed authentication
    public void getUserRequest(String username, String password, RepositoryCallback<User> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                User response = getUser(username, password);
                callback.onComplete(response);
            }
        });
    }

    public User getUser(String username, String password) {
        try {
            String urlQuery = "?username="+username+"&password="+password;
            JSONObject tmp = urlGet(endPointUser,urlQuery);
            System.out.println(tmp);
            return new User(tmp.getInt("id"), tmp.getString("username"),tmp.getString("password"),tmp.getString("role"),tmp.getString("email"),tmp.getString("created_at"));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void addUserRequest(String username, String password, String email, String role, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addUser(username, password, email, role);
                callback.onComplete(response);
            }
        });
    }

    public String addUser(String username, String password, String email, String role) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("password", password);
        data.put("email", email);
        data.put("role", role);
        try {
            return urlPost(endPointUser, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    //################POST METHODS################/
    // NULL indicates failed authentication
    public void getPostRequest(int post_id, RepositoryCallback<Post> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Post response = getPost(String.valueOf(post_id));
                callback.onComplete(response);
            }
        });
    }

    public Post getPost(String post_id) {
        try {
            String urlQuery = "?id="+post_id;
            JSONObject tmp = urlGet(endPointPost,urlQuery);
            System.out.println(tmp);
            return new Post(tmp.getInt("id"), tmp.getInt("author_id"),tmp.getString("event_start")
                    ,tmp.getString("event_end"),tmp.getString("image_url"),tmp.getString("title")
                    ,tmp.getString("location"),tmp.getString("description"),tmp.getString("created_at"));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void addPostRequest(int author_id, String event_start, String event_end, String image_url, String title,
                               String location, String description,RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addPost(author_id, event_start, event_end, image_url, title, location, description);
                callback.onComplete(response);
            }
        });
    }

    public String addPost(int author_id, String event_start, String event_end, String image_url, String title,
                          String location, String description) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("author_id", String.valueOf(author_id));
        data.put("event_start", event_start);
        data.put("event_end", event_end);
        data.put("image_url", image_url);
        data.put("title", title);
        data.put("location", location);
        data.put("description", description);
        try {
            return urlPost(endPointPost, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }











    // provide image in a specific type given the url.
    // UPDATE FROM UI/UX side regarding which type to display.
    public static void get_image(String url) {

    }

    //################GET METHODS################/

    public Post getPost(int post_id) {
        return null;
    }

    public Favourite[] getFavourites(int user_id) {
        return new Favourite[5];
    }

    //################POST METHODS################/
    // Creating a newpost in data base.
    public boolean createPost(Post new_post) {
        return false;
    }

    public boolean createUser(User new_user) {
        return false;
    }






}
