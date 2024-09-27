package com.example.assetexdemo1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProjectOverviewAdapter extends RecyclerView.Adapter<ProjectOverviewAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ProjectModel> projectModels;

    public ProjectOverviewAdapter(Context context, ArrayList<ProjectModel> projectModels) {
        this.context = context;
        this.projectModels = projectModels;
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
