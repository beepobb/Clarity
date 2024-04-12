package com.example.clarity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;


public class EventsPageActivity extends AppCompatActivity {
    private String TAG = "EventsPageActivity";
    private PreferenceUtils prefUtils;
    private MyDataRepository dataRepo;
    private RestRepo db;
    private User appUser;

    // TODO: Define Views
    private ImageView eventImageView;
    private TextView eventNameTextView;
    private TextView eventLocationTextView;
    private TextView eventDateTimeTextView;
    private TextView eventDescriptionTextView;
    private ToggleButton addButtonView;
    private ToggleButton likeButtonView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_page);

        // TODO: fetch Views
        addButtonView = findViewById(R.id.addToggleButton);
        likeButtonView = findViewById(R.id.likeToggleButton);
        eventImageView = findViewById(R.id.eventImageView);
        eventNameTextView = findViewById(R.id.eventNameTextView);
        eventLocationTextView = findViewById(R.id.eventLocationTextView);
        eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView);
        eventDateTimeTextView = findViewById(R.id.eventDateTimeTextView);

        // Initialize other attributes
        db = ((MyApplication) getApplicationContext()).getDatabase();
        prefUtils = PreferenceUtils.getInstance(this);
        appUser = ((MyApplication) getApplicationContext()).getAppUser();
        dataRepo = MyDataRepository.getInstance();

        // Get Post object from intent (Event post to display)
        Intent intent = getIntent();
        PostParcelable postParcelable = intent.getParcelableExtra("POST");
        assert postParcelable != null;
        Post post = postParcelable.getPost();

        // TODO: Bind Post data to Views
        eventNameTextView.setText(post.getTitle());
        eventLocationTextView.setText(post.getLocation());
        eventDateTimeTextView.setText(post.getEvent_start()); // unformatted
        eventDescriptionTextView.setText(post.getDescription());


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
                    // TODO: CALL DATABASE UNFAVOURITE FUNCTION ONCE JUNJIE HAS IMPLEMENTED IT
                    Toast.makeText(EventsPageActivity.this, "Event removed from user's favourites", Toast.LENGTH_SHORT).show();
                }
                prefUtils.commitCalendarUpdates();
            }
        });

    }
}
