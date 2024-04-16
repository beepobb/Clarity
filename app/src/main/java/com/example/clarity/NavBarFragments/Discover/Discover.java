package com.example.clarity.NavBarFragments.Discover;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.clarity.MainActivity;
import com.example.clarity.MyDataRepository;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.Tag;
import com.example.clarity.model.repository.RestRepo;

import java.util.*;

public class Discover extends Fragment {
    // stores info for buttons and events UI
    private List<EventTags> tagButtons;

    // UI elements
    private RecyclerView tagRecycler;
    private RecyclerView eventRecycler;
    private TagButtonAdapter tagButtonAdapter;
    private DiscoverEventAdapter discoverEventAdapter;
    private SwipeRefreshLayout swipeDownToRefresh;

    // Other attributes
    private RestRepo db; // reference for db
    private MyDataRepository dataRepo; // central location for information
    private final String TAG = "DiscoverFragment";

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
        dataRepo = MyDataRepository.getInstance();
    }

    // create UI here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        // get reference to UI elements
        tagRecycler = view.findViewById(R.id.tag_recycler);
        eventRecycler = view.findViewById(R.id.event_recycler);
        swipeDownToRefresh = view.findViewById(R.id.swipeDownToRefresh);

        // set up tag button recycler
        tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagButtonAdapter = new TagButtonAdapter(getActivity(), tagButtons);
        tagRecycler.setAdapter(tagButtonAdapter);

        // set up event recycler
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        discoverEventAdapter = new DiscoverEventAdapter(getActivity(), new ArrayList<>()); // pass in empty arraylist
        eventRecycler.setAdapter(discoverEventAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i("DiscoverFragment", "onViewCreate");

        // Set up all listeners //
        // set up observer for allEventsLiveData: when posts are loaded in, fetch the images
        dataRepo.getAllEventsHashMapLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Post>>() {
            @Override
            public void onChanged(HashMap<Integer, Post> integerPostHashMap) {
                Log.d(TAG, "eventListLive observer called");

                HashMap<Integer, Bitmap> tmpMap = new HashMap<>();
                // get images for every Post object
                // TODO: single DB request that fetches entire
                for (Post post: dataRepo.getAllEvents()) {
                    String url = post.getImage_url();
                    Integer post_id = post.getId();
                    db.getImageRequest(url, new RestRepo.RepositoryCallback<Bitmap>() {
                        @Override
                        public void onComplete(Bitmap result) {
                            Log.d(TAG, "onComplete: ");
                            tmpMap.put(post_id, result);
                            dataRepo.loadEventImageMappingOnWorkerThread(tmpMap); // triggers UI refresh
                        }
                    });
                }
                updateRecyclerEventsShown();
            }

        });

        // set up observer for eventImageMappingLiveData, refresh UI when images are loaded in
        dataRepo.getEventImageMappingLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Bitmap>>() {
            @Override
            public void onChanged(HashMap<Integer, Bitmap> integerBitmapHashMap) {
                Log.d(TAG, "eventImageMappingLive observer called");
                updateRecyclerImageMapping();
            }
        });

        // set up observer for tagsImageMappingLiveData, refresh UI when tag mappings are loading in
        dataRepo.getTagsEventMappingLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<EventTags, ArrayList<Integer>>>() {
            @Override
            public void onChanged(HashMap<EventTags, ArrayList<Integer>> eventTagsArrayListHashMap) {
                Log.d(TAG, "tagsEventMappingLiveData observer called");
                updateRecyclerEventsShown();
            }
        });

        // set up observer for tagButtonAdapter's selectedTagLiveData
        tagButtonAdapter.getSelectedTagLiveData().observe(getViewLifecycleOwner(), new Observer<EventTags>() {
            @Override
            public void onChanged(EventTags eventTags) {
                Log.d(TAG, "selectedTagLiveData observer called");
                updateRecyclerEventsShown();
            }
        });

        // Fetch data from database //
        // get all Posts
        swipeDownToRefresh.setRefreshing(true);
        loadDatabase();

        // TODO: swipe to refresh might be broken
        swipeDownToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: Fetch events from database again and update Discover fragment");
                loadDatabase();
            }
        });
    }

    public void loadDatabase() {
        db.getAllPostRequest(new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {
                Log.d(TAG, "db onComplete " + result.toString());

                // update allEventsLive, observer (UI) will be notified
                dataRepo.loadAllEventsOnWorkerThread(result);
            }
        });

        // get tags and events
        db.getAllPostsWithTagRequest(new RestRepo.RepositoryCallback<ArrayList<Tag>>() {
            @Override
            public void onComplete(ArrayList<Tag> result) {
                Log.d("DiscoverFragment", "db getAllPostsWithTagRequest onComplete "+ result.toString());
                dataRepo.createTagEventMappingOnWorkerThread(result);
                swipeDownToRefresh.setRefreshing(false);
            }
        });
    }

    public void updateRecyclerEventsShown() {
        List<Post> subList = new ArrayList<>();
        EventTags buttonPressed = tagButtonAdapter.getSelectedTag();

        // Filter out events to show in recycler, based on currently selected tags
        if (buttonPressed.name().equals("ALL")){
            subList = new ArrayList<>(dataRepo.getAllEventsHashMap().values());
        } else {
            for (Integer postId: Objects.requireNonNull(dataRepo.getTagsEventMapping().get(buttonPressed))) {
                subList.add(dataRepo.getAllEventsHashMap().get(postId));
            }

        }
        // refresh adapter for event recycler
        discoverEventAdapter.updateEventList(subList);

//        Log.d("RYANTEST", "updateRecyclerEventsShown: ");
//        for (Integer id: dataRepo.getTagsEventMapping().get(buttonPressed)) {
//            Log.d("RYANTEST", String.valueOf(id));
//        }
    }

    public void updateRecyclerImageMapping() {
        discoverEventAdapter.updateEventImageMapping(dataRepo.getEventImageMapping());
    }
}
