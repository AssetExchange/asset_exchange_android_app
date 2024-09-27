package com.example.assetexdemo1;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompletedTasks extends AppCompatActivity {
    LinearLayout completedLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_completed_tasks);

        // Find the ImageButton by its ID
        ImageButton backButton = findViewById(R.id.backCompletedTasks);
        completedLinearLayout = findViewById(R.id.completedLinearLayout);


        // Set OnClickListener on the ImageButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DBConn.getRequest(
            DBConn.getRecordURL("tasks?filter=task_owner_id,eq,1"),
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
                        List<TasksModel> dataList = new ArrayList<>();

                        List<JSONObject> completedList = new ArrayList<>();

                        for (int i = 0; i < ((JSONArray) object).length(); i++) {
                            try {
                                JSONObject jObject = ((JSONArray) object).getJSONObject(i);
                                if (!jObject.isNull("date_completed")) {
                                    completedList.add(jObject);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        Map<LocalDateTime, List<JSONObject>> groupedList = completedList.stream().collect(
                            Collectors.groupingBy(jsonObject -> {
                                try {
                                    return LocalDateTime.parse(
                                            jsonObject.getString("date_completed"),
                                            DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")
                                    ).truncatedTo(ChronoUnit.DAYS);
                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        ));


                        for (Map.Entry<LocalDateTime, List<JSONObject>> dateTimeGroup : groupedList.entrySet()) {
                            System.out.println(dateTimeGroup.getKey());

                            TextView textView = new TextView(CompletedTasks.this);
                            textView.setText(dateTimeGroup.getKey().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
                            textView.setTypeface(null, Typeface.BOLD);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 26,0,0);
                            textView.setLayoutParams(params);

                            completedLinearLayout.addView(textView);

                            RecyclerView groupTasksRV = new RecyclerView(CompletedTasks.this);
                            ArrayList<TasksModel> groupTasksModels = new ArrayList<>();

                            for (JSONObject object1 : dateTimeGroup.getValue()) {
                                try {
                                    TasksModel tasksModel = new TasksModel(
                                         object1.getInt("task_id"),
                                         object1.getString("task_title"),
                                         object1.getString("task_description"),
                                         object1.getInt("task_owner_id"),
                                         object1.getBoolean("priority"),
                                         object1.isNull("date_created") ? null : LocalDateTime.parse(object1.getString("date_created"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                         object1.isNull("due_date") ? null : LocalDateTime.parse(object1.getString("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                         object1.isNull("date_modified") ? null : LocalDateTime.parse(object1.getString("date_modified"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                         object1.isNull("date_completed") ? null : LocalDateTime.parse(object1.getString("date_completed"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"))
                                     );

                                    groupTasksModels.add(tasksModel);
                                }
                                catch (JSONException e) {}
                            }

                            CompletedTasksAdapter groupTasksAdapter = new CompletedTasksAdapter(CompletedTasks.this, groupTasksModels);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CompletedTasks.this, LinearLayoutManager.VERTICAL, false);
                            groupTasksRV.setLayoutManager(linearLayoutManager);
                            groupTasksRV.setAdapter(groupTasksAdapter);
                        }


//                         for (int i = 0; i < ((JSONArray) object).length(); i++) {
//                             try {
//                                 JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);
//
//                                 TasksModel tasksModel = new TasksModel(
//                                     jsonObject.getInt("task_id"),
//                                     jsonObject.getString("task_title"),
//                                     jsonObject.getString("task_description"),
//                                     jsonObject.getInt("task_owner_id"),
//                                     jsonObject.getBoolean("priority"),
//                                     jsonObject.isNull("date_created") ? null : LocalDateTime.parse(jsonObject.getString("date_created"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
//                                     jsonObject.isNull("due_date") ? null : LocalDateTime.parse(jsonObject.getString("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
//                                     jsonObject.isNull("date_modified") ? null : LocalDateTime.parse(jsonObject.getString("date_modified"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
//                                     jsonObject.isNull("date_completed") ? null : LocalDateTime.parse(jsonObject.getString("date_completed"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss"))
//                                 );
//
//                                 if (!tasksModel.isCompleted()) {
//                                     if (tasksModel.getDueDate() != null && tasksModel.getDueDate().toLocalDate().isEqual(LocalDate.now())) {
//                                         todayTasksModels.add(tasksModel);
//                                     }
//                                     else if (tasksModel.getDueDate() != null && tasksModel.isPriority()) {
//                                         priorityTasksModels.add(tasksModel);
//                                     }
//                                     else if (tasksModel.getDueDate() != null && tasksModel.getDueDate().isAfter((LocalDateTime.now()))) {
//                                         scheduledTasksModels.add(tasksModel);
//                                     }
//                                     else {
//                                         missingTasksModels.add(tasksModel);
//                                     }
//                                 }
//
//                             }
//                             catch (JSONException e) {
//                                 throw new RuntimeException(e);
//                             }
//                         }
////
//                         priorityTasksRV.setAdapter(priorityTasksAdapter);
//                         todayTasksRV.setAdapter(todayTasksAdapter);
//                         scheduledTasksRV.setAdapter(scheduledTasksAdapter);
//                         missingTasksRV.setAdapter(missingTasksAdapter);
                     }
                }
            },
                "Unable to connect to the database",
                "Unable to parse API response"
        );
    }
}