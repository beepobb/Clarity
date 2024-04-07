package com.example.clarity.NavBarFragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.example.clarity.R;

public class Create extends Fragment {
    String[] tags = {"Career","Campus Life","Fifth Row","Competition","Workshop"};
    AutoCompleteTextView autoCompleteTextView; // Declare as class-level field
    ArrayAdapter<String> adapterTags;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ImageView selectedImageView;
    private View rootView; // Declare as class-level field

//    private ChipGroup chipGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create, container, false);
        selectedImageView = rootView.findViewById(R.id.placeHolder);
        //chipGroup = rootView.findViewById(R.id.chipGroup);

        // Initialize AutoCompleteTextView and adapter
        autoCompleteTextView = rootView.findViewById(R.id.autoCompleteTextView);
        adapterTags = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, tags);
        autoCompleteTextView.setAdapter(adapterTags);

        // Add chips dynamically
//        for (String tag : tags) {
//            Chip chip = new Chip(requireContext());
//            chip.setText(tag);
//            chip.setCheckable(true);
//            chip.setOnCheckedChangeListener((compoundButton, isChecked) -> {
//                if (isChecked && chipGroup.getCheckedChipIds().size() > 3) {
//                    // Uncheck the chip if more than 3 are selected
//                    chip.setChecked(false);
//                    Toast.makeText(requireContext(), "You can select up to 3 tags.", Toast.LENGTH_SHORT).show();
//                }
//            });
//            chipGroup.addView(chip);
//        }

        // Set up image selection
        imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            selectedImageView.setImageURI(selectedImageUri);
                        }
                    }
                });

        // Launch the gallery picker when the ImageView is clicked
        selectedImageView.setOnClickListener(view -> selectImage());

        return rootView;
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            imageActivityResultLauncher.launch(intent);
        }
    }
}
