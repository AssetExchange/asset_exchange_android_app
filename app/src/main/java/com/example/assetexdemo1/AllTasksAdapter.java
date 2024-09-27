package com.example.assetexdemo1;

import static android.text.format.DateUtils.*;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<TasksModel> taskModels;

    public AllTasksAdapter(Context context, ArrayList<TasksModel> taskModels) {
        this.context = context;
        this.taskModels = taskModels;
    }

    @NonNull
    @Override
    public AllTasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_all_tasks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTasksAdapter.ViewHolder holder, int position) {
        TasksModel model = taskModels.get(position);

        holder.taskRadio.setText(model.getTaskTitle());
        holder.taskRadio.setChecked(model.isCompleted());

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

        holder.taskRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    model.setCompleted();
                    holder.taskRadio.setPaintFlags(holder.taskRadio.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    JSONObject params = new JSONObject();
                    try {
                        params.put("date_completed", DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                        DBConn.putRequest(DBConn.getRecordURL("tasks/" + model.getTaskId()), buttonView.getContext(), params, new DBConn.ResponseCallback() {
                                @Override
                                public void innerResponse(Object object) {
                                    removeItem(holder.getBindingAdapterPosition());
                                }
                            },
                            "Unable to connect to the database",
                            "Unable to parse API response"
                        );
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    model.setDateCompleted(null);
                    holder.taskRadio.setPaintFlags(holder.taskRadio.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskRadio = itemView.findViewById(R.id.taskRadio);
            taskDueDate = itemView.findViewById(R.id.dateDueTextView);
        }
    }
}
