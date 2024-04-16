package com.example.clarity.Event;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.MyApplication;
import com.example.clarity.MyDataRepository;
import com.example.clarity.NavBarFragments.Discover.EventTags;
import com.example.clarity.PostParcelable;
import com.example.clarity.R;
import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.Author;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;
import com.example.clarity.model.util.CardFormatter;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private MutableLiveData<Bitmap> organiserProfilePictureLiveData;
    private Post thisPost;


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
        organiserProfilePictureLiveData = new MutableLiveData<>();
        organiserUsernameLiveData = new MutableLiveData<>();

        // Get Post object from intent (Event post to display)
        Intent intent = getIntent();
        PostParcelable postParcelable = intent.getParcelableExtra("POST");
        assert postParcelable != null;
        thisPost = postParcelable.getPost();
        int organiser = thisPost.getAuthor_id();

        // Bind Post data to Views
        // addLinks only takes in Spannable object
        Spannable descriptionSpannable = new SpannableString(thisPost.getDescription());
        Linkify.addLinks(descriptionSpannable, Linkify.WEB_URLS);
        eventDescriptionTextView.setText(descriptionSpannable);
        eventDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        eventNameTextView.setText(thisPost.getTitle());
        eventLocationTextView.setText(thisPost.getLocation());
        eventDateTimeTextView.setText(CardFormatter.formatCalendarObject(thisPost.getEventStart(), thisPost.getEventEnd(), true));
        eventDescriptionTextView.setText(thisPost.getDescription());

        // set up tag recycler
        tagRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        eventTagAdapter = new EventTagAdapter(this, new ArrayList<>());
        tagRecycler.setAdapter(eventTagAdapter);

        // Set listener: update post Image once it has been fetched
        dataRepo.getEventImageMappingLiveData().observe(this, new Observer<HashMap<Integer, Bitmap>>() {
            @Override
            public void onChanged(HashMap<Integer, Bitmap> integerBitmapHashMap) {
                Bitmap imageBitmap = integerBitmapHashMap.get(thisPost.getId());
                if (imageBitmap != null) {
                    eventImageView.setImageBitmap(imageBitmap);
                }
            }
        });

        // Set listener: update organizer profile picture once it has been fetched
        organiserProfilePictureLiveData.observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap != null) {
                    Log.d(TAG, "Image changed");
                    organiserPictureImageView.setImageBitmap(bitmap);
                }
            }
        });

        // Set listener: update organizer username once it has been fetched
        organiserUsernameLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                organiserNameTextView.setText(s);
            }
        });

        // Set listener: update post tags once fetched
        dataRepo.getTagsEventMappingLiveData().observe(this, new Observer<HashMap<EventTags, ArrayList<Integer>>>() {
            @Override
            public void onChanged(HashMap<EventTags, ArrayList<Integer>> eventTagsArrayListHashMap) {
                List<EventTags> postTagList = new ArrayList<>();
                for (Map.Entry<EventTags, ArrayList<Integer>> entry: dataRepo.getTagsEventMapping().entrySet()) {
                    EventTags tag = entry.getKey();
                    ArrayList<Integer> postList = entry.getValue();
                    if (postList.contains(thisPost.getId())) {
                        postTagList.add(tag);
                    }
                }
                eventTagAdapter.updateTagList(postTagList);
            }
        });

        // Get profile picture of organiser (user who posted the event) from database
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

        boolean postInCalendar = prefUtils.inCalendar(thisPost.getId());
        addButtonView.setChecked(postInCalendar);
        addButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle button will automatically toggle state first when clicked, before the following checks
                if (addButtonView.isChecked()) {
                    // Add event to calendar
                    dataRepo.addSavedEventOnMainThread(thisPost); // Will trigger listeners to update UI
                    prefUtils.addToCalendar(thisPost.getId());
                    Toast.makeText(EventsPageActivity.this, "Event added to calendar", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Remove event from calendar
                    dataRepo.removeSavedEventOnMainThread(thisPost); // Will trigger listeners to update UI
                    prefUtils.removeFromCalendar(thisPost.getId());
                    Toast.makeText(EventsPageActivity.this, "Event removed from calendar", Toast.LENGTH_SHORT).show();
                }
                prefUtils.commitCalendarUpdates();
            }
        });


        // 'Add to Favourites' Toggle Button (like button)
        // Check whether post is in user's favourites (for the icon)

        boolean postInFavourites = dataRepo.postInFavourites(thisPost);
        likeButtonView.setChecked(postInFavourites);
        likeButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle button will automatically toggle state first when clicked, before the following checks
                if (likeButtonView.isChecked()) {
                    // Add event to user's favourites
                    dataRepo.addFavouriteEventOnMainThread(thisPost); // Will trigger listeners to update UI
                    db.addFavouritesRequest(thisPost.getId(), appUser.getId(), new RestRepo.RepositoryCallback<String>() {
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
                    Toast.makeText(EventsPageActivity.this, "Event added to favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Remove event from user's favourites
                    dataRepo.removeFavouriteEventOnMainThread(thisPost); // Will trigger listeners to update UI
                    db.removeFavouritesRequest(thisPost.getId(), appUser.getId(), new RestRepo.RepositoryCallback<String>() {
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
                    Toast.makeText(EventsPageActivity.this, "Event removed from favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Clicking event or organizer picture will display a bigger picture
        eventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog(eventImageView.getDrawable());
            }
        });

        organiserPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog(organiserPictureImageView.getDrawable());
            }
        });

    }

    private void showImageDialog(Drawable drawable) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog, null);
        ImageView dialogImageView = dialogView.findViewById(R.id.imageDialogView);
        dialogImageView.setImageDrawable(drawable);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.show();
    }


}
