package com.example.clarity.NavBarFragments;

import static android.app.Activity.RESULT_OK;
import android.graphics.Bitmap;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.clarity.MainActivity;
import com.example.clarity.MyApplication;
import com.example.clarity.model.repository.RestRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.clarity.R;

public class Create extends Fragment {

    private AutoCompleteTextView mAutoCompleteTextView; // Declare as class-level field
    private MultiAutoCompleteTextView mMultiAutoCompleteTextView;
    ArrayAdapter<String> adapterTags;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ImageView selectedImageView;
    private EditText editTextDate, editTextTime;
    private Calendar calendar;
    private View rootView;
    //start
    private RestRepo database;
    private MutableLiveData<String> userLiveData;
    private BottomNavigationView bottomNavigationView;
    private Bitmap bitmap;
//end
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create, container, false);
        selectedImageView = rootView.findViewById(R.id.placeHolder);
//start
        Button postButton = rootView.findViewById(R.id.postButton);
        ImageView placeholderImageView = rootView.findViewById(R.id.middle_image);
        EditText titleEditText = rootView.findViewById(R.id.editTextValue);
        EditText tagsEditText = rootView.findViewById(R.id.multiAutoCompleteTextView);
        EditText event_startEditText = rootView.findViewById(R.id.editTextDate);
        EditText event_endEditText = rootView.findViewById(R.id.editTextTime);
        EditText locationEditText = rootView.findViewById(R.id.location_text);
        EditText descriptionEditText = rootView.findViewById(R.id.description_text);
        EditText author_idEditText = rootView.findViewById(R.id.contact_text);

        // get reference to db
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            database = ((MainActivity) activity).database;
        }
        userLiveData = new MutableLiveData<>();
        userLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String string) {
                if (string == null) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Event succesfully added", Toast.LENGTH_LONG).show();
                    Menu menu = bottomNavigationView.getMenu();
                    MenuItem menuItem = menu.findItem(R.id.Discover);
                    bottomNavigationView.performClick();
                }
            }
        });
//end
        mMultiAutoCompleteTextView = rootView.findViewById(R.id.multiAutoCompleteTextView);
        String[] tags = {"Career", "Campus Life", "Fifth Row", "Competition", "Workshop"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, tags);

        // Set adapter to MultiAutoCompleteTextView
        mMultiAutoCompleteTextView.setAdapter(adapter);

        // Set tokenizer to separate multiple selections by comma or semicolon
        mMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // Set threshold for filtering
        mMultiAutoCompleteTextView.setThreshold(1);

        // Set up image selection
        imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            placeholderImageView.setVisibility(View.GONE);
                            Uri selectedImageUri = data.getData();
                            try {
                                // Convert URI to Bitmap, this bitmap variable refers to the image user upload
                                Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(selectedImageUri));

                                // Set the bitmap to ImageView
                                selectedImageView.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // Launch the gallery picker when the ImageView is clicked
        selectedImageView.setOnClickListener(view -> selectImage());

        editTextDate = rootView.findViewById(R.id.editTextDate);
        editTextTime = rootView.findViewById(R.id.editTextTime);
        calendar = Calendar.getInstance();
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        String dateString = editTextDate.getText().toString().trim();
        String timeString = editTextTime.getText().toString().trim();
        String dateTimeString = dateString + " " + timeString;
        Log.d("DateTimeConcatenation", "Concatenated DateTime: " + dateTimeString);
        // im trying to retrieve data that user enters and format it into our ISO format, but havent success

        //start
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer author_id = author_idEditText.getId();
                String title = titleEditText.getText().toString();
                String tagsString = tagsEditText.getText().toString();
                ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsString.split(",")));

                String event_start = event_startEditText.getText().toString();
                String event_end = event_endEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                Log.d("no bitmap", "error");
                Bitmap image = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    image = bitmap.asShared();
                    //TODO: check if asShared() is correct
                }
                Log.d("yes bitmap", "error");

                database.addPostRequest(author_id, event_start, event_end, title,
                        location, description, tags, image, new RestRepo.RepositoryCallback<String>() {
                            @Override
                            public void onComplete(String result) {
                                userLiveData.postValue(result);
                            }
                        });
            }
        });
        //end
        return rootView;
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            imageActivityResultLauncher.launch(intent);
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editTextTime.setText(hourOfDay + ":" + minute);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
        );
        timePickerDialog.show();
    }

}