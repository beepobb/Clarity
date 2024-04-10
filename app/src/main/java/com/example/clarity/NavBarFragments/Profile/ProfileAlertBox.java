package com.example.clarity.NavBarFragments.Profile;

import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;

import com.example.clarity.R;

public class ProfileAlertBox {
    private final Button button;
    private final Dialog dialog;
    private TextView actionDescription;

    ProfileAlertBox(int id, Dialog dialog, View view){
        this.button = view.findViewById(id);
        this.dialog = dialog;
        this.actionDescription = dialog.findViewById(R.id.action_description);
    }

    public void createAlert(String customText){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDescription.setText(customText);
                dialog.show();
            }
        });
    }

}
