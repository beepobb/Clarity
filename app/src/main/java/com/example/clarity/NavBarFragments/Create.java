package com.example.clarity.NavBarFragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.clarity.MainActivity;
import com.example.clarity.MyApplication;
import com.example.clarity.model.data.User;
import com.example.clarity.model.repository.RestRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.clarity.R;

public class Create extends Fragment {


    private MultiAutoCompleteTextView mMultiAutoCompleteTextView;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ImageView selectedImageView;
    private Calendar calendar;
    private View rootView;
    private RestRepo database;
    private User appUser;
    private MutableLiveData<String> userLiveData;
    private MutableLiveData<String> userdescriptionLiveData;
    private BottomNavigationView bottomNavigationView;
    private Bitmap image;
    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] langArray = {"CAREER", "CAMPUS LIFE", "FIFTH ROW", "COMPETITION", "WORKSHOP"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create, container, false);
        selectedImageView = rootView.findViewById(R.id.placeHolder);
        Button postButton = rootView.findViewById(R.id.postButton);
        ImageView placeholderImageView = rootView.findViewById(R.id.middle_image);
        EditText titleEditText = rootView.findViewById(R.id.editTextValue);
        TextView tagsEditText = rootView.findViewById(R.id.textView);
        EditText start_dateEditText = rootView.findViewById(R.id.editTextDate);
        EditText start_timeEditText = rootView.findViewById(R.id.editTextTime);
        EditText locationEditText = rootView.findViewById(R.id.location_text);
        EditText end_dateEditText = rootView.findViewById(R.id.editTextDate2);
        EditText end_timeEditText = rootView.findViewById(R.id.editTextTime2);
        EditText descriptionEditText = rootView.findViewById(R.id.description_text);
        Spinner AIornotSpinner = rootView.findViewById(R.id.spinner);
        Log.d("EESONG", AIornotSpinner.toString());
        ProgressBar progressBar = rootView.findViewById(R.id.progress_bar);
        Handler handler = new Handler();

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
        userdescriptionLiveData = new MutableLiveData<>();


        userLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String string) {
                //opens Discover fragment
                Menu menu = bottomNavigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.Discover);
                bottomNavigationView.setSelectedItemId(menuItem.getItemId());
                //resets all text fields
                titleEditText.setText("");
                tagsEditText.setText("");
                start_dateEditText.setText("");
                end_dateEditText.setText("");
                start_timeEditText.setText("");
                end_timeEditText.setText("");
                locationEditText.setText("");
                descriptionEditText.setText("");
                progressBar.setVisibility(View.GONE);
            }
        });

        //triggers when user wants to use AI to generate description
        userdescriptionLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String string) {
                descriptionEditText.setText(string);
            }
        });

        //
        imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            placeholderImageView.setVisibility(View.GONE);
                            Uri selectedImageUri = data.getData();
                            try {
                                Glide.with(this)
                                        .asBitmap()
                                        .load(selectedImageUri)
                                        .into(new BitmapImageViewTarget(selectedImageView) {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                                super.onResourceReady(bitmap, transition);
                                                // Assign the loaded Bitmap to the image variable
                                                image = bitmap;
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // assign variable
        TextView textView  = rootView.findViewById(R.id.textView);

        // initialize selected language array
        selectedLanguage = new boolean[langArray.length];

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                // set title
                builder.setTitle("Select Tags");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // Check if the number of selected items exceeds three
                        if (b && langList.size() >= 3) {
                            // If more than three options are selected, uncheck the current item
                            ((AlertDialog) dialogInterface).getListView().setItemChecked(i, false);
                            Toast.makeText(requireContext(), "You can select up to three options", Toast.LENGTH_SHORT).show();
                        } else {
                            // If less than three options are selected, proceed as usual
                            if (b) {
                                langList.add(i);
                            } else {
                                langList.remove(Integer.valueOf(i));
                            }
                            Collections.sort(langList);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            textView.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
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

        AIornotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = AIornotSpinner.getSelectedItem().toString();

                if (choice.equals("I want to use AI!")) {
                    database.bitmapToTextSummaryRequest(image, new RestRepo.RepositoryCallback<String>() {
                        @Override
                        public void onComplete(String result) {
                            userdescriptionLiveData.postValue(result);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //start
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer author_id = appUser_id;
                String title = titleEditText.getText().toString();
                String tagsString = tagsEditText.getText().toString().trim();
                ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagsString.split(",")));
                String start_date = start_dateEditText .getText().toString();
                String start_time = start_timeEditText.getText().toString();
                String end_date = end_dateEditText.getText().toString();
                String end_time = end_timeEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                if (image == null) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.event_placeholder5);
                }

                if (author_id == null || title.isEmpty() || tags.isEmpty() || start_date.isEmpty() || end_date.isEmpty() || end_time.isEmpty() || start_date.isEmpty() || location.isEmpty() || description.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), "Please wait for your event to be added", Toast.LENGTH_LONG).show();
                            }
                        });
                        int progressStatus = 0;
                        while (progressStatus < 100) {
                            progressStatus += 1;

                            // Update the progress bar and display the current value
                            int finalProgressStatus = progressStatus;
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(finalProgressStatus);
                                }
                            });

                            try {
                                // Sleep for 200 milliseconds to simulate a long operation
                                Thread.sleep(40);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // Once the operation is completed, show a toast message
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), "Event successfully added", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();

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
    private void hideKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMultiAutoCompleteTextView.getWindowToken(), 0);
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
//Use most recent API version (Pixel 5 API 34)
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