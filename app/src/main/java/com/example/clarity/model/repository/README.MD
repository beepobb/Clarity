public void getUserRequest(String username, String password, RepositoryCallback<User> callback)

public void addUserRequest(String username, String password, String email, String role, 
RepositoryCallback<String> callback)

public void getOnePostRequest(int post_id, RepositoryCallback<Post> callback)

public void getPostsRequest(ArrayList<Integer> post_id_list, RepositoryCallback<ArrayList<Post>> callback)

public void getAllPostRequest(RepositoryCallback<ArrayList<Post>> callback)

public void addPostRequest(int author_id, String event_start, String event_end, String image_url, String title,
String location, String description, ArrayList<String> tags, RepositoryCallback<String> callback)

public void getFavouritesRequest(int user_id, RepositoryCallback<ArrayList<Post>> callback)

public void addFavouritesRequest(int post_id, int user_id, RepositoryCallback<String> callback)

public void getPostsWithTagRequest(String category, RepositoryCallback<ArrayList<Post>> callback)

public void getTagsWithPostIDRequest(int post_id, RepositoryCallback<ArrayList<Tag>> callback)

public void addTagsRequest(int post_id, String category, RepositoryCallback<String> callback)