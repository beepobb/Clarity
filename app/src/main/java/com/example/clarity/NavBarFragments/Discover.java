package com.example.clarity.NavBarFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;
import com.example.clarity.model.data.Post;

import java.util.*;

public class Discover extends Fragment {
    // for tag button creation
    public enum EventTags {
        CAREER, CAMPUS_LIFE, FIFTH_ROW, COMPETITION, WORKSHOP
    }

    private EventTags currentTagFilter; // used to toggle the events listed

    // stores info for buttons and events UI
    List<EventTags> tagButtons;
    List<Post> eventList;

    private RecyclerView tagRecycler;
    private RecyclerView eventRecycler;
    private TagButtonAdapter tagButtonAdapter;
    private DiscoverEventAdapter discoverEventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
//        FragmentManager fragmentManager = getChildFragmentManager();

        // get reference to UI elements
        tagRecycler = view.findViewById(R.id.tag_recycler);
        eventRecycler = view.findViewById(R.id.event_recycler);

        // initialise values
        tagButtons = Arrays.asList(EventTags.values());
        eventList = new ArrayList<>();
        Log.d("DiscoverFragment", tagButtons.toString());
        Post p1 = new Post(4,
                2,
                "2024-04-04 04:51:21",
                "None",
                "hello",
                "First post 3",
                "STUD3",
                "What is this?3",
                "2024-04-02 05:21:25");
        Post p2 = new Post(1,
                2,
                "2024-04-02 04:51:21",
                "2024-05-02 04:51:21",
                "hello",
                "First post",
                "STUD",
                "What is this?",
                "2024-04-02 04:51:21");
        eventList.add(p1);
        eventList.add(p2);

        // set up tag button recycler
        tagRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagButtonAdapter = new TagButtonAdapter(getActivity(), tagButtons);
        tagRecycler.setAdapter(tagButtonAdapter);

        // set up event recycler
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        discoverEventAdapter = new DiscoverEventAdapter(getActivity(), eventList);
        eventRecycler.setAdapter(discoverEventAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i("DiscoverFragment", "onViewCreate");

        // bind button click listeners

    }

    public void setUpTagButtons() {
        String[] name = {EventTags.CAREER.toString(),
                EventTags.CAMPUS_LIFE.toString(),
                EventTags.FIFTH_ROW.toString(),
                EventTags.COMPETITION.toString(),
                EventTags.WORKSHOP.toString()};
        for (String s : name) {
//            tag_buttons.add(new tag_button_model(s));
        }
    }
}



