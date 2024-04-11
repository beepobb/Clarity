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
import com.example.clarity.model.data.User;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
//    private EditText editTextDate, editTextTime;
    private Calendar calendar;
    private View rootView;
    //start
    private RestRepo database;
    private User appUser;
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
        EditText start_dateEditText = rootView.findViewById(R.id.editTextDate);
        EditText start_timeEditText = rootView.findViewById(R.id.editTextTime);
        EditText locationEditText = rootView.findViewById(R.id.location_text);
        EditText end_dateEditText = rootView.findViewById(R.id.editTextDate2);
        EditText end_timeEditText = rootView.findViewById(R.id.editTextTime2);
        EditText descriptionEditText = rootView.findViewById(R.id.description_text);

        // get reference to db
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            database = ((MainActivity) activity).database;
            bottomNavigationView = ((MainActivity) activity).binding.bottomNavigationView;
        }
        // logged in user object
        appUser = ((MyApplication) getActivity().getApplicationContext()).getAppUser();
        Integer appUser_id = appUser.getId();
        userLiveData = new MutableLiveData<>();
        userLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String string) {
                Toast.makeText(getContext(), "Event succesfully added", Toast.LENGTH_LONG).show();
                Menu menu = bottomNavigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.Discover);
                bottomNavigationView.setSelectedItemId(menuItem.getItemId());
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

        calendar = Calendar.getInstance();
        start_dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(start_dateEditText);
            }
        });
        end_dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(end_dateEditText);
            }
        });

        start_timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(start_timeEditText);
            }
        });
        end_timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(end_timeEditText);
            }
        });

//        String dateString = editTextDate.getText().toString().trim();
//        String timeString = editTextTime.getText().toString().trim();
//        String dateTimeString = dateString + " " + timeString;
//        Log.d("DateTimeConcatenation", "Concatenated DateTime: " + dateTimeString);
//        // im trying to retrieve data that user enters and format it into our ISO format, but havent success

        //start
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer author_id = appUser_id;
                String title = titleEditText.getText().toString();
                String tagsString = tagsEditText.getText().toString();
                ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsString.split(",")));
                String start_date = start_dateEditText .getText().toString();
                String start_time = start_timeEditText.getText().toString();
                String end_date = end_dateEditText.getText().toString();
                String end_time = end_timeEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.event_placeholder5);

                if (author_id == null || title.isEmpty() || tags.isEmpty() || start_date.isEmpty() || end_date.isEmpty() || end_time.isEmpty() || start_date.isEmpty() || location.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String start = start_date + " " + start_time;
                String end = end_date + " " + end_time;

                database.addPostRequest(author_id, start, end, title,
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

    private void showDatePickerDialog(final EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Create a LocalDate object from the selected date components
                        LocalDate selectedDate = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                        }

                        // Define the formatter for ISO date format
                        DateTimeFormatter formatter = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter = DateTimeFormatter.ISO_DATE;
                        }

                        // Format the selected date using ISO date format
                        String formattedDate = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formattedDate = selectedDate.format(formatter);
                        }

                        // Set the formatted date to the EditText
                        editText.setText(formattedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
//MIGHT HAVE API VERSION ISSUES
    private void showTimePickerDialog(final EditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        LocalTime time = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            time = LocalTime.of(hourOfDay, minute);
                        }
                        DateTimeFormatter formatter = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formatter = DateTimeFormatter.ISO_TIME;
                        }
                        String formattedTime = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            formattedTime = time.format(formatter);
                        }
                        editText.setText(formattedTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
        );
        timePickerDialog.show();
    }

}