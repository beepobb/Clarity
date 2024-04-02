package com.example.clarity.NavBarFragments.Buttons_change_tag;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clarity.NavBarFragments.Tag_fragments.Campus_life_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Career_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Competition_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Fifth_row_tag_fragment;
import com.example.clarity.NavBarFragments.Tag_fragments.Workshop_tag_fragment;
import com.example.clarity.R;
import java.util.ArrayList;

public class Tag_button_adapter extends RecyclerView.Adapter<Tag_button_adapter.Tag_holder> {
    Context context;
    ArrayList<tag_button_model> tag_button_model_list;
    ArrayList<Button> buttonsList = new ArrayList<>();
    FragmentManager fragmentManager;
    public Tag_button_adapter(Context context, ArrayList<tag_button_model> tag_button_list, FragmentManager fragmentManager){
        this.context = context;
        this.tag_button_model_list = tag_button_list;
        this.fragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public Tag_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tag_button_model, parent, false);
        return new Tag_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Tag_holder holder, int position) {
        holder.button.setText(tag_button_model_list.get(position).getName());
        if (position == 0) {
            holder.button.setTextColor(Color.parseColor("#FDFAFF"));
            holder.button.setBackgroundResource(R.drawable.tag_rectangle);
        }
        buttonsList.add(holder.button);
        Log.d("TAG", getButtonsList().toString());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Fragment> fragmentList = new ArrayList<>();
                fragmentList.add(new Career_tag_fragment());
                fragmentList.add(new Campus_life_tag_fragment());
                fragmentList.add(new Fifth_row_tag_fragment());
                fragmentList.add(new Competition_tag_fragment());
                fragmentList.add(new Workshop_tag_fragment());
                int count = 0;
                for (Button button : buttonsList){
                    if (v == button) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.tags, fragmentList.get(count));
                        fragmentTransaction.commit();
                        button.setBackgroundResource(R.drawable.tag_rectangle);
                        button.setTextColor(Color.parseColor("#FDFAFF"));
                    }
                    else {
                        button.setBackgroundResource(R.drawable.tag_unselect_rectangle);
                        button.setTextColor(Color.parseColor("#967ADC"));
                    }
                    count +=1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tag_button_model_list.size();
    }

    public static class Tag_holder extends RecyclerView.ViewHolder {
        Button button;
        public Tag_holder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.Tag_button_model);
        }
    }

    public ArrayList<Button> getButtonsList() {
        Log.d("Buttons List Size2", buttonsList.toString());
        return buttonsList;
    }
}