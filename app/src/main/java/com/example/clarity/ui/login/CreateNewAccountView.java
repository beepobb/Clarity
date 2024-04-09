package com.example.clarity.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.clarity.MainActivity;
import com.example.clarity.R;
import com.example.clarity.databinding.CreateNewBinding;

public class CreateNewAccountView extends AppCompatActivity {
    private CreateNewBinding binding;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ImageView selectedImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                // Proceed with account creation (e.g., send data to server or store locally)
                // Once the account is successfully created, you may navigate to another activity
                // or display a success message
                Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                Intent go_to_main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(go_to_main);
                // Example: startActivity(new Intent(CreateNewAccountView.this, MainActivity.class));
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