package com.example.assetexdemo1.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assetexdemo1.R;
import com.example.assetexdemo1.model.TasksModel;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class CompletedTasksAdapter extends RecyclerView.Adapter<CompletedTasksAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<TasksModel> taskModels;

    public CompletedTasksAdapter(Context context, ArrayList<TasksModel> taskModels) {
        this.context = context;
        this.taskModels = taskModels;
    }

    @NonNull
    @Override
    public CompletedTasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_completed_tasks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedTasksAdapter.ViewHolder holder, int position) {
        TasksModel model = taskModels.get(position);

        holder.taskRadio.setText(model.getTaskTitle());
        holder.taskRadio.setChecked(model.isCompleted());
        holder.taskRadio.setVisibility(View.GONE);

        holder.taskTitle.setText(model.getTaskTitle());

        if (model.getDueDate() != null) {
            // holder.taskDueDate.setText(model.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            holder.taskDueDate.setText(DateUtils.formatSameDayTime(
                    model.getDueDate().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli(),
                    System.currentTimeMillis(),
                    DateFormat.MEDIUM, DateFormat.SHORT
            ));
            if (model.isPriority()) {
                holder.taskRadio.setTextColor(Color.parseColor("#4069BB"));
                holder.taskRadio.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#4069BB")));
                holder.taskDueDate.setTextColor(Color.parseColor("#4069BB"));
            }
            else if (model.getDueDate().toLocalDate().isEqual(LocalDate.now())) {
                // today
                holder.taskDueDate.setTextColor(Color.parseColor("#F44E1C"));
            }
            else if (model.getDueDate().isBefore(LocalDateTime.now())) {
                // missed the due
                holder.taskRadio.setTextColor(Color.parseColor("#E44D4D"));
                holder.taskRadio.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#E44D4D")));
                holder.taskDueDate.setTextColor(Color.parseColor("#E44D4D"));
            }
            else {
                // scheduled
                holder.taskRadio.setTextColor(Color.parseColor("#AE46BF"));
                holder.taskRadio.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#AE46BF")));
                holder.taskDueDate.setTextColor(Color.parseColor("#AE46BF"));
            }

        }
        else if (model.getDueDate() == null) {
            // missed without due
            holder.taskDueDate.setVisibility(View.GONE);

            holder.taskRadio.setTextColor(Color.parseColor("#E44D4D"));
            holder.taskRadio.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#E44D4D")));
        }
        else {
            holder.taskDueDate.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.taskRadio.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.taskModels.size();
    }

    public void removeItem(int position) {
        taskModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox taskRadio;
        private final TextView taskDueDate;
        private final TextView taskTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskRadio = itemView.findViewById(R.id.taskRadio);
            taskDueDate = itemView.findViewById(R.id.dateDueTextView);
            taskTitle = itemView.findViewById(R.id.taskTitleTextView);

        }
    }
}
