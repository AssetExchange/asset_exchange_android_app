package com.example.assetexdemo1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProjectRevisionsFragment extends Fragment {

   private ProjectModel projectModel;

    public ProjectRevisionsFragment() {
        // Required empty public constructor
    }

    public ProjectRevisionsFragment(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public static ProjectAccessFragment newInstance(ProjectModel projectModel) {
        ProjectAccessFragment fragment = new ProjectAccessFragment();
        Bundle args = new Bundle();
        args.putParcelable("project_model", projectModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectModel = getArguments().getParcelable("project_model");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_revisions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (projectModel != null) {
            ProgressBar progressBar = view.findViewById(R.id.progressBar14);
            RecyclerView projectRevisionsRV = view.findViewById(R.id.projectRevisionsRV);

            ArrayList<RevisionCommentsModel> revisionCommentsModels = new ArrayList<>();
            ProjectRevisionsCommentsAdapter projectRevisionsAdapter = new ProjectRevisionsCommentsAdapter(getContext(), revisionCommentsModels);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            projectRevisionsRV.setLayoutManager(linearLayoutManager);
            projectRevisionsRV.setAdapter(projectRevisionsAdapter);

            progressBar.setVisibility(View.VISIBLE);

            DBConn.getRequest(DBConn.getRecordURL("project_assets?filter=project_id,eq," + String.valueOf(projectModel.getProjectId())), getContext(), new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {
                    if (object instanceof JSONArray) {
                        progressBar.setVisibility(View.GONE);
                        if (((JSONArray) object).length() > 0) {
                            ArrayList<String> assetIds = new ArrayList<>();

                            for (int i = 0; i < ((JSONArray) object).length(); i++) {
                                try {
                                    JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);
                                    String assetId = String.valueOf(jsonObject.getInt("asset_id"));
                                    assetIds.add(assetId);
                                }
                                catch (JSONException e) {
                                    Toast.makeText(getContext(), "Unable to retrieve comments", Toast.LENGTH_LONG).show();
                                }
                            }

                            DBConn.getRequest(DBConn.getRecordURL("revision_comments?join=users&join=assets,files&filter=asset_id,in," + String.join(",", assetIds)), getContext(),
                                new DBConn.ResponseCallback() {
                                    @Override
                                    public void innerResponse(Object object) {
                                        if (object instanceof JSONArray) {
                                            for (int i = 0; i < ((JSONArray) object).length(); i++) {
                                                try {
                                                    JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);

                                                    AssetModel assetModel = new AssetModel(
                                                        jsonObject.getJSONObject("asset_id").getInt("asset_id"),
                                                        projectModel.getProjectId(),
                                                        jsonObject.getJSONObject("asset_id").getString("asset_title"),
                                                        jsonObject.getJSONObject("asset_id").getString("asset_description"),
                                                        LocalDateTime.parse(jsonObject.getJSONObject("asset_id").getString("date_created"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                                        LocalDateTime.parse(jsonObject.getJSONObject("asset_id").getString("date_modified"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                                        jsonObject.getJSONObject("asset_id").getString("latest_revision"),
                                                        jsonObject.getJSONObject("asset_id").getJSONObject("latest_revision_file_path").getString("file_id") + "." + jsonObject.getJSONObject("asset_id").getJSONObject("latest_revision_file_path").getString("file_ext")
                                                    );

                                                    UserModel shareUserModel = new UserModel(
                                                        jsonObject.getJSONObject("revision_comment_user_id").getInt("user_id"),
                                                        jsonObject.getJSONObject("revision_comment_user_id").getString("email"),
                                                        jsonObject.getJSONObject("revision_comment_user_id").getString("full_name"),
                                                        jsonObject.getJSONObject("revision_comment_user_id").getInt("role_id"),
                                                        jsonObject.getJSONObject("revision_comment_user_id").getString("profile_pic_path")
                                                    );

                                                    RevisionCommentsModel revisionCommentsModel = new RevisionCommentsModel(
                                                        jsonObject.getInt("revision_comment_id"),
                                                        assetModel,
                                                        shareUserModel,
                                                        jsonObject.getString("revision_comment_text"),
                                                        LocalDateTime.parse(jsonObject.getString("date_created"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                                        jsonObject.isNull("date_processed") ? null : LocalDateTime.parse(jsonObject.getString("date_processed"), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                                                        jsonObject.getInt("comment_status")
                                                    );

                                                    revisionCommentsModels.add(revisionCommentsModel);
                                                }
                                                catch (Exception e) {
                                                    System.out.println(e.getMessage());
                                                    Toast.makeText(getContext(), "Unable to retrieve comments", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            projectRevisionsAdapter.notifyDataSetChanged();
                                        }
                                    }
                                },
                                "Unable to connect to the database",
                                "Unable to parse API response"
                            );
                            System.out.println();
                        }
                    }
                    System.out.println(object);
                }
            },
            "Unable to connect to the database",
            "Unable to parse API response");

//            DBConn.getRequest(DBConn.getRecordURL("project_shares?join=users&filter=project_id,eq," + String.valueOf(projectModel.getProjectId())), getContext(), new DBConn.ResponseCallback() {
//                        @Override
//                        public void innerResponse(Object object) {
//                        }
//
//                        @Override
//                        public void innerResponse(Object object, Context context) {
//                            progressBar.setVisibility(View.GONE);
//                            try {
//                                System.out.println(object.toString() + "a");
//                                if (object instanceof JSONObject) {
//                                    System.out.println(((JSONObject) object).toString(4));
//                                } else if (object instanceof JSONArray) {
//                                    System.out.println(object.toString() + "b");
//                                    for (int i = 0; i < ((JSONArray) object).length(); i++) {
//                                        try {
//                                            JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);
//
//                                            System.out.println(jsonObject.toString(4));
//
//                                            UserModel projectOwner = new UserModel(
//                                                    jsonObject.getJSONObject("project_owner_id").getInt("user_id"),
//                                                    jsonObject.getJSONObject("project_owner_id").getString("email"),
//                                                    jsonObject.getJSONObject("project_owner_id").getString("full_name"),
//                                                    jsonObject.getJSONObject("project_owner_id").getInt("role_id"),
//                                                    jsonObject.getJSONObject("project_owner_id").getString("profile_pic_path")
//                                            );
//
//                                            UserModel shareUser = new UserModel(
//                                                    jsonObject.getJSONObject("share_user_id").getInt("user_id"),
//                                                    jsonObject.getJSONObject("share_user_id").getString("email"),
//                                                    jsonObject.getJSONObject("share_user_id").getString("full_name"),
//                                                    jsonObject.getJSONObject("share_user_id").getInt("role_id"),
//                                                    jsonObject.getJSONObject("share_user_id").getString("profile_pic_path")
//                                            );
//
//                                            ProjectAccessModel model = new ProjectAccessModel(
//                                                    projectModel,
//                                                    projectOwner,
//                                                    shareUser,
//                                                    jsonObject.getInt("privileges")
//                                            );
//
//                                            projectAccessModels.add(model);
//                                        } catch (Exception e) {
//                                            System.out.println(e.getMessage());
//                                        }
//                                    }
//
//                                    projectAccessRV.setAdapter(projectAccessAdapter);
//                                }
//                            } catch (Exception e) {
//                                Toast.makeText(getContext(), "Unable to fetch project share data", Toast.LENGTH_SHORT).show();
//                                throw new RuntimeException(e);
//                            }
//
//                        }
//                    }, "Unable to connect to the database",
//                    "Unable to parse API response");

        }

    }
}