package com.example.clarity.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.clarity.MainActivity;
import com.example.clarity.MyApplication;
import com.example.clarity.databinding.CreateNewBinding;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

import java.io.FileNotFoundException;

public class CreateNewAccountView extends AppCompatActivity {
    private CreateNewBinding binding;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ImageView selectedImageView;
    private RestRepo database;
    private MutableLiveData<String> stringMutableLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = ((MyApplication) getApplicationContext()).getDatabase();
        stringMutableLiveData = new MutableLiveData<>(); // contains null at this step
        stringMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String string) {
                // When string is fetched (addUserRequest): switch to MainActivity
                Toast.makeText(getApplicationContext(), "Account created successfully, please log in again", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateNewAccountView.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        final ImageView NewImageView = binding.addImage;
        final TextView CreateAccountTextView = binding.textView4;
        final TextView choiceTextView = binding.textView5;
        final Spinner choicesSpinner = binding.spinner;
        final TextView usernameTextView = binding.textView1;
        final EditText usernameEditText = binding.username;
        final TextView emailTextView = binding.textView2;
        final EditText emailEditText = binding.email;
        final TextView passwordTextView = binding.password;
        final EditText passwordEditText = binding.enterPassword;
        final TextView confirmTextView = binding.textView3;
        final EditText confirmEditText = binding.confirmPassword;
        final Button loginButton = binding.login;

        selectedImageView = NewImageView;
        imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            // Load the selected image into the ImageView
                            selectedImageView.setImageURI(selectedImageUri);
                            try {
                                //TODO: check getApplicationContext()
                                Bitmap bitmap = BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(selectedImageUri));
                                selectedImageView.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

// Launch the gallery picker when the ImageView is clicked
        selectedImageView.setOnClickListener(view -> selectImage());
        // Add functionality to handle account creation
        // For example, set up a button click listener to create an account
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String role = choicesSpinner.getSelectedItem().toString();
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmEditText.getText().toString();
                // Validate input fields (e.g., check if username, email, and password are not empty)
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    // Display an error message if any field is empty
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Additional validation logic can be added here (e.g., check if passwords match)
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.contains("@")) {
                    Toast.makeText(getApplicationContext(), "Must be a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(password.length() >= 8)) {
                    Toast.makeText(getApplicationContext(), "Must be more than 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                database.addUserRequest(username, password, email, role, new RestRepo.RepositoryCallback<String>() {
                    @Override
                    public void onComplete(String result) {
                        stringMutableLiveData.postValue(result);
                    }
                });
            }
        });
    }
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            imageActivityResultLauncher.launch(intent);
        }
    }
}