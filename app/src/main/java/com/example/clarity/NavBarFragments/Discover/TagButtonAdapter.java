package com.example.clarity.NavBarFragments.Discover;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;

import java.util.ArrayList;
import java.util.List;

public class TagButtonAdapter extends RecyclerView.Adapter<TagButtonAdapter.TagButtonViewHolder>{
    private final boolean theme;
    private Context context;
    private List<EventTags> buttonNameList; // store name of buttons
    private MutableLiveData<EventTags> selectedTagLiveData;

    private List<Button> buttonsList; // store references to Button views
    private final String TAG = "TagButtonAdapter";

    public TagButtonAdapter(Context context, List<EventTags> buttonNameList, boolean theme) {
        Log.d(TAG, "TagButtonAdapter");
        this.context = context;
        this.buttonNameList = buttonNameList;
        this.buttonsList = new ArrayList<>();
        this.theme = theme;
        selectedTagLiveData = new MutableLiveData<>(EventTags.ALL); // first selected tag should be 'ALL'
    }

    public static class TagButtonViewHolder extends RecyclerView.ViewHolder {
        Button button;
        public TagButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("TagButtonAdapter", "TagButtonViewHolder");
            button = itemView.findViewById((R.id.tag_button_model));
        }
    }

    @NonNull
    @Override
    public TagButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_button_model, parent, false);
        return new TagButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagButtonViewHolder holder, int position) {
        // RecyclerView calls this to bind a EventViewHolder to data
        Log.d(TAG, "onBindViewHolder");

        EventTags buttonName = buttonNameList.get(position);
        buttonsList.add(holder.button);

        // bind content to UI
        holder.button.setText(buttonName.toString());

        // Determine background resource and text color based on the theme
        if (theme) { // Night mode
            holder.button.setBackgroundResource(R.drawable.tag_unselect_rectangle_night);
            holder.button.setTextColor(ContextCompat.getColor(context, R.color.purple_tint));
        } else { // Day mode
            holder.button.setBackgroundResource(R.drawable.tag_unselect_rectangle);
            holder.button.setTextColor(ContextCompat.getColor(context, R.color.dark_purple));
        }

        // Bind click listener
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTagLiveData.setValue(buttonName); // Will trigger observer in Discover, to change the events being presented

                // Update UI for all buttons based on what button was clicked
                for (Button button : buttonsList) {
                    // When button clicked, change background color and text color
                    if (view == button) {
                        button.setBackgroundResource(theme ? R.drawable.tag_select_rectangle_night : R.drawable.tag_select_rectangle);
                        button.setTextColor(ContextCompat.getColor(context, R.color.white));
                    } else {
                        // Change UI for buttons that are not clicked
                        button.setBackgroundResource(theme ? R.drawable.tag_unselect_rectangle_night : R.drawable.tag_unselect_rectangle);
                        button.setTextColor(ContextCompat.getColor(context, theme ? R.color.purple_tint : R.color.dark_purple));
                    }
                }
            }
        });

        if (position == 0) { // Select 'ALL' tag button on start-up
            holder.button.performClick();
        }
    }

    @Override
    public int getItemCount() {
        return buttonNameList.size();
    }

    public List<Button> getButtonsList() {
        return buttonsList;
    }
    public MutableLiveData<EventTags> getSelectedTagLiveData() { return selectedTagLiveData; }
    public EventTags getSelectedTag() { return selectedTagLiveData.getValue(); }
}
