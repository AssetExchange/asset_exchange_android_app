package com.example.assetexdemo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ProjectListItemsAdapter extends RecyclerView.Adapter<ProjectListItemsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<AssetModel> assetModels;

    public ProjectListItemsAdapter(Context context, ArrayList<AssetModel> assetModels) {
        this.context = context;
        this.assetModels = assetModels;
    }

    @NonNull
    @Override
    public ProjectListItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_project_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectListItemsAdapter.ViewHolder holder, int position) {
        AssetModel model = assetModels.get(position);

        String ext = model.getLatestRevisionFilePath().trim().toLowerCase();

        if (ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".gif")) {
            holder.projectListItemImageButton.setVisibility(View.VISIBLE);
            Glide.with(context)
                .load(DBConn.getURL("filegator/repository/user/" + model.getLatestRevisionFilePath()))
                .apply(
                    new RequestOptions()
                    .placeholder(android.R.drawable.screen_background_dark)
                    .error(R.drawable.password_cross)
                )
                .fitCenter()
                .dontAnimate()
                .into(holder.projectListItemImageButton);
            holder.projectListItemCaption.setVisibility(View.GONE);
        }
        else {
            Glide.with(context).clear(holder.projectListItemImageButton);
//            holder.projectListItemImageButton.setVisibility(View.GONE);
            holder.projectListItemImageButton.setBackgroundResource(R.drawable.bottom_nav_menu_filetext_icon);
            holder.projectListItemImageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.projectListItemCaption.setText(model.getAssetTitle());
            holder.projectListItemCaption.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return assetModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton projectListItemImageButton;
        private final TextView projectListItemCaption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectListItemImageButton = itemView.findViewById(R.id.projectListItemImageButton);
            projectListItemCaption = itemView.findViewById(R.id.projectListItemCaption);
        }
    }
}
