package com.example.assetexdemo1;

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

import java.util.ArrayList;

public class ProjectRevisionsCommentsAdapter extends RecyclerView.Adapter<ProjectRevisionsCommentsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<RevisionCommentsModel> revisionCommentsModels;

    public ProjectRevisionsCommentsAdapter(Context context, ArrayList<RevisionCommentsModel> revisionCommentsModels) {
        this.context = context;
        this.revisionCommentsModels = revisionCommentsModels;
    }

    @NonNull
    @Override
    public ProjectRevisionsCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_project_revisions_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RevisionCommentsModel model = revisionCommentsModels.get(position);

        holder.projectRevisionsName.setText(model.getRevisionCommentUser().getFullName());
        holder.projectRevisionsActionType.setText(model.getAsset().getAssetTitle());
        holder.projectRevisionsType.setText(model.getRevisionCommentText());

        if (model.getRevisionCommentUser().getProfilePicPath() != null) {
            String ext = model.getRevisionCommentUser().getProfilePicPath().trim().toLowerCase();

            if (ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".gif")) {
                Glide.with(context)
                        .load(DBConn.getURL("filegator/repository/user/" + model.getRevisionCommentUser().getProfilePicPath()))
                        .apply(
                                new RequestOptions()
                                        .placeholder(R.drawable.bottom_nav_menu_profile_icon)
                        )
                        .fitCenter()
                        .dontAnimate()
                        .into(holder.projectRevisionsProfileIcon);
            }
        }

        holder.projectRevisionsProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileViewerActivity.class);
                intent.putExtra("user_model", model.getRevisionCommentUser());
                context.startActivity(intent);
            }
        });

         holder.projectRevisionsType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AssetRevisionActivity.class);
                intent.putExtra("asset_model", model.getAsset());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return revisionCommentsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView projectRevisionsProfileIcon;
        private final TextView projectRevisionsName;
        private final TextView projectRevisionsActionType;
        private final TextView projectRevisionsType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectRevisionsProfileIcon = itemView.findViewById(R.id.projectRevisionsProfileIcon);
            projectRevisionsName = itemView.findViewById(R.id.projectRevisionsName);
            projectRevisionsActionType = itemView.findViewById(R.id.projectRevisionsActionType);
            projectRevisionsType = itemView.findViewById(R.id.projectRevisionsType);
        }
    }
}
