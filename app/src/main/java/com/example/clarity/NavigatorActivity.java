package com.example.clarity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.repository.RestRepo;
import com.example.clarity.ui.login.LoginActivity;

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

        if (prefUtils.getSessionToken().equals("")) { // No session token
            // Go to log-in page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            // Authenticate session token

            // if valid, user object is saved, go to main activity

            // else: go to log-in page
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
    }
}
