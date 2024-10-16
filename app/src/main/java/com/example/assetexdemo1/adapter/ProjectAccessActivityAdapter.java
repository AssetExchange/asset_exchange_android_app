package com.example.assetexdemo1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.assetexdemo1.DBConn;
import com.example.assetexdemo1.activity.ProfileViewerActivity;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.model.ProjectAccessModel;

import java.util.ArrayList;

public class ProjectAccessActivityAdapter extends RecyclerView.Adapter<ProjectAccessActivityAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ProjectAccessModel> projectAccessModels;

    public ProjectAccessActivityAdapter(Context context, ArrayList<ProjectAccessModel> projectAccessModels) {
        this.context = context;
        this.projectAccessModels = projectAccessModels;
    }

    @NonNull
    @Override
    public ProjectAccessActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_project_access, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAccessActivityAdapter.ViewHolder holder, int position) {
        ProjectAccessModel model = projectAccessModels.get(position);

        holder.projectAccessName.setText(model.getShareUser().getFullName());

        if (model.getShareUser().getProfilePicPath() != null) {
            String ext = model.getShareUser().getProfilePicPath().trim().toLowerCase();

            if (ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".gif")) {
                Glide.with(context)
                        .load(DBConn.getURL("filegator/repository/user/" + model.getShareUser().getProfilePicPath()))
                        .apply(
                                new RequestOptions()
                                        .placeholder(R.drawable.bottom_nav_menu_profile_icon)
                        )
                        .fitCenter()
                        .dontAnimate()
                        .into(holder.projectAccessProfileIcon);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileViewerActivity.class);
                intent.putExtra("user_model", model.getShareUser());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectAccessModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView projectAccessProfileIcon;
        private final TextView projectAccessName;
        private final TextView projectAccessDate;
        private final TextView projectAccessType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectAccessProfileIcon = itemView.findViewById(R.id.projectAccessProfileIcon);
            projectAccessName = itemView.findViewById(R.id.projectAccessName);
            projectAccessDate = itemView.findViewById(R.id.projectAccessDate);
            projectAccessType = itemView.findViewById(R.id.projectAccessType);
        }
    }
}
