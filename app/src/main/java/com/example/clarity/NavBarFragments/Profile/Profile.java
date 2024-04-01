package com.example.clarity.NavBarFragments.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clarity.R;
import com.example.clarity.adapters.CalendarEventAdapter;
import com.example.clarity.adapters.ProfileInterestAdapter;
import com.example.clarity.model.Interest;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profile extends Fragment {
    private Dialog dialog;
    private Button cancelAction, resetCalendar;
    private View view;
    private List<Interest> interestList = new ArrayList<>();
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        // Return the inflated layout
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton editProfile = view.findViewById(R.id.buttonEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile profile = new EditProfile();
                if (getActivity( )!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profile).commit();
                }else{
                    Log.i("null", "null activity");
                }
                Toast.makeText(getContext(), "Activity detected",Toast.LENGTH_SHORT).show();
            }
        });

        Context context = requireContext();
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
        dialog.setCancelable(false); // if user clicks outside alert box it will not disappear

        //set dialog box for reset calendar, reset account and delete account
        ProfileAlertBox resetCalendarAlertBox = new ProfileAlertBox(R.id.resetCalendarConstraintLayout, dialog, view);
        resetCalendarAlertBox.createAlert("This action permanently deletes the calendar");

        ProfileAlertBox resetAccountAlertBox = new ProfileAlertBox(R.id.resetAccountConstraintLayout, dialog, view);
        resetAccountAlertBox.createAlert("This action permanently deletes all data except username and account type");

        ProfileAlertBox deleteAccountAlertBox = new ProfileAlertBox(R.id.deleteAccountConstraintLayout, dialog, view);
        deleteAccountAlertBox.createAlert("This action permanently deletes the account and you cannot retrieve it");

        // TODO: change image logo for settings in fragment_profile.xml

        // initialise dialog buttons
        cancelAction = dialog.findViewById(R.id.buttonCancelAction);
        resetCalendar = dialog.findViewById(R.id.buttonResetCalendar);

        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); //close alert box
            }
        });

        resetCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView description = dialog.findViewById(R.id.action_description);
                description.setText("");
            }
        });

        // RECYCLER VIEW SET-UP
        // Placeholder for data source
        interestList.add(new Interest("Technology"));
        interestList.add(new Interest("Event"));
        interestList.add(new Interest("Poop"));
        interestList.add(new Interest("Workshop"));
        interestList.add(new Interest("Seminar"));

        // Set up RecyclerView and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ProfileInterestAdapter adapter = new ProfileInterestAdapter(getActivity(), interestList);
        recyclerView.setAdapter(adapter);

    }
}