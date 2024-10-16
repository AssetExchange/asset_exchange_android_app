package com.example.assetexdemo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assetexdemo1.model.AssetModel;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.model.AssetVersionModel;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AssetRevisionVersionHistoryAdapter extends RecyclerView.Adapter<AssetRevisionVersionHistoryAdapter.ViewHolder> {
    private final Context context;
    private final AssetModel assetModel;
    private final ArrayList<AssetVersionModel> assetVersionModels;

    public AssetRevisionVersionHistoryAdapter(Context context, AssetModel assetModel, ArrayList<AssetVersionModel> assetVersionModels) {
        this.context = context;
        this.assetModel = assetModel;
        this.assetVersionModels = assetVersionModels;
    }

    @NonNull
    @Override
    public AssetRevisionVersionHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_asset_version_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetRevisionVersionHistoryAdapter.ViewHolder holder, int position) {
        AssetVersionModel model = assetVersionModels.get(position);

        holder.assetVersionHistoryComment.setText(model.getComment());
        holder.assetVersionHistoryDateRevised.setText(model.getDateCreated().format(DateTimeFormatter.ofPattern("LLLL d, yyyy h:mm:ss a")));

        if (model.getRevisionId().equals(assetModel.getLatestRevision())) {
            holder.assetVersionHistoryLatestVersion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return assetVersionModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView assetVersionHistoryComment;
        private final TextView assetVersionHistoryDateRevised;
        private final TextView assetVersionHistoryLatestVersion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assetVersionHistoryComment = itemView.findViewById(R.id.assetVersionHistoryComment);
            assetVersionHistoryDateRevised = itemView.findViewById(R.id.assetVersionHistoryDateRevised);
            assetVersionHistoryLatestVersion = itemView.findViewById(R.id.assetVersionHistoryLatestVersion);
        }
    }
}
