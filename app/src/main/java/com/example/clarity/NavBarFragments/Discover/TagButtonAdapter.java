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
    private Context context;
    private List<EventTags> buttonNameList; // store name of buttons
    private MutableLiveData<EventTags> selectedTagLiveData;

    private List<Button> buttonsList; // store references to Button views
    private final String TAG = "TagButtonAdapter";

    public TagButtonAdapter(Context context, List<EventTags> buttonNameList) {
        Log.d(TAG, "TagButtonAdapter");
        this.context = context;
        this.buttonNameList = buttonNameList;
        this.buttonsList = new ArrayList<>();
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
        int pos = position;

        // bind content to UI
        holder.button.setText(buttonName.toString());

        // bind click listener
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTagLiveData.setValue(buttonName); // will trigger observer in Discover, to change the events being presented

                // Update UI for all buttons based on what button was clicked
                for (Button button : buttonsList) {
                    // when button clicked, change background color and text color
                    if (view == button) {
                        Log.d(TAG, view.toString());
                        button.setBackgroundResource(R.drawable.tag_select_rectangle);
                        button.setTextColor(ContextCompat.getColor(context, R.color.white));

                        // updates eventRecycler in Discover Fragment
                        // tagButtonUpdateEventsClickListener.onButtonClick(pos);
                    // change UI for buttons that are not clicked
                    } else {
                        button.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                        button.setTextColor(ContextCompat.getColor(context, R.color.accent_1));
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
