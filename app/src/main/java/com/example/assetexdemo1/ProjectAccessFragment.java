package com.example.assetexdemo1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectAccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectAccessFragment extends Fragment {
    private ProjectModel projectModel;

    public ProjectAccessFragment() {
        // Required empty public constructor
    }

    public ProjectAccessFragment(ProjectModel projectModel) {
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
        return inflater.inflate(R.layout.fragment_project_access, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (projectModel != null) {
            RecyclerView projectAccessRV = view.findViewById(R.id.projectAccessRV);

            ArrayList<ProjectAccessModel> projectAccessModels = new ArrayList<>();
            ProjectAccessActivityAdapter projectAccessAdapter = new ProjectAccessActivityAdapter(getContext(), projectAccessModels);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            projectAccessRV.setLayoutManager(linearLayoutManager);
            projectAccessRV.setAdapter(projectAccessAdapter);

            DBConn.getRequest(DBConn.getRecordURL("project_shares?join=users&filter=project_id,eq," + String.valueOf(projectModel.getProjectId())), getContext(), new DBConn.ResponseCallback() {
                        @Override
                        public void innerResponse(Object object) {
                        }

                        @Override
                        public void innerResponse(Object object, Context context) {
                            try {
                                System.out.println(object.toString() + "a");
                                if (object instanceof JSONObject) {
                                    System.out.println(((JSONObject) object).toString(4));
                                } else if (object instanceof JSONArray) {
                                    System.out.println(object.toString() + "b");
                                    for (int i = 0; i < ((JSONArray) object).length(); i++) {
                                        try {
                                            JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);

                                            System.out.println(jsonObject.toString(4));

                                            UserModel projectOwner = new UserModel(
                                                    jsonObject.getJSONObject("project_owner_id").getInt("user_id"),
                                                    jsonObject.getJSONObject("project_owner_id").getString("email"),
                                                    jsonObject.getJSONObject("project_owner_id").getString("full_name"),
                                                    jsonObject.getJSONObject("project_owner_id").getInt("role_id"),
                                                    jsonObject.getJSONObject("project_owner_id").getString("profile_pic_path")
                                            );

                                            UserModel shareUser = new UserModel(
                                                    jsonObject.getJSONObject("share_user_id").getInt("user_id"),
                                                    jsonObject.getJSONObject("share_user_id").getString("email"),
                                                    jsonObject.getJSONObject("share_user_id").getString("full_name"),
                                                    jsonObject.getJSONObject("share_user_id").getInt("role_id"),
                                                    jsonObject.getJSONObject("share_user_id").getString("profile_pic_path")
                                            );

                                            ProjectAccessModel model = new ProjectAccessModel(
                                                    projectModel,
                                                    projectOwner,
                                                    shareUser,
                                                    jsonObject.getInt("privileges")
                                            );

                                            projectAccessModels.add(model);
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }

                                    projectAccessRV.setAdapter(projectAccessAdapter);
                                }
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Unable to fetch project share data", Toast.LENGTH_SHORT).show();
                                throw new RuntimeException(e);
                            }

                        }
                    }, "Unable to connect to the database",
                    "Unable to parse API response");

        }

    }
}