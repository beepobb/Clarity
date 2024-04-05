package com.example.clarity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.clarity.NavBarFragments.CalendarFragment;
import com.example.clarity.NavBarFragments.Create;
import com.example.clarity.NavBarFragments.Discover;
import com.example.clarity.NavBarFragments.Favourites;
import com.example.clarity.NavBarFragments.Profile.Profile;
import com.example.clarity.databinding.ActivityMainBinding;

import com.example.clarity.model.data.Tag;
import com.example.clarity.model.data.User;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.repository.RestRepo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    RestRepo data = new RestRepo(executorService);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new CalendarFragment());
        replaceFragment(new Discover());

        // set click listeners to nav bar
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.Discover) {
                replaceFragment(new Discover());
            } else if (itemID == R.id.Favourites) {
                replaceFragment(new Favourites());
            } else if (itemID == R.id.Create) {
                replaceFragment(new Create());
            } else if (itemID == R.id.Calendar) {
                replaceFragment(new CalendarFragment());
            } else if (itemID == R.id.Profile) {
                replaceFragment(new Profile());
            }
            return true;
        });
    }

    /**
     * Loads fragment associated with the bottom nav bar
     * @param fragment Discover, Favourites, Create, CalendarFragment, Profile
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

