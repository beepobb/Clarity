package com.example.clarity.Event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.MyApplication;
import com.example.clarity.MyDataRepository;
import com.example.clarity.PostParcelable;
import com.example.clarity.R;
import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.Author;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Tag;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;


public class EventsPageActivity extends AppCompatActivity {
    private String TAG = "EventsPageActivity";
    private PreferenceUtils prefUtils;
    private MyDataRepository dataRepo;
    private RestRepo db;
    private User appUser;

    // TODO: Define Views
    private ImageView eventImageView;
    private TextView eventNameTextView,eventLocationTextView,eventDateTimeTextView,
            eventDescriptionTextView,organiserNameTextView;
    private ShapeableImageView organiserPictureImageView;
    private ToggleButton addButtonView;
    private ToggleButton likeButtonView;
    private RecyclerView tagRecycler;
    private EventTagAdapter eventTagAdapter;
    private MutableLiveData<String> organiserUsernameLiveData;
    private MutableLiveData<ArrayList<Tag>> categoryListLiveData;
    private MutableLiveData<Bitmap> organiserProfilePictureLiveData;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page_remake);

        // TODO: fetch Views
        addButtonView = findViewById(R.id.addToggleButton);
        likeButtonView = findViewById(R.id.likeToggleButton);
        eventImageView = findViewById(R.id.eventImageView);
        eventNameTextView = findViewById(R.id.eventNameTextView);
        eventLocationTextView = findViewById(R.id.eventLocationTextView);
        eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView);
        eventDateTimeTextView = findViewById(R.id.eventDateTimeTextView);
        tagRecycler = findViewById(R.id.event_tag_recycler);
        organiserPictureImageView = findViewById(R.id.organiserPictureImageView);
        organiserNameTextView = findViewById(R.id.organiserNameTextView);

        // Initialize other attributes
        db = ((MyApplication) getApplicationContext()).getDatabase();
        prefUtils = PreferenceUtils.getInstance(this);
        appUser = ((MyApplication) getApplicationContext()).getAppUser();
        dataRepo = MyDataRepository.getInstance();
        categoryListLiveData = new MutableLiveData<>(new ArrayList<>());
        organiserProfilePictureLiveData = new MutableLiveData<>();
        organiserUsernameLiveData = new MutableLiveData<>();

        // Get Post object from intent (Event post to display)
        Intent intent = getIntent();
        PostParcelable postParcelable = intent.getParcelableExtra("POST");
        assert postParcelable != null;
        Post post = postParcelable.getPost();
        int organiser = post.getAuthor_id();

        // TODO: Bind Post data to Views
        eventNameTextView.setText(post.getTitle());
        eventLocationTextView.setText(post.getLocation());
        eventDateTimeTextView.setText(post.getEvent_start()); // unformatted
        //eventDescriptionTextView.setText(post.getDescription());

        //addLinks only takes in Spannable object
        Spannable descriptionSpannable = new SpannableString(post.getDescription());
        Linkify.addLinks(descriptionSpannable, Linkify.WEB_URLS);
        eventDescriptionTextView.setText(descriptionSpannable);
        eventDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());

        // set up tag recycler
        tagRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventTagAdapter = new EventTagAdapter(this, categoryListLiveData.getValue());
        tagRecycler.setAdapter(eventTagAdapter);

        // Get image byte array from intent
        byte[] byteArray = intent.getByteArrayExtra("IMAGE");
        if (byteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            eventImageView.setImageBitmap(bitmap);
        }

        // Set listener: update tags UI once data has been fetched
        categoryListLiveData.observe(this, new Observer<ArrayList<Tag>>() {
            @Override
            public void onChanged(ArrayList<Tag> tags) {
                // TODO: update UI - populate tags
                eventTagAdapter.updateTagList(categoryListLiveData.getValue());
            }
        });

        organiserProfilePictureLiveData.observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap != null) {
                    Log.d(TAG, "Image changed");
                    organiserPictureImageView.setImageBitmap(bitmap);
                }
            }
        });

        organiserUsernameLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                organiserNameTextView.setText(s);
            }
        });

        //get all tags associated with the post
        db.getTagsWithPostIDRequest(post.getId(), new RestRepo.RepositoryCallback<ArrayList<Tag>>() {
            @Override
            public void onComplete(ArrayList<Tag> result) {
                if (result != null) {
                    categoryListLiveData.postValue(result);
                }else{
                    Log.d("getTagsWithPostIDRequest", "return null");
                }
            }
        });

        // Get profile picture of organiser (user who posted the event)
        // Get username of organiser of event
        db.getAuthorRequest(organiser, new RestRepo.RepositoryCallback<Author>() {
            @Override
            public void onComplete(Author result) {
                db.getImageRequest(result.getProfile_pic_url(), new RestRepo.RepositoryCallback<Bitmap>() {
                    @Override
                    public void onComplete(Bitmap result) {
                        organiserProfilePictureLiveData.postValue(result);
                    }
                });
                organiserUsernameLiveData.postValue(result.getUsername());
            }
        });

        // 'Add to Calendar' Toggle Button
        // Check whether post is saved to Calendar (for the icon)

        boolean postInCalendar = prefUtils.inCalendar(post.getId());
        addButtonView.setChecked(postInCalendar);
        addButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle button will automatically toggle state first when clicked, before the following checks
                if (addButtonView.isChecked()) {
                    // Add event to calendar
                    dataRepo.addSavedEventOnMainThread(post); // Will trigger listeners to update UI
                    prefUtils.addToCalendar(post.getId());
                    Toast.makeText(EventsPageActivity.this, "Event added to calendar", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Remove event from calendar
                    dataRepo.removeSavedEventOnMainThread(post); // Will trigger listeners to update UI
                    prefUtils.removeFromCalendar(post.getId());
                    Toast.makeText(EventsPageActivity.this, "Event removed from calendar", Toast.LENGTH_SHORT).show();
                }
                prefUtils.commitCalendarUpdates();
            }
        });


        // 'Add to Favourites' Toggle Button (like button)
        // Check whether post is in user's favourites (for the icon)

        boolean postInFavourites = dataRepo.postInFavourites(post);
        likeButtonView.setChecked(postInFavourites);
        likeButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle button will automatically toggle state first when clicked, before the following checks
                if (likeButtonView.isChecked()) {
                    // Add event to user's favourites
                    dataRepo.addFavouriteEventOnMainThread(post); // Will trigger listeners to update UI
                    db.addFavouritesRequest(post.getId(), appUser.getId(), new RestRepo.RepositoryCallback<String>() {
                        @Override
                        public void onComplete(String result) {
                            if (result != null) {
                                Log.d(TAG, "onComplete: event successfully added to user's favourites");
                            }
                            else {
                                Log.d(TAG, "onComplete: error - event failed to be added to user's favourites");
                            }
                        }
                    });
                    Toast.makeText(EventsPageActivity.this, "Event added to user's favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Remove event from user's favourites
                    dataRepo.removeFavouriteEventOnMainThread(post); // Will trigger listeners to update UI
                    db.removeFavouritesRequest(post.getId(), appUser.getId(), new RestRepo.RepositoryCallback<String>() {
                        @Override
                        public void onComplete(String result) {
                            if (result != null) {
                                Log.d(TAG, "onComplete: event successfully removed from user's favourites");
                            }
                            else {
                                Log.d(TAG, "onComplete: error - event failed to be removed from user's favourites");
                            }
                        }
                    });
                    Toast.makeText(EventsPageActivity.this, "Event removed from user's favourites", Toast.LENGTH_SHORT).show();
                }
                prefUtils.commitCalendarUpdates();
            }
        });

    }
}
