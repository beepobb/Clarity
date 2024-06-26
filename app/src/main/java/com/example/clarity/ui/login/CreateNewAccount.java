package com.example.clarity.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.clarity.MyApplication;
import com.example.clarity.R;
import com.example.clarity.databinding.CreateNewBinding;
import com.example.clarity.model.repository.RestRepo;

public class CreateNewAccount extends AppCompatActivity {

    private CreateNewBinding binding;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ImageView selectedImageView;
    private RestRepo database;
    private MutableLiveData<String> stringMutableLiveData;
    private Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final ImageView NewImageView = binding.addImage;
        final Spinner choicesSpinner = binding.spinner;
        final EditText usernameEditText = binding.username;
        final EditText emailEditText = binding.email;
        final EditText passwordEditText = binding.enterPassword;
        final EditText confirmEditText = binding.confirmPassword;
        final Button loginButton = binding.login;
        final ProgressBar progressBar = binding.progressBar;
        final Handler handler = new Handler();

        database = ((MyApplication) getApplicationContext()).getDatabase();
        stringMutableLiveData = new MutableLiveData<>(); // contains null at this step

        //allows user to pick a custom image for their profile picture
        selectedImageView = NewImageView;
        imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            try {
                                Glide.with(this)
                                        .asBitmap()
                                        .load(selectedImageUri)
                                        .into(new BitmapImageViewTarget(selectedImageView) {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                                super.onResourceReady(bitmap, transition);
                                                // Assign the loaded Bitmap to the image variable
                                                image = bitmap;
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // Launch the gallery picker when the ImageView is clicked
        selectedImageView.setOnClickListener(view -> selectImage());

        //triggers when user clicks sign up button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setEnabled(false);
                String role = choicesSpinner.getSelectedItem().toString();
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmEditText.getText().toString();
                if (image == null) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_profile);
                }
                // Validate input fields (check if username, email, and password are not empty)
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    // Display an error message if any field is empty
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                    return;
                }

                // check if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                    return;
                }

                //check if email is "valid"
                if (!email.contains("@")) {
                    Toast.makeText(getApplicationContext(), "Must be a valid email", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                    return;
                }
                //check if password length is more than 7 characters
                if (!(password.length() >= 8)) {
                    Toast.makeText(getApplicationContext(), "Must be more than 8 characters", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                    return;
                }
                //background thread will play a circular loading bar
                new Thread(new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Please wait for your account to be created...", Toast.LENGTH_LONG).show();
                            }
                        });
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

                //calling RestRepo method for instance in order to add new user to database
                database.addUserRequest(username, password, email, role, image, new RestRepo.RepositoryCallback<String>() {
                    @Override
                    public void onComplete(String result) {
                        stringMutableLiveData.postValue(result);
                    }
                });

            }
        });
        // sets up an observer on a MutableLiveData<String> object, listen for changes to stringMutableLiveData object
        stringMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String string) {
                loginButton.setEnabled(true);
                if (string == null) {
                    Toast.makeText(getApplicationContext(), "Username/email taken, please choose another username/email", Toast.LENGTH_SHORT).show();
                }
                else {
                    // When string is fetched (addUserRequest): switch to MainActivity
                    goToLoginActivity();
                    finish();
                }
            }
        });
    }
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            imageActivityResultLauncher.launch(intent);
        }
    }

    private void goToLoginActivity() {
        Toast.makeText(getApplicationContext(), "Account created successfully, please log in again", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CreateNewAccount.this, LoginActivity.class);
        startActivity(intent);
    }
}