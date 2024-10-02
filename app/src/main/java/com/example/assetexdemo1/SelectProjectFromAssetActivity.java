package com.example.assetexdemo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SelectProjectFromAssetActivity extends AppCompatActivity {
    ProjectModel projectModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_project_from_asset);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton backSelectProject = findViewById(R.id.backSelectProject);
        Button selectProjectAddButton = findViewById(R.id.selectProjectAddButton);

        backSelectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectModel != null) {
                    Intent intent = getIntent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
                else {
                    finish();
                }
            }
        });

        selectProjectAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (projectModel != null) {
                    Intent intent = getIntent();
                    intent.putExtra("selected_project", projectModel);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView selectProjectRV = findViewById(R.id.selectProjectRecyclerView);
        ArrayList<ProjectModel> selectProjectModels = new ArrayList<>();

        SelectProjectFromAssetAdapter selectProjectAdapter = new SelectProjectFromAssetAdapter(this, selectProjectModels, new SelectProjectFromAssetAdapter.OnProjectSelectListener() {
            @Override
            public void onProjectSelect(ProjectModel model) {
                projectModel = model;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        selectProjectRV.setLayoutManager(linearLayoutManager);
        selectProjectRV.setAdapter(selectProjectAdapter);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

        DBConn.getRequest(
            DBConn.getRecordURL("projects?filter=project_owner_id,eq," + sharedPref.getString("user_id", "1")),
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

                                 ProjectModel projectModel = new ProjectModel(
                                     jsonObject.getInt("project_id"),
                                     jsonObject.isNull("date_created") ? null : LocalDateTime.parse(jsonObject.getString("date_created"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                     jsonObject.getInt("project_owner_id"),
                                     jsonObject.getString("project_title"),
                                     jsonObject.getBoolean("priority"),
                                     jsonObject.getString("project_image_path"),
                                     jsonObject.getString("project_description")
                                 );

                                 selectProjectModels.add(projectModel);

                             }
                             catch (JSONException e) {
                                 throw new RuntimeException(e);
                             }
                         }
//
                         selectProjectRV.setAdapter(selectProjectAdapter);
                     }
                }
            },
            "Unable to connect to the database",
            "Unable to parse API response"
        );

    }
}