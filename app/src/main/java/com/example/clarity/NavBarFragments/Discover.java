package com.example.clarity.NavBarFragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.clarity.NavBarFragments.Tag_fragments.Campus_life_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Career_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Competition_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Fifth_row_tag_fragment;
import com.example.clarity.R;

public class Discover extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);

        // Get the FragmentManager and begin a transaction
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the nested fragment to the container within the parent fragment's layout
        fragmentTransaction.add(R.id.tags, new Career_tag_fragment());

        // Commit the transaction
        fragmentTransaction.commit();
        Button CareerButton = rootView.findViewById(R.id.Button_Career);
        Button CampusLifeButton = rootView.findViewById(R.id.Button_Campus_life);
        Button FifthRowButton = rootView.findViewById(R.id.Button_Fifth_row);
        Button Competition = rootView.findViewById(R.id.Button_Competition);

        CareerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.tags, new Career_tag_fragment());
                CareerButton.setBackgroundResource(R.drawable.tag_rectangle);
                CareerButton.setTextColor(Color.parseColor("#FDFAFF"));
                CampusLifeButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                CampusLifeButton.setTextColor(Color.parseColor("#967ADC"));
                FifthRowButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                FifthRowButton.setTextColor(Color.parseColor("#967ADC"));
                Competition.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                Competition.setTextColor(Color.parseColor("#967ADC"));
                fragmentTransaction.commit();
            }
        });
        CampusLifeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.tags, new Campus_life_tag_fragment());
                CareerButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                CareerButton.setTextColor(Color.parseColor("#967ADC"));
                CampusLifeButton.setBackgroundResource(R.drawable.tag_rectangle);
                CampusLifeButton.setTextColor(Color.parseColor("#FDFAFF"));
                FifthRowButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                FifthRowButton.setTextColor(Color.parseColor("#967ADC"));
                Competition.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                Competition.setTextColor(Color.parseColor("#967ADC"));
                fragmentTransaction.commit();
            }
        });
        FifthRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.tags, new Fifth_row_tag_fragment());
                CareerButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                CareerButton.setTextColor(Color.parseColor("#967ADC"));
                CampusLifeButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                CampusLifeButton.setTextColor(Color.parseColor("#967ADC"));
                FifthRowButton.setBackgroundResource(R.drawable.tag_rectangle);
                FifthRowButton.setTextColor(Color.parseColor("#FDFAFF"));
                Competition.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                Competition.setTextColor(Color.parseColor("#967ADC"));
                fragmentTransaction.commit();
            }
        });

        Competition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.tags, new Competition_tag_fragment());
                CareerButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                CareerButton.setTextColor(Color.parseColor("#967ADC"));
                CampusLifeButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                CampusLifeButton.setTextColor(Color.parseColor("#967ADC"));
                FifthRowButton.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                FifthRowButton.setTextColor(Color.parseColor("#967ADC"));
                Competition.setBackgroundResource(R.drawable.tag_rectangle);
                Competition.setTextColor(Color.parseColor("#FDFAFF"));
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }
}



