package com.example.assetexdemo1.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assetexdemo1.R;
import com.example.assetexdemo1.model.ProjectModel;

import java.util.ArrayList;

public class SelectProjectFromAssetAdapter extends RecyclerView.Adapter<SelectProjectFromAssetAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ProjectModel> projectModels;
    private ProjectModel selectedProjectModel;
    private final OnProjectSelectListener onProjectSelectListener;
    private int lastClickedPosition = -1;

    public SelectProjectFromAssetAdapter(Context context, ArrayList<ProjectModel> projectModels, OnProjectSelectListener onProjectSelectListener) {
        this.context = context;
        this.projectModels = projectModels;
        this.onProjectSelectListener = onProjectSelectListener;
    }

    @NonNull
    @Override
    public SelectProjectFromAssetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_select_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectProjectFromAssetAdapter.ViewHolder holder, int position) {
        ProjectModel model = projectModels.get(position);

        holder.projectNameTV.setText(model.getProjectTitle());
        holder.projectNameTV.setTypeface(null, Typeface.NORMAL);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.projectNameTV.setTypeface(null, Typeface.BOLD);

                if (lastClickedPosition != -1) {
                    notifyItemChanged(lastClickedPosition);
                }

                lastClickedPosition = position;

                selectedProjectModel = model;
                onProjectSelectListener.onProjectSelect(selectedProjectModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectModels.size();
    }

    public ProjectModel getSelectedProjectModel() {
        return selectedProjectModel;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView projectNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectNameTV = itemView.findViewById(R.id.projectNameTV);
        }
    }

    public interface OnProjectSelectListener {
        void onProjectSelect(ProjectModel model);
    }
}

