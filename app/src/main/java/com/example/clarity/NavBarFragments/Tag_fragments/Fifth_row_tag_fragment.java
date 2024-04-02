package com.example.clarity.NavBarFragments.Tag_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;
import java.util.ArrayList;

public class Fifth_row_tag_fragment extends Fragment {
    ArrayList<Event_model> list_of_event= new ArrayList<>();
    int[] images = {R.drawable.event_placeholder6, R.drawable.event_placeholder5, R.drawable.event_placeholder4};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewroot = inflater.inflate(R.layout.tag_fragment_fifth_row, container, false);
        RecyclerView recyclerView = viewroot.findViewById(R.id.Fifth_row);
        setUpEventList();
        Event_adapter adapter = new Event_adapter(requireContext(), list_of_event );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return viewroot;


    }
    //        probably can modify later, whenever db update, call this method. can make it public in discover
    //        java file since every fragment of tags are going to use this function.
    public void setUpEventList(){
        String[] name = getResources().getStringArray(R.array.Event_name);
        String[] time = getResources().getStringArray(R.array.time);
        String[] description = getResources().getStringArray(R.array.Description);
        String[] location = getResources().getStringArray(R.array.Location);

        for(int i = 0; i<images.length; i++){
            list_of_event.add(new Event_model(name[i], time[i], description[i], location[i], images[i]));


        }

    }
}
