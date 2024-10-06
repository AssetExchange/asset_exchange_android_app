package com.example.assetexdemo1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectOverviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectOverviewFragment newInstance(String param1, String param2) {
        ProjectOverviewFragment fragment = new ProjectOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.getView() != null) {
            ImageButton addProjectTopButton = getView().findViewById(R.id.addProjectTopButton);

            ProgressBar progressBar = getView().findViewById(R.id.progressBar2);

            RecyclerView allProjectsRV = getView().findViewById(R.id.allProjectsRV);
            ArrayList<ProjectModel> projectOverviewModels = new ArrayList<>();

            ProjectOverviewAdapter projectOverviewAdapter = new ProjectOverviewAdapter(getContext(), projectOverviewModels);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            allProjectsRV.setLayoutManager(gridLayoutManager);
            allProjectsRV.setAdapter(projectOverviewAdapter);

            allProjectsRV.addItemDecoration(new GridSpacingItemDecoration(2, 16, false));

            SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

            final boolean[] loading = new boolean[] {false, false};

            DBConn.getRequest(
                DBConn.getRecordURL("projects?filter=project_owner_id,eq," + sharedPref.getString("user_id", "1")),
                getContext(),
                new DBConn.ResponseCallback() {
                    @Override
                    public void innerResponse(Object object) {}
                    @Override
                    public void innerResponse(Object object, Context context) {
                        if (object instanceof JSONArray) {
                            System.out.println(object);

                            if (((JSONArray) object).length() < 1) {
                                loading[0] = true;
                            }

                            List<ProjectModel> dataList = new ArrayList<>();

                            for (int i = 0; i < ((JSONArray) object).length(); i++) {
                                try {
                                    JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);
                                    ProjectModel projectModel = new ProjectModel(
                                        jsonObject.getInt("project_id"),
                                        jsonObject.isNull("date_created") ? null : LocalDateTime.parse(jsonObject.getString("date_created"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                        jsonObject.getInt("project_owner_id"),
                                        jsonObject.getString("project_title"),
                                        jsonObject.getString("project_description"),
                                        jsonObject.isNull("due_date") ? null : LocalDateTime.parse(jsonObject.getString("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss")),
                                        jsonObject.getBoolean("priority"),
                                        jsonObject.getString("project_image_path")
                                    );

                                    projectOverviewModels.add(projectModel);
                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            loading[0] = true;
                            if (loading[0] == true && loading[1] == true) {
                                progressBar.setVisibility(View.GONE);
                            }

                            Collections.sort(projectOverviewModels, new Comparator<ProjectModel>() {
                               @Override
                               public int compare(ProjectModel lhs, ProjectModel rhs) {
                                   int a = Boolean.compare(rhs.isPriority(), lhs.isPriority());
                                   if (a != 0) {
                                       return a;
                                   }
                                   return rhs.getProjectTitle().compareToIgnoreCase(lhs.getProjectTitle());
                               }
                            });
                            allProjectsRV.setAdapter(projectOverviewAdapter);
                         }
                    }
                },
                "Unable to connect to the database",
                "Unable to parse API response"
            );

            DBConn.getRequest(
                DBConn.getRecordURL("project_shares?join=projects&filter=privileges,in,1,2,3&filter=share_user_id,eq," + sharedPref.getString("user_id", "1")),
                getContext(),
                new DBConn.ResponseCallback() {
                    @Override
                    public void innerResponse(Object object) {}
                    @Override
                    public void innerResponse(Object object, Context context) {
                        if (object instanceof JSONArray) {
                            if (((JSONArray) object).length() < 1) {
                                loading[1] = true;
                            }
                            if (loading[0] == true && loading[1] == true) {
                                progressBar.setVisibility(View.GONE);
                            }

                            for (int i = 0; i < ((JSONArray) object).length(); i++) {
                                try {
                                    JSONObject jsonObject = ((JSONArray) object).getJSONObject(i);

                                    ProjectModel projectModel = new ProjectModel(
                                        jsonObject.getJSONObject("project_id").getInt("project_id"),
                                        jsonObject.getJSONObject("project_id").isNull("date_created") ? null : LocalDateTime.parse(jsonObject.getJSONObject("project_id").getString("date_created"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                        jsonObject.getJSONObject("project_id").getInt("project_owner_id"),
                                        jsonObject.getJSONObject("project_id").getString("project_title"),
                                        jsonObject.getJSONObject("project_id").getString("project_description"),
                                        jsonObject.getJSONObject("project_id").isNull("due_date") ? null : LocalDateTime.parse(jsonObject.getJSONObject("project_id").getString("due_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                        jsonObject.getJSONObject("project_id").getBoolean("priority"),
                                        jsonObject.getJSONObject("project_id").getString("project_image_path")
                                    );

                                    projectOverviewModels.add(projectModel);
                                }
                                catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            if (((JSONArray) object).length() >= 1) {
                                Collections.sort(projectOverviewModels, new Comparator<ProjectModel>() {
                                   @Override
                                   public int compare(ProjectModel lhs, ProjectModel rhs) {
                                       int a = Boolean.compare(rhs.isPriority(), lhs.isPriority());
                                       if (a != 0) {
                                           return a;
                                       }
                                       return rhs.getProjectTitle().compareToIgnoreCase(lhs.getProjectTitle());
                                   }
                                });
                            }

                            loading[1] = true;
                            if (loading[0] == true && loading[1] == true) {
                                progressBar.setVisibility(View.GONE);
                            }

                            projectOverviewAdapter.notifyDataSetChanged();
                         }
                    }
                },
                "Unable to connect to the database",
                "Unable to parse API response"
            );

            addProjectTopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddProjectBottomSheet bottomSheet = new AddProjectBottomSheet();
                    bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
                }
            });

            Spinner spinner = getView().findViewById(R.id.spinner);
            String[] items = new String[] {"Name", "Date Created"};
            ArrayAdapter<String> sortByAdapter = new ArrayAdapter<String>(getView().getContext(), android.R.layout.simple_spinner_item, items);

            spinner.setAdapter(sortByAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                        int position, long id) {
                    if (((String) parent.getItemAtPosition(position)).equals("Name")) {
                        Collections.sort(projectOverviewModels, new Comparator<ProjectModel>() {
                            @Override
                            public int compare(ProjectModel lhs, ProjectModel rhs) {
                                int a = Boolean.compare(rhs.isPriority(),lhs.isPriority());
                                if (a != 0) {
                                    return a;
                                }
                                return rhs.getProjectTitle().compareToIgnoreCase(lhs.getProjectTitle());
                            }
                        });
                    }
                    else if (((String) parent.getItemAtPosition(position)).equals("Date Created")) {
                        Collections.sort(projectOverviewModels, new Comparator<ProjectModel>() {
                            @Override
                            public int compare(ProjectModel lhs, ProjectModel rhs) {
                                int a = Boolean.compare(rhs.isPriority(), lhs.isPriority());
                                if (a != 0) {
                                    return a;
                                }
                                return rhs.getDateCreated().compareTo(lhs.getDateCreated());
                            }
                        });
                    }
                    projectOverviewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}