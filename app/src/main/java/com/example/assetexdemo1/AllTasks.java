package com.example.assetexdemo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AllTasks extends AppCompatActivity implements AddReminderBottomSheet.BottomSheetListener {
    MaterialButton allTasksRemindButton;

    ArrayList<TasksModel> todayTasksModels;
    ArrayList<TasksModel> priorityTasksModels;
    ArrayList<TasksModel> scheduledTasksModels;
    ArrayList<TasksModel> missingTasksModels;

    AllTasksAdapter priorityTasksAdapter;
    AllTasksAdapter todayTasksAdapter;
    AllTasksAdapter scheduledTasksAdapter;
    AllTasksAdapter missingTasksAdapter;

    SharedPreferences sharedPref;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_tasks);

        allTasksRemindButton = findViewById(R.id.allTasksRemindButton);
        progressBar = findViewById(R.id.progressBar7);

        sharedPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

        allTasksRemindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReminderBottomSheet bottomSheet = new AddReminderBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        }});

        // Find the ImageButton by its ID
        ImageButton backButton = findViewById(R.id.backAllTasks);

        // Set OnClickListener on the ImageButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Create an Intent to start the MainActivity
//                Intent intent = new Intent(All_Tasks.this, MainActivity.class);
//                startActivity(intent);
//                // Optionally finish the current activity
//                finish();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView priorityTasksRV = findViewById(R.id.allTasksRecyclerView);
        RecyclerView todayTasksRV = findViewById(R.id.allTodayTasksRecyclerView);
        RecyclerView scheduledTasksRV = findViewById(R.id.allScheduledTasksRecyclerView);
        RecyclerView missingTasksRV = findViewById(R.id.allMissingTasksRecyclerView);

        todayTasksModels = new ArrayList<>();
        priorityTasksModels = new ArrayList<>();
        scheduledTasksModels = new ArrayList<>();
        missingTasksModels = new ArrayList<>();

        priorityTasksAdapter = new AllTasksAdapter(this, priorityTasksModels);
        todayTasksAdapter = new AllTasksAdapter(this, todayTasksModels);
        scheduledTasksAdapter = new AllTasksAdapter(this, scheduledTasksModels);
        missingTasksAdapter = new AllTasksAdapter(this, missingTasksModels);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        priorityTasksRV.setLayoutManager(linearLayoutManager);
        priorityTasksRV.setAdapter(priorityTasksAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        todayTasksRV.setLayoutManager(linearLayoutManager2);
        todayTasksRV.setAdapter(todayTasksAdapter);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        scheduledTasksRV.setLayoutManager(linearLayoutManager3);
        scheduledTasksRV.setAdapter(scheduledTasksAdapter);

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        missingTasksRV.setLayoutManager(linearLayoutManager4);
        missingTasksRV.setAdapter(missingTasksAdapter);

        progressBar.setVisibility(View.VISIBLE);
        DBConn.getRequest(
            DBConn.getRecordURL("tasks?filter=task_owner_id,eq," + sharedPref.getString("user_id", "1")),
            this,
            new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {}
                @Override
                public void innerResponse(Object object, Context context) {
                    progressBar.setVisibility(View.GONE);
                    if (object instanceof JSONObject) {

                    }
                     else if (object instanceof JSONArray) {
                         System.out.println(object);
                         List<TasksModel> dataList = new ArrayList<>();

                         for (int i = 0; i < ((JSONArray) object).length(); i++) {
                             try {
                                 JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);

                                 TasksModel tasksModel = new TasksModel(
                                     jsonObject.getInt("task_id"),
                                     jsonObject.getString("task_title"),
                                     jsonObject.getString("task_description"),
                                     jsonObject.getInt("task_owner_id"),
                                     jsonObject.getBoolean("priority"),
                                     jsonObject.isNull("date_created") ? null : LocalDateTime.parse(jsonObject.getString("date_created"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                     jsonObject.isNull("due_date") ? null : LocalDateTime.parse(jsonObject.getString("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                     jsonObject.isNull("date_modified") ? null : LocalDateTime.parse(jsonObject.getString("date_modified"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                     jsonObject.isNull("date_completed") ? null : LocalDateTime.parse(jsonObject.getString("date_completed"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"))
                                 );

                                 if (!tasksModel.isCompleted()) {
                                     if (tasksModel.getDueDate() != null && tasksModel.getDueDate().toLocalDate().isEqual(LocalDate.now())) {
                                         todayTasksModels.add(tasksModel);
                                     }
                                     else if (tasksModel.getDueDate() != null && tasksModel.isPriority()) {
                                         priorityTasksModels.add(tasksModel);
                                     }
                                     else if (tasksModel.getDueDate() != null && tasksModel.getDueDate().isAfter((LocalDateTime.now()))) {
                                         scheduledTasksModels.add(tasksModel);
                                     }
                                     else {
                                         missingTasksModels.add(tasksModel);
                                     }
                                 }

                             }
                             catch (JSONException e) {
                                 throw new RuntimeException(e);
                             }
                         }
//
                         priorityTasksRV.setAdapter(priorityTasksAdapter);
                         todayTasksRV.setAdapter(todayTasksAdapter);
                         scheduledTasksRV.setAdapter(scheduledTasksAdapter);
                         missingTasksRV.setAdapter(missingTasksAdapter);
                     }
                }
            },
                "Unable to connect to the database",
                "Unable to parse API response"
        );
    }

    public void onUpdateData() {
        DBConn.getRequest(
            DBConn.getRecordURL("tasks?filter=task_owner_id,eq," + sharedPref.getString("user_id", "1")),
            this,
            new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {}
                @Override
                public void innerResponse(Object object, Context context) {
                    if (object instanceof JSONObject) {

                    }
                     else if (object instanceof JSONArray) {
                         System.out.println(object);

                         for (int i = 0; i < ((JSONArray) object).length(); i++) {
                             try {
                                 JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);

                                 TasksModel tasksModel = new TasksModel(
                                     jsonObject.getInt("task_id"),
                                     jsonObject.getString("task_title"),
                                     jsonObject.getString("task_description"),
                                     jsonObject.getInt("task_owner_id"),
                                     jsonObject.getBoolean("priority"),
                                     jsonObject.isNull("date_created") ? null : LocalDateTime.parse(jsonObject.getString("date_created"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                     jsonObject.isNull("due_date") ? null : LocalDateTime.parse(jsonObject.getString("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                     jsonObject.isNull("date_modified") ? null : LocalDateTime.parse(jsonObject.getString("date_modified"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                     jsonObject.isNull("date_completed") ? null : LocalDateTime.parse(jsonObject.getString("date_completed"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"))
                                 );

                                 if (!tasksModel.isCompleted()) {
                                     if (tasksModel.getDueDate() != null && tasksModel.getDueDate().toLocalDate().isEqual(LocalDate.now())) {
                                         todayTasksModels.add(tasksModel);
                                     }
                                     else if (tasksModel.getDueDate() != null && tasksModel.isPriority()) {
                                         priorityTasksModels.add(tasksModel);
                                     }
                                     else if (tasksModel.getDueDate() != null && tasksModel.getDueDate().isAfter((LocalDateTime.now()))) {
                                         scheduledTasksModels.add(tasksModel);
                                     }
                                     else {
                                         missingTasksModels.add(tasksModel);
                                     }
                                 }

                             }
                             catch (JSONException e) {
                                 throw new RuntimeException(e);
                             }
                         }

                         priorityTasksAdapter.notifyDataSetChanged();
                         todayTasksAdapter.notifyDataSetChanged();
                         scheduledTasksAdapter.notifyDataSetChanged();
                         missingTasksAdapter.notifyDataSetChanged();
                     }
                }
            },
                "Unable to connect to the database",
                "Unable to parse API response"
        );
    }
}