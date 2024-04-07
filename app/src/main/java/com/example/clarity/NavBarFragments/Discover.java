package com.example.clarity.NavBarFragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.MainActivity;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.repository.RestRepo;

import java.util.*;

public class Discover extends Fragment {
    // for tag button creation
    public enum EventTags {
        CAREER, CAMPUS_LIFE, FIFTH_ROW, COMPETITION, WORKSHOP
    }

    private EventTags currentTagFilter; // used to toggle the events listed

    // stores info for buttons and events UI
    List<EventTags> tagButtons;
    MutableLiveData<List<Post>> eventListLive;
    MutableLiveData<List<Integer>> tagButtonsLive;
    List<Post> eventList;

    // UI elements
    private RecyclerView tagRecycler;
    private RecyclerView eventRecycler;
    private TagButtonAdapter tagButtonAdapter;
    private DiscoverEventAdapter discoverEventAdapter;

    // reference for db
    private RestRepo db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        // get reference to db
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        }

        // get reference to UI elements
        tagRecycler = view.findViewById(R.id.tag_recycler);
        eventRecycler = view.findViewById(R.id.event_recycler);

        // initialise values
        tagButtons = Arrays.asList(EventTags.values());
        eventList = new ArrayList<>();
        eventListLive = new MutableLiveData<>(new ArrayList<>());
        tagButtonsLive = new MutableLiveData<>(new ArrayList<>());
        Post placeholderPost = new Post(0,
                0,
                "event start",
                "event end",
                "hello",
                "Post title",
                "School",
                "Rubbish",
                "created time");
        for (int i = 0; i < 4; i++) {
            eventList.add(placeholderPost);
        }

        // set up tag button recycler
        tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagButtonAdapter = new TagButtonAdapter(getActivity(), tagButtons);
        tagRecycler.setAdapter(tagButtonAdapter);

        // set up event recycler
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        discoverEventAdapter = new DiscoverEventAdapter(getActivity(), eventList);
        eventRecycler.setAdapter(discoverEventAdapter);
        Log.d("EESONG", eventList.toString());

        // get all Posts
        db.getAllPostRequest(new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {
                Log.d("DiscoverFragment", "db onComplete "+result.toString());

                // update eventListLive, observer (UI) will be notified
                eventListLive.postValue(result);
            }
        });

        // set up observer for eventListLive, will update UI when data comes in
        eventListLive.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                Log.d("DiscoverFragment", "observer called");
                updateEventRecycler();
            }
        });

        // TODO: get all Tags


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i("DiscoverFragment", "onViewCreate");

        // bind button click listeners

    }

    // Helper function
    public void updateEventRecycler() {
        discoverEventAdapter.updateEventList(eventListLive.getValue());
    }

    public void updateTagRecycler() {

    }
}



