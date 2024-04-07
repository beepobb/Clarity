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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.clarity.R;

public class Create extends Fragment {
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ImageView selectedImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_create, container, false);
        selectedImageView = rootview.findViewById(R.id.placeHolder);
        imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            // Load the selected image into the ImageView
                            selectedImageView.setImageURI(selectedImageUri);
                        }
                    }
                });

        // Launch the gallery picker when the ImageView is clicked
        selectedImageView.setOnClickListener(view -> selectImage());
        return rootview;
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            imageActivityResultLauncher.launch(intent);
        }
    }
}