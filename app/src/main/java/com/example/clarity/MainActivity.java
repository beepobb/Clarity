package com.example.clarity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.clarity.NavBarFragments.CalendarFragment;
import com.example.clarity.NavBarFragments.Create;
import com.example.clarity.NavBarFragments.Discover.Discover;
import com.example.clarity.NavBarFragments.Favourites;
import com.example.clarity.NavBarFragments.Profile.Profile;
import com.example.clarity.databinding.ActivityMainBinding;

import com.example.clarity.model.data.Post;
import com.example.clarity.model.repository.RestRepo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public RestRepo database; // for all fragments to access

    // Fragments
    private Fragment discoverFragment;
    private Fragment favouritesFragment;
    private Fragment createFragment;
    private Fragment calendarFragment;
    private Fragment profileFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Debugging
        Toast.makeText(this, "MainActivity onCreate called", Toast.LENGTH_SHORT).show();

        // Database instance
        database = ((MyApplication) getApplicationContext()).getDatabase();

        // Initialize Fragments and Fragment Manager
        discoverFragment = new Discover();
        favouritesFragment = new Favourites();
        createFragment = new Create();
        calendarFragment = new CalendarFragment();
        profileFragment = new Profile();
        fragmentManager = getSupportFragmentManager();

        // Default fragment is Discover:
        showFragment(discoverFragment);

        // set click listeners to nav bar
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.Discover) {
                showFragment(discoverFragment);
            } else if (itemID == R.id.Favourites) {
                showFragment(favouritesFragment);
            } else if (itemID == R.id.Create) {
                showFragment(createFragment);
            } else if (itemID == R.id.Calendar) {
                showFragment(calendarFragment);
            } else if (itemID == R.id.Profile) {
                showFragment(profileFragment);
            }
            return true;
        });
    }

    /**
     * Loads fragment associated with the bottom nav bar
     * @param fragment Discover, Favourites, Create, CalendarFragment, Profile
     */
    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}

