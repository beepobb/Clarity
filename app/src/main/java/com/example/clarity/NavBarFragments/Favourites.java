package com.example.clarity.NavBarFragments;

import android.app.Activity;
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
import com.example.clarity.NavBarFragments.Discover.DiscoverEventAdapter;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

import java.util.ArrayList;
import java.util.List;

public class Favourites extends Fragment {
    private RestRepo db;
    private User user;
    private DiscoverEventAdapter favouriteEventAdapter;
    private RecyclerView favouriteRecyclerView;
    private MutableLiveData<List<Post>> favouriteListLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        };
        favouriteListLiveData = new MutableLiveData<>(new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        favouriteRecyclerView = view.findViewById(R.id.favouriteRecyclerView);

        user = new User(2, "ryan", "ryan", "ryan", "ryan", "2024-04-01 03:39:42", "123123132");

        favouriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        favouriteEventAdapter = new DiscoverEventAdapter(getActivity(), new ArrayList<>());
        favouriteRecyclerView.setAdapter(favouriteEventAdapter);
        Log.d("FavouriteEventAdaper", "onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("View", "onViewCreated");

        favouriteListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                // TODO: update RecyclerView with updated data from database
                // executed in main thread, so we can modify Views
                favouriteEventAdapter.updateEventList(favouriteListLiveData.getValue());
            }
        });

        db.getFavouritesRequest(user.getId(), new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {
                if (result != null) {
                    favouriteListLiveData.postValue(result); // use post as it is executed in worker thread
                }
            }
        });
    }
}