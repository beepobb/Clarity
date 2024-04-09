package com.example.clarity.NavBarFragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clarity.MainActivity;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

import java.util.ArrayList;

public class Favourites extends Fragment {
    private RestRepo db;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        }
        user = new User(3, "ifalltower", "jeui3ug4i836", "SUTD Student", "test@gmail.com", "2024-04-01 06:35:23");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db.getFavouritesRequest(user.getId(), new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {

            }
        });
    }
}