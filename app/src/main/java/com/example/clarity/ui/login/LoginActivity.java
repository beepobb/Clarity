package com.example.clarity.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = (Button) binding.login;
        final ImageView imageView = binding.imageView;
        final Button createButton = (Button) binding.newAccount;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Log.d("xy is a bitch", "onClick: after");
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent go_to_main = new Intent(this, MainActivity.class);
        startActivity(go_to_main);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void createNewAccount(){
        Intent go_to_create = new Intent(this, CreateNewAccountView.class);
        startActivity(go_to_create);
    }
}