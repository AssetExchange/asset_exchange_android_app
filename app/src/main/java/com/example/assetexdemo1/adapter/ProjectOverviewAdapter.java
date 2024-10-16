package com.example.assetexdemo1.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assetexdemo1.activity.ProjectItemViewActivity;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.model.ProjectModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ProjectOverviewAdapter extends RecyclerView.Adapter<ProjectOverviewAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ProjectModel> projectModels;

    Random rand = new Random();
    List<String> colors;


    public ProjectOverviewAdapter(Context context, ArrayList<ProjectModel> projectModels) {
        this.context = context;
        this.projectModels = projectModels;

        colors = Arrays.asList(new String[] {
                "#67666B", "#38B068", "#FFA00B", "#C85656", "#376094"
        });

        Collections.shuffle(colors);
    }

    @NonNull
    @Override
    public ProjectOverviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_project_overview_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectOverviewAdapter.ViewHolder holder, int position) {
        ProjectModel model = projectModels.get(position);

        holder.projectNameTV.setText(model.getProjectTitle());
        if (model.isPriority()) {
            holder.projectPriorityTV.setVisibility(View.VISIBLE);
        }
        else {
            holder.projectPriorityTV.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colors.get(position % colors.size()))));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectItemViewActivity.class);
                intent.putExtra("selected_project", model);
                startActivity(v.getContext(), intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView projectNameTV;
        private final ImageView projectPriorityTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectNameTV = itemView.findViewById(R.id.projectNameTV);
            projectPriorityTV = itemView.findViewById(R.id.projectPriorityTV);
        }
    }
}
