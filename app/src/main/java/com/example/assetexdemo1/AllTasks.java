package com.example.assetexdemo1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;

public class AllTasks extends AppCompatActivity implements AddReminderBottomSheet.BottomSheetListener, OnDataPass {
    Button allTasksRemindButton;

    ArrayList<TasksModel> todayTasksModels;
    ArrayList<TasksModel> priorityTasksModels;
    ArrayList<TasksModel> scheduledTasksModels;
    ArrayList<TasksModel> missingTasksModels;
    ArrayList<TasksModel> allTasksModels;

    ArrayList<TasksModel> filteredTodayTasksModels;
    ArrayList<TasksModel> filteredPriorityTasksModels;
    ArrayList<TasksModel> filteredScheduledTasksModels;
    ArrayList<TasksModel> filteredMissingTasksModels;
    ArrayList<TasksModel> filteredAllTasksModels;

    AllTasksAdapter priorityTasksAdapter;
    AllTasksAdapter todayTasksAdapter;
    AllTasksAdapter scheduledTasksAdapter;
    AllTasksAdapter missingTasksAdapter;
    AllTasksAdapter allTasksAdapter;

    RecyclerView priorityTasksRV;
    RecyclerView todayTasksRV;
    RecyclerView scheduledTasksRV;
    RecyclerView missingTasksRV;
    RecyclerView allTasksRV;

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

        ImageButton filterButton = findViewById(R.id.filterAllTasks);

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

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortFilterBottomSheet sortFilterBottomSheet = new SortFilterBottomSheet();
                sortFilterBottomSheet.setDataPassListener(AllTasks.this);
                sortFilterBottomSheet.show(getSupportFragmentManager(), sortFilterBottomSheet.getTag());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        priorityTasksRV = findViewById(R.id.allPriorityTasksRecyclerView);
        todayTasksRV = findViewById(R.id.allTodayTasksRecyclerView);
        scheduledTasksRV = findViewById(R.id.allScheduledTasksRecyclerView);
        missingTasksRV = findViewById(R.id.allMissingTasksRecyclerView);
        allTasksRV = findViewById(R.id.allTasksRecyclerView);

        todayTasksModels = new ArrayList<>();
        priorityTasksModels = new ArrayList<>();
        scheduledTasksModels = new ArrayList<>();
        missingTasksModels = new ArrayList<>();
        allTasksModels = new ArrayList<>();

        priorityTasksAdapter = new AllTasksAdapter(this, priorityTasksModels);
        todayTasksAdapter = new AllTasksAdapter(this, todayTasksModels);
        scheduledTasksAdapter = new AllTasksAdapter(this, scheduledTasksModels);
        missingTasksAdapter = new AllTasksAdapter(this, missingTasksModels);
        allTasksAdapter = new AllTasksAdapter(this, allTasksModels);

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

        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        allTasksRV.setLayoutManager(linearLayoutManager5);

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
                                 allTasksModels.add(tasksModel);
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
                                 allTasksModels.add(tasksModel);
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

    @Override
    public void onDataPass(SortFilterOptions data) {
        if (allTasksModels != null && data != null) {
            if (data.getSortBy() != 0) {
                Collections.sort(allTasksModels, new Comparator<TasksModel>() {
                    @Override
                    public int compare(TasksModel lhs, TasksModel rhs) {
                        if (data.getPriorityFirst()) {
                            int a = Boolean.compare(rhs.isPriority(),lhs.isPriority());
                            if (a != 0) {
                                return a;
                            }
                        }
                        if (data.getSortBy() == 1) {
                            return -1 * rhs.getTaskTitle().compareToIgnoreCase(lhs.getTaskTitle());
                        }
                        else if (data.getSortBy() == 2) {
                            return rhs.getTaskTitle().compareToIgnoreCase(lhs.getTaskTitle());
                        }
                        else if (data.getSortBy() == 3) {
                            if (rhs.getDateCreated() == null) {
                                if (lhs.getDateCreated() == null) {
                                    return 0;
                                }
                                else {
                                    return -1;
                                }
                            }
                            else if (lhs.getDateCreated() == null) {
                                return 1;
                            }
                            else {
                                return rhs.getDateCreated().compareTo(lhs.getDateCreated());
                            }
                        }
                        else if (data.getSortBy() == 4) {
                            if (rhs.getDateCreated() == null) {
                                if (lhs.getDateCreated() == null) {
                                    return 0;
                                }
                                else {
                                    return 1;
                                }
                            }
                            else if (lhs.getDateCreated() == null) {
                                return -1;
                            }
                            else {
                                return -1 * rhs.getDateCreated().compareTo(lhs.getDateCreated());
                            }
                        }
                        else if (data.getSortBy() == 5) {
                            if (rhs.getDueDate() == null) {
                                if (lhs.getDueDate() == null) {
                                    return 0;
                                }
                                else {
                                    return -1;
                                }
                            }
                            else if (lhs.getDueDate() == null) {
                                return 1;
                            }
                            else {
                                return rhs.getDueDate().compareTo(lhs.getDueDate());
                            }
                        }
                        else if (data.getSortBy() == 6) {
                            if (rhs.getDueDate() == null) {
                                if (lhs.getDueDate() == null) {
                                    return 0;
                                }
                                else {
                                    return 1;
                                }
                            }
                            else if (lhs.getDueDate() == null) {
                                return -1;
                            }
                            else {
                                return -1 * rhs.getDueDate().compareTo(lhs.getDueDate());
                            }
                        }
                        else if (data.getSortBy() == 7) {
                            if (rhs.getDateCompleted() == null) {
                                if (lhs.getDateCompleted() == null) {
                                    return 0;
                                }
                                else {
                                    return -1;
                                }
                            }
                            else if (lhs.getDateCompleted() == null) {
                                return 1;
                            }
                            else {
                                return rhs.getDateCompleted().compareTo(lhs.getDateCompleted());
                            }
                        }
                        else if (data.getSortBy() == 8) {
                            if (rhs.getDateCompleted() == null) {
                                if (lhs.getDateCompleted() == null) {
                                    return 0;
                                }
                                else {
                                    return 1;
                                }
                            }
                            else if (lhs.getDateCompleted() == null) {
                                return -1;
                            }
                            else {
                                return -1 * rhs.getDateCompleted().compareTo(lhs.getDateCompleted());
                            }
                        }
                        else {
                            return rhs.getTaskTitle().compareToIgnoreCase(lhs.getTaskTitle());
                        }
                    }
                });

                allTasksRV.setAdapter(allTasksAdapter);
                priorityTasksRV.setVisibility(View.GONE);
                scheduledTasksRV.setVisibility(View.GONE);
                todayTasksRV.setVisibility(View.GONE);
                missingTasksRV.setVisibility(View.GONE);

                findViewById(R.id.textView16).setVisibility(View.GONE);
                findViewById(R.id.textView20).setVisibility(View.GONE);
                findViewById(R.id.textView10).setVisibility(View.GONE);
                findViewById(R.id.textView21).setVisibility(View.GONE);
            }

            if (data.getLimitResults()) {
                priorityTasksModels = new ArrayList<TasksModel>(priorityTasksModels.subList(0, Math.min(priorityTasksModels.size(), 5)));
                missingTasksModels = new ArrayList<TasksModel>(missingTasksModels.subList(0,Math.min(missingTasksModels.size(), 5)));
                scheduledTasksModels = new ArrayList<TasksModel>(scheduledTasksModels.subList(0,Math.min(scheduledTasksModels.size(), 5)));
                todayTasksModels = new ArrayList<TasksModel>(todayTasksModels.subList(0,Math.min(todayTasksModels.size(), 5)));
                allTasksModels = new ArrayList<TasksModel>(allTasksModels.subList(0,Math.min(allTasksModels.size(), 20)));
            }

            if (data.getSearchTerm() != null && !data.getSearchTerm().trim().isEmpty()) {
                List<BoundExtractedResult<TasksModel>> searchResults = FuzzySearch.extractSorted(data.getSearchTerm(), allTasksModels, x-> x.getTaskTitle() + (x.getTaskDescription() != null ? " " + x.getTaskDescription() : ""), 50);
                ArrayList<TasksModel> searchModels = new ArrayList<>();

                System.out.println(searchResults);

                for (BoundExtractedResult<TasksModel> result : searchResults) {
                    searchModels.add(result.getReferent());
                }

                allTasksRV.setAdapter(allTasksAdapter);
                priorityTasksRV.setVisibility(View.GONE);
                scheduledTasksRV.setVisibility(View.GONE);
                todayTasksRV.setVisibility(View.GONE);
                missingTasksRV.setVisibility(View.GONE);

                findViewById(R.id.textView16).setVisibility(View.GONE);
                findViewById(R.id.textView20).setVisibility(View.GONE);
                findViewById(R.id.textView10).setVisibility(View.GONE);
                findViewById(R.id.textView21).setVisibility(View.GONE);

                allTasksRV.setVisibility(View.VISIBLE);

                priorityTasksModels.removeIf(element -> !searchModels.contains(element));
                missingTasksModels.removeIf(element -> !searchModels.contains(element));
                scheduledTasksModels.removeIf(element -> !searchModels.contains(element));
                todayTasksModels.removeIf(element -> !searchModels.contains(element));
                allTasksModels.removeIf(element -> !searchModels.contains(element));

                priorityTasksAdapter.notifyDataSetChanged();
                missingTasksAdapter.notifyDataSetChanged();
                scheduledTasksAdapter.notifyDataSetChanged();
                todayTasksAdapter.notifyDataSetChanged();
                allTasksAdapter.notifyDataSetChanged();
            }
        }


    }
}