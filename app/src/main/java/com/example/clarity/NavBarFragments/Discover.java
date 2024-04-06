package com.example.clarity.NavBarFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clarity.NavBarFragments.Buttons_change_tag.Tag_button_adapter;
import com.example.clarity.NavBarFragments.Buttons_change_tag.tag_button_model;
import com.example.clarity.NavBarFragments.Tag_fragments.Campus_life_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Career_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Competition_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Fifth_row_tag_fragment;
import com.example.clarity.R;

import java.util.*;

public class Discover extends Fragment {
    ArrayList<tag_button_model> tag_buttons = new ArrayList<>();
    // for tag button creation
    public enum EventTags {CAREER, CAMPUS_LIFE, FIFTH_ROW, COMPETITION, WORKSHOP}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        FragmentManager fragmentManager = getChildFragmentManager();

        // set up tag button recycler
        RecyclerView tag_recycler = rootView.findViewById(R.id.tag_recycler);

        Tag_button_adapter tagButtonAdapter = new Tag_button_adapter(requireContext(), tag_buttons, fragmentManager);
        tag_recycler.setAdapter(tagButtonAdapter);
        tag_recycler.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false));


        // Get the FragmentManager and begin a transaction

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the nested fragment to the container within the parent fragment's layout
//        fragmentTransaction.add(R.id.tags, new Career_tag_fragment());

        // Commit the transaction
        fragmentTransaction.commit();

        ArrayList<Button> buttonsList = tagButtonAdapter.getButtonsList();
        Log.d("Buttons List Size", String.valueOf(buttonsList.size()));


//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<Fragment> fragmentList = new ArrayList<>();
//                fragmentList.add(new Career_tag_fragment());
//                fragmentList.add(new Campus_life_tag_fragment());
//                fragmentList.add(new Fifth_row_tag_fragment());
//                fragmentList.add(new Competition_tag_fragment());
//                int count = 0;
//                for (Button button : buttonsList){
//                    if (v == button) {
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.tags, fragmentList.get(count));
//                        fragmentTransaction.commit();
//                        button.setBackgroundResource(R.drawable.tag_rectangle);
//                        button.setTextColor(Color.parseColor("#FDFAFF"));
//                    }
//                    else {
//                        button.setBackgroundResource(R.drawable.tag_unselect_rectangle);
//                        button.setTextColor(Color.parseColor("#967ADC"));
//                    }
//                    count +=1;
//                }
//            }
//        };
        for (Button button : buttonsList) {
//            button.setOnClickListener(listener);
        }
        return rootView;
    }

    public void setUpTagButtons() {
        String[] name = new String[5];
        name[0] = "CAREER";
        name[1] = "CAMPUS_LIFE";
        name[2] = "FIFTH_ROW";
        name[3] = "COMPETITION";
        name[4] = "WORKSHOP";
        for(int i = 0; i<name.length; i++){
            tag_buttons.add(new tag_button_model(name[i]));
        }
    }
}



