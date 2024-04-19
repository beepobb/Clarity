package com.example.clarity.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.clarity.MainActivity;
import com.example.clarity.MyApplication;
import com.example.clarity.SerializationUtils;
import com.example.clarity.databinding.ActivityLoginBinding;
import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private RestRepo database;
    private MutableLiveData<User> userLiveData;
    private PreferenceUtils prefUtils;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = (Button) binding.login;
        final Button createButton = binding.newAccount;

        final ProgressBar progressBar = binding.progressBar;

        final Handler handler = new Handler();

        database = ((MyApplication) getApplicationContext()).getDatabase();
        prefUtils = PreferenceUtils.getInstance(this);
        userLiveData = new MutableLiveData<>(); // contains null at this step

        //triggers when user clicks login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);

                // Start long operation in a background thread
                hideKeyboard(getApplicationContext(), v);

                username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    // Display an error message if any field is empty
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                    return;
                }
                //new thread will play a circular loading bar
                new Thread(new Runnable() {
                    public void run() {
                        int progressStatus = 0;
                        while (progressStatus < 100) {
                            progressStatus += 1;

                            // Update the progress bar and display the current value
                            int finalProgressStatus = progressStatus;
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(finalProgressStatus);
                                }
                            });

                            try {
                                // Sleep for 200 milliseconds to simulate a long operation
                                Thread.sleep(25);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            // Sleep for 200 milliseconds after reaching 100%
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(0);
                            }
                        });
                    }
                }).start();

                //calling RestRepo method for instance in order to get user from database
                database.getUserRequest(username, password, new RestRepo.RepositoryCallback<User>() {

                    @Override
                    public void onComplete(User data) {
                        userLiveData.postValue(data); // use postValue as this is executed in worker thread
                    }
                });
            }
        });

        //triggers when database validates username and password
        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                loginButton.setEnabled(true);
                // When user object is fetched (getUserRequest): switch to MainActivity
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Username/password not valid", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Welcome, " + username + "!", Toast.LENGTH_LONG).show();
                    ((MyApplication) getApplicationContext()).saveAppUser(user); // save logged-in user
                    // serializes a User object into a string representation and saves it using prefUtils
                    try {
                        String sessionToken = SerializationUtils.serializeToString(user);
                        prefUtils.saveSessionToken(sessionToken);
                    } catch (IOException e) {
                        Log.d(TAG, "Failed to serialize User object.");
                        throw new RuntimeException(e);
                    }

                    goToMainActivity();
                    finish();
                }
            }
        });

        //triggers when user clicks create new account button
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    //method to go to CreateNewAccountView
    private void createNewAccount(){
        Intent intent = new Intent(this, CreateNewAccount.class);
        startActivity(intent);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //hides keyboard after user inputs password
    private void hideKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}