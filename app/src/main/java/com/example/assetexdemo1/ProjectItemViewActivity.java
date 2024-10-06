package com.example.assetexdemo1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ProjectItemViewActivity extends AppCompatActivity {
    ProjectModel projectModel;

    TextView projectItemViewProjectTitle;
    TextView projectItemViewProjectOwner;
    TextView projectItemViewProjectDescription;
    Chip projectItemViewProjectDateChip;
    Chip projectItemViewProjectPriorityChip;
    Chip projectItemViewProjectShareChip;
    ImageView projectItemViewProjectPicture;
    ImageButton projectItemViewLatestAsset;
    TextView projectItemViewLatestAssetText;
    ConstraintLayout projectItemViewLatestAssetConstraintLayout;
    ImageButton projectItemViewBackButton;
    ImageButton projectItemViewActivityButton;
    Button projectItemViewUploadAssetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_project_item_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        projectItemViewProjectTitle = findViewById(R.id.projectItemViewProjectTitle);
        projectItemViewProjectOwner = findViewById(R.id.projectItemViewProjectOwner);
        projectItemViewProjectDescription = findViewById(R.id.projectItemViewProjectDescription);
        projectItemViewProjectDateChip = findViewById(R.id.projectItemViewProjectDateChip);
        projectItemViewProjectPriorityChip = findViewById(R.id.projectItemViewProjectPriorityChip);
        projectItemViewProjectShareChip = findViewById(R.id.projectItemViewProjectShareChip);
        projectItemViewProjectPicture = findViewById(R.id.projectItemViewProjectPicture);
        projectItemViewLatestAsset = findViewById(R.id.projectItemViewLatestAsset);
        projectItemViewLatestAssetText = findViewById(R.id.projectItemViewLatestAssetText);
        projectItemViewLatestAssetConstraintLayout = findViewById(R.id.projectItemViewLatestAssetConstraintLayout);
        projectItemViewBackButton = findViewById(R.id.projectItemViewBackButton);
        projectItemViewActivityButton = findViewById(R.id.projectItemViewActivityButton);
        projectItemViewUploadAssetButton = findViewById(R.id.projectItemViewUploadAssetButton);

        projectModel = getIntent().getParcelableExtra("selected_project");

        if (projectModel != null) {
            projectItemViewProjectTitle.setText(projectModel.getProjectTitle());

            RecyclerView projectItemViewAllAssetsRV = findViewById(R.id.projectItemViewAllAssetsRV);

            if (projectModel.isPriority()) {
                projectItemViewProjectPriorityChip.setVisibility(View.VISIBLE);
            }
            else {
                projectItemViewProjectPriorityChip.setVisibility(View.GONE);
            }

            ArrayList<AssetModel> projectListItemModels = new ArrayList<>();

            ProgressBar progressBar = findViewById(R.id.progressBar3);
            final boolean[] loading = new boolean[] {false, false};

            ProjectListItemsAdapter projectListItemsAdapter = new ProjectListItemsAdapter(this, projectListItemModels);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            projectItemViewAllAssetsRV.setLayoutManager(gridLayoutManager);
            projectItemViewAllAssetsRV.setAdapter(projectListItemsAdapter);

            projectItemViewBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            projectItemViewActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectItemViewActivity.this, ProjectBasedActivityViewActivity.class);
                    intent.putExtra("project_model", projectModel);
                    startActivity(intent);
                }
            });

            projectItemViewUploadAssetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("project_model", projectModel);

                    SendAssetBottomSheet bottomSheet = new SendAssetBottomSheet();
                    bottomSheet.setArguments(bundle);
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
                }
            });

            DBConn.getRequest(DBConn.getRecordURL("project_assets?join=assets,files&join=projects,users&filter=project_id,eq," + String.valueOf(projectModel.getProjectId())), this, new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {}
                @Override
                public void innerResponse(Object object, Context context) {
                    try {
                        if (object instanceof JSONObject) {
                            System.out.println(((JSONObject) object).toString(4));
//                            projectItemViewProjectOwner.setText(((JSONObject) object).getString("full_name"));
                        }
                        else if (object instanceof JSONArray) {
                            for (int i = 0; i < ((JSONArray) object).length(); i++) {
                                try {
                                    JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);

                                    System.out.println(jsonObject.toString(4));

                                    AssetModel model = new AssetModel(
                                            jsonObject.getJSONObject("asset_id").getInt("asset_id"),
                                            jsonObject.getJSONObject("project_id").getInt("project_id"),
                                            jsonObject.getJSONObject("asset_id").getString("asset_title"),
                                            jsonObject.getJSONObject("asset_id").getString("asset_description"),
                                            LocalDateTime.parse(jsonObject.getJSONObject("asset_id").getString("date_created"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                            LocalDateTime.parse(jsonObject.getJSONObject("asset_id").getString("date_modified"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                            jsonObject.getJSONObject("asset_id").getString("latest_revision"),
                                            jsonObject.getJSONObject("asset_id").getJSONObject("latest_revision_file_path").getString("file_id") + "." + jsonObject.getJSONObject("asset_id").getJSONObject("latest_revision_file_path").getString("file_ext")
                                    );
                                    projectListItemModels.add(model);
                                }
                                catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                loading[0] = true;
                                if (loading[0] == true && loading[1] == true) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            Collections.sort(projectListItemModels, new Comparator<AssetModel>() {
                                @Override
                                public int compare(AssetModel lhs, AssetModel rhs) {
                                    return rhs.getDateModified().compareTo(lhs.getDateModified());
                                }
                            });

                            projectItemViewAllAssetsRV.setAdapter(projectListItemsAdapter);

                            if (projectListItemsAdapter.getItemCount() > 0) {
                                AssetModel latestAsset = Collections.max(projectListItemModels, Comparator.comparing(s -> s.getDateCreated()));

                                System.out.println(latestAsset.getAssetTitle());
                                String ext = latestAsset.getLatestRevisionFilePath().trim().toLowerCase();

                                findViewById(R.id.textView26).setVisibility(View.VISIBLE);

                                if (ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".gif")) {
                                    Glide.with(context)
                                        .load(DBConn.getURL("filegator/repository/user/" + latestAsset.getLatestRevisionFilePath()))
                                        .apply(
                                            new RequestOptions()
                                            .placeholder(android.R.drawable.screen_background_dark)
                                            .error(R.drawable.password_cross)
                                        )
                                        .fitCenter()
                                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                                        .into(projectItemViewLatestAsset);

                                    projectItemViewLatestAssetConstraintLayout.setVisibility(View.VISIBLE);
                                    projectItemViewLatestAsset.setVisibility(View.VISIBLE);
                                    projectItemViewLatestAssetText.setVisibility(View.GONE);
                                }
                                else {
                                    projectItemViewLatestAssetConstraintLayout.setVisibility(View.VISIBLE);
                                    projectItemViewLatestAssetText.setVisibility(View.VISIBLE);
                                    projectItemViewLatestAssetText.setText(latestAsset.getAssetTitle());
                                    projectItemViewLatestAsset.setVisibility(View.GONE);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(ProjectItemViewActivity.this, "Unable to fetch project data", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }

                }
            }, "Unable to connect to the database",
                "Unable to parse API response");

            DBConn.getRequest(DBConn.getRecordURL("users/" + String.valueOf(projectModel.getProjectOwnerId())), this, new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {
                    try {
                        System.out.println(object.toString() + " " + object.getClass());
                        if (object instanceof JSONObject) {
                            UserModel userModel = new UserModel(
                                    ((JSONObject) object).getInt("user_id"),
                                    ((JSONObject) object).getString("email"),
                                    ((JSONObject) object).getString("full_name"),
                                    ((JSONObject) object).getInt("role_id"),
                                    (((JSONObject) object).isNull("profile_pic_path") ? null : ((JSONObject) object).getString("profile_pic_path"))
                            );

                            projectItemViewProjectOwner.setText(userModel.getFullName());
                            projectItemViewProjectOwner.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProjectItemViewActivity.this, ProfileViewerActivity.class);
                                    intent.putExtra("user_model", userModel);
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (Exception e) {
                        Toast.makeText(ProjectItemViewActivity.this, "Unable to fetch project data", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                    loading[1] = true;
                    if (loading[0] == true && loading[1] == true) {
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }, "Unable to connect to the database",
                "Unable to parse API response");

            projectItemViewProjectDescription.setText(projectModel.getProjectDescription());
            projectItemViewProjectDateChip.setText(projectModel.getDateCreated().format(DateTimeFormatter.ofPattern("LLL. dd, uuuu")));

            if (projectModel.getProjectImagePath() != null || !projectModel.getProjectImagePath().isEmpty()) {
                Glide.with(this)
                    .load(DBConn.getURL("filegator/repository/user/" + projectModel.getProjectImagePath()))
                    .apply(
                        new RequestOptions().placeholder(android.R.drawable.screen_background_light_transparent)
                    )
                    .fitCenter()
                    .into(projectItemViewProjectPicture);
            }

        }

    }


}