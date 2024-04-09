package com.example.clarity.NavBarFragments.Profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clarity.MainActivity;
import com.example.clarity.NavBarFragments.DiscoverEventAdapter;
import com.example.clarity.R;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Fragment {
    private User user;
    private Dialog changePasswordDialog,alertDialog,editProfileDialog;
    private Button buttonChangePassword,buttonCancel,buttonConfirm,buttonAbout,buttonDonePassword,
            buttonDoneProfile, buttonLogOut;
    private View view;
    private RestRepo db;
    private TextView username,role;
    ImageView profilePicture;
    ImageButton editProfile;
    ProfileAlertBox resetCalendarAlertBox, resetAccountAlertBox, deactivateAccountAlertBox;
    TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fetch database (RestRepo instance)
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Context context = requireContext();

        //dummy user
        user = new User(3, "ifalltower", "jeui3ug4i836", "SUTD Student",
                "test@gmail.com", "2024-04-01 06:35:23");

        username = view.findViewById(R.id.username);
        role = view.findViewById(R.id.role);
        editProfile = view.findViewById(R.id.editFAB);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);

        changePasswordDialog = new Dialog(context);
        changePasswordDialog.setContentView(R.layout.change_password_box);
        changePasswordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        changePasswordDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
        changePasswordDialog.setCancelable(false);

        alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.alert_box);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
        alertDialog.setCancelable(false); // if user clicks outside alert box it will not disappear

        // Edit Profile Pop-up Window
        editProfileDialog = new Dialog(context);
        editProfileDialog.setContentView(R.layout.edit_profile_box);
        editProfileDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editProfileDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
        editProfileDialog.setCancelable(false); // if user clicks outside alert box it will not disappear

        buttonDonePassword = changePasswordDialog.findViewById(R.id.buttonDone);
        buttonCancel = alertDialog.findViewById(R.id.buttonCancel);
        buttonConfirm = alertDialog.findViewById(R.id.buttonConfirm);
        buttonDoneProfile = editProfileDialog.findViewById(R.id.buttonDone);

        Spinner spinner = editProfileDialog.findViewById(R.id.editAccountType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.dropdown_options, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("ProfileCreated", "onViewCreated");



        username.setText(user.getUsername());
        role.setText(user.getRole());
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog.show();
            }
        });

        buttonDonePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText oldPassword, newPassword, cfmPassword;
                oldPassword = changePasswordDialog.findViewById(R.id.oldPassword);
                newPassword = changePasswordDialog.findViewById(R.id.newPassword);
                cfmPassword = changePasswordDialog.findViewById(R.id.cfmPassword);
                //TODO: check if old password tally
                //TODO: check if new password tally with confirm password
                //TODO: update password if no issues, throw error if password does not match
                changePasswordDialog.dismiss();
            }
        });

        //set dialog box for reset calendar, reset account and delete account
        resetCalendarAlertBox = new ProfileAlertBox(R.id.buttonResetCalendar, alertDialog, view);
        resetCalendarAlertBox.createAlert("This action permanently deletes the calendar");

        deactivateAccountAlertBox = new ProfileAlertBox(R.id.buttonDeactivateAccount, alertDialog, view);
        deactivateAccountAlertBox.createAlert("This action permanently deletes the account and you cannot retrieve it");

        // initialise dialog buttons,
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); //close alert box
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = alertDialog.findViewById(R.id.action_description);
                //TODO: if deactivate account -> delete account from database and lock user out
                //TODO: if reset calendar -> remove all added events from local storage
                //TODO: toast/alert when done
                description.setText(""); // Placeholder action
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity( )==null) {
                    Log.i("null", "null activity");
                }
                editProfileDialog.show();
                Toast.makeText(getContext(), "edit profile dialog box",Toast.LENGTH_SHORT).show();
            }
        });

        buttonDoneProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: update username and role to database
                editProfileDialog.dismiss();
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: clear global variable that stores user object
                //TODO: go back to login page
            }
        });

    }
}