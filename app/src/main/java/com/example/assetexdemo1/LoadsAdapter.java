package com.example.assetexdemo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LoadsAdapter extends RecyclerView.Adapter<LoadsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<LoadsModel> loadsModels;

    public LoadsAdapter(Context context, ArrayList<LoadsModel> loadsModels) {
        this.context = context;
        this.loadsModels = loadsModels;
    }

    @NonNull
    @Override
    public LoadsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadsAdapter.ViewHolder holder, int position) {
        LoadsModel model = loadsModels.get(position);
        holder.loadNameView.setText(model.getLoadName());
        holder.loadClientView.setText(model.getLoadClient());
        holder.loadStatusView.setText(model.getLoadStatus());
        holder.loadDateView.setText(model.getLoadDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        holder.loadDueView.setText(model.getLoadDue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        holder.loadImageView.setImageResource(model.getLoadImage());
    }
    @Override
    public int getItemCount() {
        return loadsModels.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // private final ImageView courseIV;
        private final TextView loadNameView;
        private final TextView loadClientView;
        private final TextView loadStatusView;
        private final TextView loadDateView;
        private final TextView loadDueView;
        private final ImageView loadImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            loadNameView = itemView.findViewById(R.id.loadNameTV);
            loadClientView = itemView.findViewById(R.id.loadClientTV);
            loadStatusView = itemView.findViewById(R.id.loadStatusTV);
            loadDateView = itemView.findViewById(R.id.loadDateTV);
            loadDueView = itemView.findViewById(R.id.loadDueTV);
            loadImageView = itemView.findViewById(R.id.loadImageIV);

        }
    }
}
