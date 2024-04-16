package com.example.clarity.NavBarFragments.Discover;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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

public class Discover extends Fragment implements TagButtonUpdateEventsClickListener {
    // stores info for buttons and events UI
    private List<EventTags> tagButtons;

    // observed data
    private MutableLiveData<List<Post>> eventListLive;
    private MutableLiveData<HashMap<Integer, Bitmap>> eventImageMappingLive;

    // UI elements
    private RecyclerView tagRecycler;
    private RecyclerView eventRecycler;
    private TagButtonAdapter tagButtonAdapter;
    private DiscoverEventAdapter discoverEventAdapter;

    private RestRepo db; // reference for db
    private MyDataRepository dataRepo; // central location for information

    private final String TAG = "DiscoverFragment";
    private SwipeRefreshLayout swipeDownToRefresh;

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
        eventImageMappingLive = new MutableLiveData<>(new HashMap<>());
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
        tagButtonAdapter = new TagButtonAdapter(getActivity(), tagButtons, this);
        tagRecycler.setAdapter(tagButtonAdapter);


        // set up event recycler
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        discoverEventAdapter = new DiscoverEventAdapter(getActivity(), new ArrayList<>()); // pass in empty arraylist
        eventRecycler.setAdapter(discoverEventAdapter);

        // set up observer for allEventsLive, will update UI when data comes in
        dataRepo.getAllEventsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                Log.d(TAG, "eventListLive observer called");

                HashMap<Integer, Bitmap> tmpMap = new HashMap<>();
                // get images for every Post object
                for (Post post : Objects.requireNonNull(dataRepo.getAllEventsLiveData().getValue())) {
                    String url = post.getImage_url();
                    Integer post_id = post.getId();
                    db.getImageRequest(url, new RestRepo.RepositoryCallback<Bitmap>() {
                        @Override
                        public void onComplete(Bitmap result) {
                            Log.d(TAG, "onComplete: ");
                            tmpMap.put(post_id, result);
                            dataRepo.loadEventImageMappingLiveData(tmpMap);
                        }
                    });
                }
                updateEventRecycler();
            }
        });

        // set up observer for eventImageMappingLive, update image in cardView
        dataRepo.getEventImageMappingLiveData().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Bitmap>>() {
            @Override
            public void onChanged(HashMap<Integer, Bitmap> integerBitmapHashMap) {
                Log.d(TAG, "eventImageMappingLive observer called");
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
                Log.d("DiscoverFragment", "db onComplete " + result.toString());

                // update allEventsLive, observer (UI) will be notified
                dataRepo.loadAllEventsOnWorkerThread(result);
            }
        });

        // get tags and events
        db.getAllPostsWithTagRequest(new RestRepo.RepositoryCallback<ArrayList<Tag>>() {
            @Override
            public void onComplete(ArrayList<Tag> result) {
                Log.d("DiscoverFragment", "db getAllPostsWithTagRequest onComplete "+ result.toString());
                dataRepo.createTagEventMapping(result);
            }
        });

        // TODO: swipe to refresh might be broken
        swipeDownToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                db.getAllPostsWithTagRequest(new RestRepo.RepositoryCallback<ArrayList<Tag>>() {
                    @Override
                    public void onComplete(ArrayList<Tag> result) {
                        Log.d("DiscoverFragment", "db getAllPostsWithTagRequest onComplete "+ result.toString());
                        HashMap<EventTags, ArrayList<Integer>> tagsEventMapping = dataRepo.getTagsEventMapping();
                        for (Tag tag : result) {
                            Integer post_id = tag.getPost_id();
                            String tag_category = tag.getTag_category();

                            if (tag_category.equals(EventTags.FIFTH_ROW.name())) {
                                if (!tagsEventMapping.get(EventTags.FIFTH_ROW).contains(post_id)){
                                    tagsEventMapping.get(EventTags.FIFTH_ROW).add(post_id);
                                }
                            } else if (tag_category.equals(EventTags.CAREER.name())) {
                                if (!tagsEventMapping.get(EventTags.CAREER).contains(post_id)) {
                                    tagsEventMapping.get(EventTags.CAREER).add(post_id);
                                }
                            } else if (tag_category.equals(EventTags.WORKSHOP.name())) {
                                if (!tagsEventMapping.get(EventTags.WORKSHOP).contains(post_id)){
                                    tagsEventMapping.get(EventTags.WORKSHOP).add(post_id);
                                }
                            } else if (tag_category.equals(EventTags.CAMPUS_LIFE.name())) {
                                if (!tagsEventMapping.get(EventTags.CAMPUS_LIFE).contains(post_id)){
                                    tagsEventMapping.get(EventTags.CAMPUS_LIFE).add(post_id);
                                }
                            } else if (tag_category.equals(EventTags.COMPETITION.name())) {
                                if (!tagsEventMapping.get(EventTags.COMPETITION).contains(post_id)){
                                    tagsEventMapping.get(EventTags.COMPETITION).add(post_id);
                                }
                            }
                        }
                    }
                });
                Log.d("RefreshDiscoverFragment", "Check for new events and update Discover fragment");

                // end refresh state on main thread
                assert getActivity()!=null;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("onRefresh", "Refresh discover page done.");
                        swipeDownToRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    // Helper function
    public void updateEventRecycler() {
        discoverEventAdapter.updateEventList(dataRepo.getAllEventsLiveData().getValue());
        discoverEventAdapter.updateEventImageMapping(dataRepo.getEventImageMappingLiveData().getValue());
    }

    /**
     * tagRecycler buttons call this function to change the eventRecycler
     * @param position of the tags buttons in the recycler
     */
    @Override
    public void onButtonClick(int position) {
//      // Toast.makeText(getActivity(), "TAG BUTTON CLICKED "+tagButtons.get(position).toString(), Toast.LENGTH_SHORT).show();
        Log.d("DiscoverFragment", tagButtons.get(position).toString());

        List<Post> subList = new ArrayList<>();
        EventTags buttonPressed = tagButtons.get(position);
        if (buttonPressed.name().equals("ALL")){
            subList = dataRepo.getAllEventsLiveData().getValue();
        } else {
            // generate a sublist of events based on the tag button that was clicked
            for (Post post : Objects.requireNonNull(dataRepo.getAllEventsLiveData().getValue())) {
                for (Integer id : Objects.requireNonNull(dataRepo.getTagsEventMapping().get(buttonPressed))) {
                    if (post.getId() == id) {
                        subList.add(post);
                    }
                }
            }
        }
        // refresh adapter for event recycler
        discoverEventAdapter.updateEventList(subList);
    }
}
