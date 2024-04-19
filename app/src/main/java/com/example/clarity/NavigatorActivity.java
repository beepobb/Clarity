package com.example.clarity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;
import com.example.clarity.ui.login.LoginActivity;

import java.io.IOException;

// No views associated with this class - only start-up navigation logic
// This is the first Activity of the application
public class NavigatorActivity extends AppCompatActivity {

    private static final String TAG = "NavigatorActivity";
    private PreferenceUtils prefUtils;
    private RestRepo database;
    private MyApplication myApplication;
    private MutableLiveData<Boolean> hasSessionTokenLive;
    private User appUser;
    private TextView loadingMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigator_activity);
        prefUtils = PreferenceUtils.getInstance(this);
        myApplication = ((MyApplication) getApplicationContext());
        database = myApplication.getDatabase();
        hasSessionTokenLive = new MutableLiveData<>();
        // Lesson learnt: if you pass in a value into MutableLiveData during initialization
        // the observer callback will get called the moment its attached, even without setValue or postValue

        loadingMessageTextView = findViewById(R.id.loadingMessageTextView);

        // If there is a session token, login as user:
        hasSessionTokenLive.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hasSessionToken) {
                if (hasSessionToken) { // deserialized user object is valid (in database)
                    Log.i(TAG, "User found in database, logging in");
                    assert appUser != null;
                    myApplication.saveAppUser(appUser);
                    goToMainActivity();
                }
                else {
                    Log.i(TAG, "onChanged: User not in database, log in again");
                    myApplication.saveAppUser(null);
                    prefUtils.clearSessionToken();
                    Toast.makeText(myApplication, "User no longer exists.", Toast.LENGTH_SHORT).show();
                    goToLoginActivity();
                }
            }
        });


        String sessionToken = prefUtils.getSessionToken();
        if (sessionToken.equals("")) {
            Log.d(TAG, "No session token found");
            goToLoginActivity();
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
             * We then verify that the User object is in the database (and not deleted).
             * This "session token" is cleared when logging out.
             */

            try {
                loadingMessageTextView.setText("Verifying session token...");
                appUser = (User) (SerializationUtils.deserializeFromString(sessionToken));
                Log.i(TAG, "Fetched User object from session token. ID: " + appUser.getId());
                database.checkUserRequest(appUser.getId(), new RestRepo.RepositoryCallback<Boolean>() {
                    @Override
                    public void onComplete(Boolean result) {
                        hasSessionTokenLive.postValue(result);
                        // Triggers observer defined above, branching logic for which page to navigate to
                    }
                });

            } catch (IOException | ClassNotFoundException e) {
                Log.i(TAG, "Failed to get User object from session token");
                // throw new RuntimeException(e);
                goToLoginActivity();
            }

        }

    }

    /**
     * Redirect to MainActivity (user object saved)
     */
    private void goToMainActivity() {
        Toast.makeText(myApplication, "Logged in: Welcome "+appUser.getUsername()+"!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Redirect to LoginActivity
     */
    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
