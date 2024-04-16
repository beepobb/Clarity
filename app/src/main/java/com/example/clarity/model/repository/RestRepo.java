package com.example.clarity.model.repository;

import androidx.appcompat.app.AppCompatActivity;
import com.example.clarity.databinding.ActivityMainBinding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.clarity.R;
import com.example.clarity.model.data.Author;
import com.example.clarity.model.data.User;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Favourite;
import com.example.clarity.model.data.Tag;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.lang.Runnable;

import com.example.clarity.model.util.MD5;

public class RestRepo {
    private final String endPointUser = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/user";
    private final String endPointPost = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/post";
    private final String endPointFavourites = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/favourites";
    private final String endPointTags = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/tags";
    private final String imageBucket = "https://18ihlowsu4.execute-api.ap-southeast-2.amazonaws.com/beta/sutdclarity";
    private final String endPointSummary = "https://ixx239v32j.execute-api.ap-southeast-2.amazonaws.com/beta/summary";

    private final Executor executor;
    private static RestRepo instance; // single instance

    // Singleton Design Pattern: only one instance of RestRepo across activities
    private RestRepo(Executor executor) {
        this.executor = executor;
    }

    //################STATIC METHODS################/
    public static RestRepo getInstance(Executor executor) {
        if (instance == null) {
            instance = new RestRepo(executor);
        }
        return instance;
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

    public static String urlGetImage(String targetURL) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
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

    public static String urlPost(String targetURL, JSONObject data) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            String data_string = data.toString();
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestProperty("content-length", Integer.toString(data_string.length()));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(data_string);
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

    public static String urlPutImage(String targetURL, String data) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(data);
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
            System.out.println("response" + response.toString() );
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

    public static String urlDelete(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL + urlParameters);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

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
    public void checkUserRequest(int id, RepositoryCallback<Boolean> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Boolean response = checkUser(Integer.toString(id));
                callback.onComplete(response);
            }
        });
    }

    public void getUserRequest(String username, String password, RepositoryCallback<User> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                User response = getUser(username, MD5.getMd5(password));
                callback.onComplete(response);
            }
        });
    }

    private User getUser(String username, String password) {
        try {
            String urlQuery = "?username="+username+"&password="+password;
            JSONObject tmp = urlGet(endPointUser,urlQuery);
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

    public void getAuthorRequest(int author_id, RepositoryCallback<Author> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Author response = getAuthor(Integer.toString(author_id));
                callback.onComplete(response);
            }
        });
    }

    private Author getAuthor(String author_id) {
        try {
            String urlQuery = "?author_id="+author_id;
            JSONObject tmp = urlGet(endPointUser,urlQuery);
            System.out.println(tmp);
            return new Author(tmp.getString("username"),tmp.getString("role"),tmp.getString("profile_pic_url"));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    private Boolean checkUser(String id) {
        try {
            String urlQuery = "?id="+id;
            JSONObject tmp = urlGet(endPointUser,urlQuery);
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


    public void addUserRequest(String username, String password, String email, String role, Bitmap bm, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String filename = username + ".png";
                    String url = imageBucket + '/' + filename;
                    String response = addUser(username, MD5.getMd5(password), email, role, url);
                    if(response != null) {
                        addImage(bm, filename);
                    }
                    callback.onComplete(response);
                }
                catch (Exception e) {
                    callback.onComplete("error");
                }
            }
        });
    }

    private String addUser(String username, String password, String email, String role, String profile_pic_url) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("password", password);
        data.put("email", email);
        data.put("role", role);
        data.put("profile_pic_url", profile_pic_url);
        try {
            JSONObject res = new JSONObject(urlPost(endPointUser, new JSONObject(data)));
            if(res.getString("body").equals("Error adding data.")) {
                return null;
            }
            return res.getString("body");
        }
        catch(Exception e) {
            return null;
        }
    }

    //################END USER METHODS################/


    //################POST METHODS################/
    // Getting one post
    public void getOnePostRequest(int post_id, RepositoryCallback<Post> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Post response = getOnePost(String.valueOf(post_id));
                callback.onComplete(response);
            }
        });
    }

    // Getting multiple post
    public void getAllPostRequest(RepositoryCallback<ArrayList<Post>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Post> response = getAllPost();
                callback.onComplete(response);
            }
        });
    }

    // Getting multiple post
    public void getPostsRequest(ArrayList<Integer> post_id_list, RepositoryCallback<ArrayList<Post>> callback) {
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

    private ArrayList<Post> getMultiplePost(String post_id_list) {
        try {
            String urlQuery = "?post_id_list="+post_id_list;
            JSONObject tmpList = urlGet(endPointPost,urlQuery);
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

    private Post getOnePost(String post_id) {
        try {
            String urlQuery = "?id="+post_id;
            JSONObject tmp = urlGet(endPointPost,urlQuery);
            return new Post(tmp.getInt("id"), tmp.getInt("author_id"),tmp.getString("event_start")
                    ,tmp.getString("event_end"),tmp.getString("image_url"),tmp.getString("title")
                    ,tmp.getString("location"),tmp.getString("description"),tmp.getString("created_at"));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private ArrayList<Post> getAllPost() {
        try {
            JSONObject tmpList = urlGet(endPointPost,"");
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

    public void addPostRequest(int author_id, String event_start, String event_end, String title,
                               String location, String description, ArrayList<String> tags, Bitmap picture, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String item_hash = MD5.getMd5(Integer.toString(author_id) + title + description);
                    String filename = item_hash + ".png";
                    String response = addImage(picture, filename);
                    String image_url = imageBucket + "/" + filename;
                    System.out.println(image_url);
                    response = addPost(author_id, event_start, event_end, image_url, title, location, description, tags);
                    callback.onComplete(response);

                }
                catch(Exception e){
                    System.out.println("error" + e.getMessage());
                    callback.onComplete("");
                }
            }
        });
    }

    private String addPost(int author_id, String event_start, String event_end, String image_url, String title,
                          String location, String description, ArrayList<String> tags) {
        String listString = String.join(",", tags);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("author_id", String.valueOf(author_id));
        data.put("event_start", event_start);
        data.put("event_end", event_end);
        data.put("image_url", image_url);
        data.put("title", title);
        data.put("location", location);
        data.put("description", description);
        data.put("tag_list", listString);
        try {
            return urlPost(endPointPost, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //################END POST METHODS################/

    //################Favourites METHODS################/
    public void getFavouritesRequest(int user_id, RepositoryCallback<ArrayList<Post>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Post> response = getFavourites(String.valueOf(user_id));
                callback.onComplete(response);
            }
        });
    }

    private ArrayList<Post> getFavourites(String user_id) {
        try {
            JSONObject tmpList = urlGet(endPointFavourites,"?user_id="+user_id);
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

    public void addFavouritesRequest(int post_id, int user_id, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addFavourites(String.valueOf(post_id), String.valueOf(user_id));
                callback.onComplete(response);
            }
        });
    }

    private String addFavourites(String post_id, String user_id) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("post_id", post_id);
        data.put("user_id", user_id);
        try {
            return urlPost(endPointFavourites, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void removeFavouritesRequest(int post_id, int user_id, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = removeFavourites(String.valueOf(post_id), String.valueOf(user_id));
                callback.onComplete(response);
            }
        });
    }

    private String removeFavourites(String post_id, String user_id) {
        String urlParameter = String.format("?post_id=%s&user_id=%s",post_id,user_id);
        try {
            return urlDelete(endPointFavourites, urlParameter);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //################Favourites METHODS################/

    //################Tag METHODS################/
    public void getPostsWithTagRequest(String category, RepositoryCallback<ArrayList<Post>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Post> response = getPostsWithTag(category);
                callback.onComplete(response);
            }
        });
    }

    private ArrayList<Post> getPostsWithTag(String category) {
        try {
            JSONObject tmpList = urlGet(endPointTags,"?tag_category="+category);
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

    public void getAllPostsWithTagRequest(RepositoryCallback<ArrayList<Tag>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Tag> response = getAllPostsWithTag();
                callback.onComplete(response);
            }
        });
    }

    private ArrayList<Tag> getAllPostsWithTag() {
        try {
            JSONObject tmpList = urlGet(endPointTags, "");
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

    public void getTagsWithPostIDRequest(int post_id, RepositoryCallback<ArrayList<Tag>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Tag> response = getTagsWithPostID(String.valueOf(post_id));
                callback.onComplete(response);
            }
        });
    }

    private ArrayList<Tag> getTagsWithPostID(String post_id) {
        int post_id_int = Integer.parseInt(post_id);
        try {
            JSONObject tmpList = urlGet(endPointTags,"?post_id="+post_id);
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

    public void addTagsRequest(int post_id, String category, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addTags(String.valueOf(post_id),category);
                callback.onComplete(response);
            }
        });
    }

    private String addTags(String post_id, String category) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("post_id", post_id);
        data.put("tag_category", category);
        try {
            return urlPost(endPointTags, new JSONObject(data));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    // #############IMAGE METHODS##############
    // provide image in a specific type given the url.
    // UPDATE FROM UI/UX side regarding which type to display
    public void postImageRequest(Bitmap bmp, String filename, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String response = addImage(bmp, filename);
                callback.onComplete(response);
            }
        });
    }

    private String addImage(Bitmap bmp, String filename) {
        try {
            String fileType = getFileType(filename);
            Bitmap.CompressFormat format;
            if(fileType.equals("png")) {
                format = Bitmap.CompressFormat.PNG;
            }
            else if(fileType.equals("jpeg")) {
                format = Bitmap.CompressFormat.JPEG;
            }
            else{
                return "";
            }
            String image_data = getStringImage(bmp, format);
            return urlPutImage(imageBucket+"/" +filename ,image_data);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public void getProfilePictureRequest(int userID, RepositoryCallback<Bitmap> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Author author = getAuthor(Integer.toString(userID));
                    Bitmap bm = getImage(author.getProfile_pic_url());
                    callback.onComplete(bm);
                }
                catch(Exception e) {
                    callback.onComplete(null);
                }
            }
        });

    }

    public void getImageRequest(String url, RepositoryCallback<Bitmap> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(getImage(url));
            }
        });
    }

    private Bitmap getImage(String url) {
        try {
            String encodedImage = urlGetImage(url);
            return getImageFromString(encodedImage);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private  String getStringImage(Bitmap bmp, Bitmap.CompressFormat format){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, out);
        byte[] imageBytes = out.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private  Bitmap getImageFromString(String encodedImage){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private String getFileType(String filename) {
        int index = filename.lastIndexOf('.');
        if(index > 0) {
            return filename.substring(index + 1);
        }
        return "";
    }

    public void  bitmapToTextSummaryRequest(Bitmap bm, RepositoryCallback<String> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete("Updated");
//                try {
//                    String base64Text = getStringImage(bm, Bitmap.CompressFormat.JPEG);
//                    base64Text = base64Text.replace("\n", "");
//                    HashMap<String, String> data = new HashMap<String, String>();
//                    data.put("document", base64Text);
//                    String response = urlPost(endPointSummary, new JSONObject(data));
//                    JSONObject tmp = new JSONObject(response);
//                    String result = tmp.getString("summary");
//                    callback.onComplete(result.substring(1,result.length() - 1));
//                }
//                catch(Exception e) {
//                    callback.onComplete("");
//                }
            }
        });
    }
}
