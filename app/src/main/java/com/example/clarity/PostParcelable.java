package com.example.clarity;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.clarity.model.data.Post;

// Wrapper class for Post object that implements Parcelable
// The wrapped Post (a PostParcelable object) can be attached to Intents
public class PostParcelable implements Parcelable {
    /*
    PostParcelable gets serialized into a Parcel object internally when sent through intent
    Then the Parcel objects get deserialized back into PostParcelable object when we receive it
     */

    private Post post; // Post object to be wrapped around
    public Post getPost() {
        return post;
    }

    public PostParcelable(Post post) {
        // This constructor is used for constructing a PostParcelable from existing Post object
        // i.e. wrap a Post object
        this.post = post;
    }

    // Parcelable implementation
    protected PostParcelable(Parcel in) {
        // This constructor is used to reconstruct a PostParcelable object from a Parcel object during deserialization.
        // Gets called by the Creator object (see below)

        // Read the data from Parcel in the same order it was written
        int id = in.readInt();
        int author_id = in.readInt();
        String event_start = in.readString();
        String event_end = in.readString();
        String image_url = in.readString();
        String title = in.readString();
        String location = in.readString();
        String description = in.readString();
        String created_at = in.readString();

        post = new Post(id, author_id, event_start, event_end, image_url, title, location, description, created_at);
    }

    // Creator object manages the reconstruction of the PostParcelable object from the serialized Parcel object during deserialization
    // Calls the alternative constructor
    public static final Creator<PostParcelable> CREATOR = new Creator<PostParcelable>() {
        @Override
        public PostParcelable createFromParcel(Parcel in) {
            return new PostParcelable(in);
        }

        @Override
        public PostParcelable[] newArray(int size) {
            return new PostParcelable[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Responsible for serialization
        // Converts this PostParcelable instance to Parcel object (for internal use, will be deserialized later)
        dest.writeInt(post.getId());
        dest.writeInt(post.getAuthor_id());
        dest.writeString(post.getEvent_start());
        dest.writeString(post.getEvent_end());
        dest.writeString(post.getImage_url());
        dest.writeString(post.getTitle());
        dest.writeString(post.getLocation());
        dest.writeString(post.getDescription());
        dest.writeString(post.getCreated_at());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

