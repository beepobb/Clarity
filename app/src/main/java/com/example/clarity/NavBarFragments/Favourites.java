package com.example.clarity.NavBarFragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clarity.MainActivity;
import com.example.clarity.MyApplication;
import com.example.clarity.MyDataRepository;
import com.example.clarity.NavBarFragments.Discover.DiscoverEventAdapter;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Favourites extends Fragment {
    private String TAG = "FavouritesFragment";
    private RestRepo db;
    private User appUser;
    private MyDataRepository dataRepo;
    private DiscoverEventAdapter favouriteEventAdapter;
    private RecyclerView favouriteRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        };
        dataRepo = MyDataRepository.getInstance();
        appUser = ((MyApplication) getActivity().getApplicationContext()).getAppUser(); // Logged in User
        Log.i(TAG, "onCreate: finished");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        favouriteRecyclerView = view.findViewById(R.id.favouriteRecyclerView);

        favouriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        favouriteEventAdapter = new DiscoverEventAdapter(getActivity(), new ArrayList<>());
        favouriteRecyclerView.setAdapter(favouriteEventAdapter);
        Log.i(TAG, "onCreateView: finished");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("View", "onViewCreated");

        // Set up observer for favourite events (favouriteEventsLiveData from dataRepo)
        // When there is a change in the favourite events list (e.g. Posts removed, etc), update the UI
        dataRepo.getFavouriteEventsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                // Updates RecyclerView with updated data from database
                // executed in main thread, so that we can modify Views

                Log.d(TAG, "favouriteEventsLiveData triggered: refresh UI");
                favouriteEventAdapter.updateEventList(dataRepo.getFavouriteEvents());
            }
        });

        dataRepo.getEventImageMappingLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Bitmap>>() {
            @Override
            public void onChanged(HashMap<Integer, Bitmap> integerBitmapHashMap) {
                favouriteEventAdapter.updateEventImageMapping(dataRepo.getEventImageMapping());
            }
        });

        // Load in user's favourite posts from database
        db.getFavouritesRequest(appUser.getId(), new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {
                if (result == null) { // no Posts fetched
                    result = new ArrayList<>(); // set result to be an empty list rather than null
                }

                dataRepo.loadFavouriteEventsOnWorkerThread(result);
                Log.d(TAG, "List of user's favourite events pulled from database");

                // Note: we cannot directly update UI Views here as onComplete will be executed on a worker thread
                // We instead trigger observers that will update the UI in the main thread.
            }
        });


    }
}