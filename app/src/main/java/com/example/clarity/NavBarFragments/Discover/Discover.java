package com.example.clarity.NavBarFragments.Discover;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.clarity.model.data.Tag;
import com.example.clarity.model.repository.RestRepo;

import java.util.*;

public class Discover extends Fragment implements TagButtonUpdateEventsClickListener {
    // stores info for buttons and events UI
    private List<EventTags> tagButtons;
    private List<Post> eventList;
    private MutableLiveData<List<Post>> eventListLive;

    // UI elements
    private RecyclerView tagRecycler;
    private RecyclerView eventRecycler;
    private TagButtonAdapter tagButtonAdapter;
    private DiscoverEventAdapter discoverEventAdapter;

    private RestRepo db; // reference for db
    private HashMap<EventTags, ArrayList<Integer>> tagsEventMapping; // tags and event link

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get reference to db
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        }

        // initialise values
        tagButtons = Arrays.asList(EventTags.values());
        eventList = new ArrayList<>();
        eventListLive = new MutableLiveData<>(new ArrayList<>());
        tagsEventMapping = new HashMap<>();
        for (EventTags e : EventTags.values()) {
            tagsEventMapping.put(e, new ArrayList<>());
        }
    }

    // create UI here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        // get reference to UI elements
        tagRecycler = view.findViewById(R.id.tag_recycler);
        eventRecycler = view.findViewById(R.id.event_recycler);

        // set up tag button recycler
        tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagButtonAdapter = new TagButtonAdapter(getActivity(), tagButtons, this);
        tagRecycler.setAdapter(tagButtonAdapter);

        // set up event recycler
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        discoverEventAdapter = new DiscoverEventAdapter(getActivity(), eventList);
        eventRecycler.setAdapter(discoverEventAdapter);

        // set up observer for eventListLive, will update UI when data comes in
        eventListLive.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                Log.d("DiscoverFragment", "observer called");
                updateEventRecycler();
            }
        });

        return view;
    }

    // put db calls here to avoid calling db everytime screen is changed to fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i("DiscoverFragment", "onViewCreate");

        // get all Posts from db
        db.getAllPostRequest(new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {
                Log.d("DiscoverFragment", "db onComplete "+result.toString());

                // update eventListLive, observer (UI) will be notified
                eventListLive.postValue(result);
            }
        });

        // get tags and events
        db.getAllPostsWithTagRequest(new RestRepo.RepositoryCallback<ArrayList<Tag>>() {
            @Override
            public void onComplete(ArrayList<Tag> result) {
                Log.d("DiscoverFragment", "db getAllPostsWithTagRequest onComplete "+ result.toString());
                for (Tag tag : result) {
                    Integer post_id = tag.getPost_id();
                    String tag_category = tag.getTag_category();
                    // TODO: change this after the DB has new tag category values
                    if (tag_category.equals("fifthrow") || tag_category.equals("FIFTH_ROW")) {
                        tagsEventMapping.get(EventTags.FIFTH_ROW).add(post_id);
                    } else if (tag_category.equals("CAREER")) {
                        tagsEventMapping.get(EventTags.CAREER).add(post_id);
                    }
                }
            }
        });
    }

    // Helper function
    public void updateEventRecycler() {
        discoverEventAdapter.updateEventList(eventListLive.getValue());
    }

    /**
     * tagRecycler buttons call this function to change the eventRecycler
     * @param position of the tags buttons in the recycler
     */
    @Override
    public void onButtonClick(int position) {
        Toast.makeText(getActivity(), "TAG BUTTON CLICKED "+tagButtons.get(position).toString(), Toast.LENGTH_SHORT).show();
        Log.d("DiscoverFragment", tagButtons.get(position).toString());

        EventTags buttonPressed = tagButtons.get(position);

        // generate a sublist of events based on the tag button that was clicked
        List<Post> subList = new ArrayList<>();
        for (Post post : Objects.requireNonNull(eventListLive.getValue())) {
            for (Integer id : Objects.requireNonNull(tagsEventMapping.get(buttonPressed))) {
                if (post.getId() == id) {
                    subList.add(post);
                }
            }
        }

        // refresh adapter for event recycler
        discoverEventAdapter = new DiscoverEventAdapter(getActivity(), subList);
        eventRecycler.setAdapter(discoverEventAdapter);
    }
}



