package com.example.clarity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;
import com.example.clarity.ui.login.LoginActivity;

import java.io.IOException;

// No views associated with this class - only start-up navigation logic
public class NavigatorActivity extends AppCompatActivity {
    // Need to test whether it is better to have start-up logic be in this no-view activity
    // or in MyApplication class
    private static final String TAG = "NavigatorActivity";
    // TODO: set starting activity of app to be NavigatorActivity
    private PreferenceUtils prefUtils;
    private RestRepo database;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefUtils = PreferenceUtils.getInstance(this);
        myApplication = ((MyApplication) getApplicationContext());
        database = myApplication.getDatabase();

        String sessionToken = prefUtils.getSessionToken();
        if (sessionToken.equals("")) {
            Log.d(TAG, "No session token found");
            // Go to log-in page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            // Authenticate session token
            // if valid, user object is saved, go straight to main activity

            /*
             * IMPORTANT: due to time constraints, we are unable to implement a proper session token
             * system on the database end. Instead, when a user logs into the app for the first time,
             * we will serialize the User object as a session token in local storage (sharedPrefs).
             * Whenever the app is opened, if there is a "session token" in sharedPrefs,
             * then we simply log in with that user (and skip the log-in page).
             * This "session token" can be cleared by logging out.
             */

            try {
                User appUser = (User) (SerializationUtils.deserializeFromString(sessionToken));
                // TODO: ensure that the User object exists in database with database method
                Log.d(TAG, "Fetched User object from session token");
                myApplication.saveAppUser(appUser);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } catch (IOException | ClassNotFoundException e) {
                Log.d(TAG, "Failed to get User object from session token");
                // throw new RuntimeException(e);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        }
        finish();
    }
}
