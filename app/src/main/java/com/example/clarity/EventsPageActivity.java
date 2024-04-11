package com.example.clarity;

import android.content.Intent;
import android.os.Bundle;
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
    private PreferenceUtils prefUtils;
    private RestRepo db;
    private User appUser;

    // TODO: Define Views
    private ImageView eventImageView;
    private TextView eventNameTextView;
    private TextView eventLocationTextView;
    private ToggleButton addButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_page);

        // TODO: fetch Views
        addButtonView = findViewById(R.id.add_button);
        eventNameTextView = findViewById(R.id.eventNameView);
        eventLocationTextView = findViewById(R.id.location);

        // Initialize other attributes
        db = ((MyApplication) getApplicationContext()).getDatabase();
        prefUtils = PreferenceUtils.getInstance(this);
        appUser = ((MyApplication) getApplicationContext()).getAppUser();

        // Get Post object from intent
        Intent intent = getIntent();
        PostParcelable postParcelable = intent.getParcelableExtra("POST");
        assert postParcelable != null;
        Post post = postParcelable.getPost();

        // TODO: Bind Post data to Views
        eventNameTextView.setText(post.getTitle());
        eventLocationTextView.setText(post.getLocation());


        // 'Add to Calendar' Toggle Button
        // Check whether post is saved to Calendar (for the icon)

        boolean postInCalendar = prefUtils.inCalendar(post.getId());
        addButtonView.setChecked(postInCalendar);
        addButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle button will automatically toggle state first
                if (addButtonView.isChecked()) {
                    // Add event to calendar
                    prefUtils.addToCalendar(post.getId());
                    Toast.makeText(EventsPageActivity.this, "Event added to calendar", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Remove event from calendar
                    prefUtils.removeFromCalendar(post.getId());
                    Toast.makeText(EventsPageActivity.this, "Event removed from calendar", Toast.LENGTH_SHORT).show();
                }
                prefUtils.commitCalendarUpdates();
            }
        });

    }
}
