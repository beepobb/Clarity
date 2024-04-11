package com.example.clarity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;
import com.example.clarity.ui.login.LoginActivity;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

// No views associated with this class - only start-up navigation logic
public class NavigatorActivity extends AppCompatActivity {
    // Need to test whether it is better to have start-up logic be in this no-view activity
    // or in MyApplication class

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
        if (sessionToken.equals("")) { // No session token
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
             * we will store the user id as a session token in local storage (sharedPrefs).
             * Whenever the app is opened, if there is a "session token" (user id) in sharedPrefs,
             * then we simply log in with that user (and skip the log-in page).
             * This "session token" can be cleared by logging out.
             */
            // TODO: serialize whole User object as session token instead, then verify that id exists in database

            // Deserialize session token back into a User object
            // Deserialize string back to object
//            byte[] decoded = Base64.getDecoder(sessionToken);
//            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(decoded));
//            User deserializedObj = (User) ois.readObject();
//            ois.close();


            // else: go to log-in page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
    }
}
