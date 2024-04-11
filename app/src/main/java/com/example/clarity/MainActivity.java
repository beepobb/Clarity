package com.example.clarity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.clarity.NavBarFragments.Calendar.CalendarFragment;
import com.example.clarity.NavBarFragments.Create;
import com.example.clarity.NavBarFragments.Discover.Discover;
import com.example.clarity.NavBarFragments.Favourites;
import com.example.clarity.NavBarFragments.Profile;

import com.example.clarity.databinding.ActivityMainBinding;
import com.example.clarity.model.repository.RestRepo;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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

        // Database instance
        database = ((MyApplication) getApplicationContext()).getDatabase();

<<<<<<< HEAD


        // Initialize Fragments
=======
        // Initialize Fragments and Fragment Manager
>>>>>>> e2c55ac0ceef94c038791ccccc5c06942d31d7b9
        discoverFragment = new Discover();
        favouritesFragment = new Favourites();
        createFragment = new Create();
        calendarFragment = new CalendarFragment();
        profileFragment = new Profile();
        fragmentManager = getSupportFragmentManager();

        // Before setting button listeners, 'start' all fragment (lifecycle methods)
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, favouritesFragment);
        fragmentTransaction.hide(favouritesFragment);
        fragmentTransaction.add(R.id.frame_layout, createFragment);
        fragmentTransaction.hide(createFragment);
        fragmentTransaction.add(R.id.frame_layout, calendarFragment);
        fragmentTransaction.hide(calendarFragment);
        fragmentTransaction.add(R.id.frame_layout, profileFragment);
        fragmentTransaction.hide(profileFragment);
        fragmentTransaction.add(R.id.frame_layout, discoverFragment); // Discover is first
        fragmentTransaction.commit();

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
        // Hide all existing fragments
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            fragmentTransaction.hide(f);
        }

        // Show or add the new fragment
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.frame_layout, fragment);
            Log.d(TAG, "Theoretically, you should not see this message.");
        }

        // We are not using fragmentTransaction.replace in order to maintain fragment state
        fragmentTransaction.commit();
    }
}

