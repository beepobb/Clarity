package com.example.clarity.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clarity.MainActivity;
import com.example.clarity.MyApplication;
import com.example.clarity.R;
import com.example.clarity.databinding.ActivityLoginBinding;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private RestRepo database;
    private MutableLiveData<User> userLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = (Button) binding.login;
        final ImageView imageView = binding.imageView;
        final Button createButton = (Button) binding.newAccount;

        final ProgressBar progressBar = binding.progressBar;

        final Handler handler = new Handler();

        database = ((MyApplication) getApplicationContext()).getDatabase();
        userLiveData = new MutableLiveData<>(); // contains null at this step


        userLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                // When user object is fetched (getUserRequest): switch to MainActivity
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Username/password not valid", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((MyApplication) getApplicationContext()).saveAppUser(user); // save logged-in user
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start long operation in a background thread
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
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // Once the operation is completed, show a toast message
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    // Display an error message if any field is empty
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                database.getUserRequest(username, password, new RestRepo.RepositoryCallback<User>() {

                    @Override
                    public void onComplete(User data) {
                        userLiveData.postValue(data); // use postValue as this is executed in worker thread
                    }
                });
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount(){
        Intent go_to_create = new Intent(this, CreateNewAccountView.class);
        startActivity(go_to_create);
    }
}