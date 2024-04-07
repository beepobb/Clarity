package com.example.clarity.NavBarFragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.R;

import java.util.List;

public class TagButtonAdapter extends RecyclerView.Adapter<TagButtonAdapter.TagButtonViewHolder>{

    private Context context;
    private List<Discover.EventTags> buttonNameList;
    private List<Button> buttonList;
    public TagButtonAdapter(Context context, List<Discover.EventTags> buttonNameList) {
        this.context = context;
        this.buttonNameList = buttonNameList;
        Log.d("TagButtonAdapter", "TagButtonAdapter");
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
        Log.d("TagButtonAdapter", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_button_model, parent, false);
        return new TagButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagButtonViewHolder holder, int position) {
        // RecyclerView calls this to bind a EventViewHolder to data
        Log.d("TagButtonAdapter", "onBindViewHolder");
        Discover.EventTags buttonName = buttonNameList.get(position);

        // bind all content to UI
        holder.button.setText(buttonName.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To be set
                Toast.makeText(context, holder.button.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return buttonNameList.size();
    }

    // TODO: programmatically generate tags based on what tags is in db
    public void updateTags() {

    }
}
