package com.example.clarity.NavBarFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.clarity.MainActivity;
import com.example.clarity.MyApplication;
import com.example.clarity.MyDataRepository;
import com.example.clarity.NavBarFragments.Discover.DiscoverEventAdapter;
import com.example.clarity.R;
import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;
import com.example.clarity.ui.login.LoginActivity;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Fragment {
    private PreferenceUtils userPrefs;
    private MyApplication appContext;
    private User appUser;
    private Dialog alertDialog;
    private Button buttonResetCalendar,buttonCancel,buttonConfirm,buttonLogOut;
    private View view;
    private RestRepo db;
    private MyDataRepository dataRepo;
    private TextView username,role,alertBoxAction;
    ShapeableImageView profilePicture;
    TextView description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPrefs = PreferenceUtils.getInstance(getActivity());
        dataRepo = MyDataRepository.getInstance();
        
        // Fetch database (RestRepo instance)
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        }

        appContext = ((MyApplication) getActivity().getApplicationContext());

        // Get logged-in user
        appUser = appContext.getAppUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Context context = requireContext();

        username = view.findViewById(R.id.username);
        role = view.findViewById(R.id.role);
        profilePicture = view.findViewById(R.id.placeholderProfilePic);
        buttonResetCalendar = view.findViewById(R.id.buttonResetCalendar);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);

        alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.alert_box);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_bg);
        alertDialog.setCancelable(false); // if user clicks outside alert box it will not disappear
        alertBoxAction = alertDialog.findViewById(R.id.action_description);

        buttonCancel = alertDialog.findViewById(R.id.buttonCancel);
        buttonConfirm = alertDialog.findViewById(R.id.buttonConfirm);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.dropdown_options, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("ProfileCreated", "onViewCreated");



        username.setText(appUser.getUsername());
        role.setText(appUser.getRole());
        db.getImageRequest(appUser.getProfile_pic_url(), new RestRepo.RepositoryCallback<Bitmap>() {
            @Override
            public void onComplete(Bitmap result) {
                profilePicture.setImageBitmap(result);
            }
        });

        buttonResetCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertBoxAction.setText("This action remove all events from the calendar.");
                alertDialog.show();
            }
        });

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
                dataRepo.resetSavedEventsOnMainThread(); // triggers observers to refresh UI
                userPrefs.resetCalendar();
                userPrefs.commitCalendarUpdates();
                alertDialog.dismiss();
                Toast.makeText(getContext(), "Calendar reset done", Toast.LENGTH_SHORT).show();; // Placeholder action
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Placeholder (until session token)
                appContext.saveAppUser(null); // delete the User object that was saved
                userPrefs.clearSessionToken(); // delete session token
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}