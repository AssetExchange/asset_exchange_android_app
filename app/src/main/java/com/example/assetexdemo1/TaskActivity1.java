package com.example.assetexdemo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskActivity1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskActivity1 extends Fragment implements AddReminderBottomSheet.BottomSheetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskActivity1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskActivity1.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskActivity1 newInstance(String param1, String param2) {
        TaskActivity1 fragment = new TaskActivity1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Button btnRemind;
    ArrayList<TasksModel> priorityTasksModels;
    AllTasksAdapter priorityTasksAdapter;
    RecyclerView priorityTasksRV;
    SharedPreferences sharedPref;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

         sharedPref = AssetExchangeApp.context.getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_activity1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView completedTaskCard = getView().findViewById(R.id.taskCompleted);
        CardView allTaskCard = getView().findViewById(R.id.taskAll);

        btnRemind = getView().findViewById(R.id.btnRemind);
        btnRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar snackbar = Snackbar.make(getContext(), getActivity().getWindow().getDecorView(), "test", BaseTransientBottomBar.LENGTH_SHORT);
//                snackbar.setBackgroundTint(getResources().getColor(R.color.colorOnboardingPasswordHintGreen));
//                snackbar.setTextColor(getResources().getColor(R.color.black));
//
//                TextView a = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
//                a.setTypeface(null, Typeface.BOLD);
//                a.setCompoundDrawablesWithIntrinsicBounds(R.drawable.logo, 0, 0, 0);
//
//
//                snackbar.getView().bringToFront();
//                snackbar.show();

                ToastMessage a = ToastMessage.getInstance(getContext());
                a.showLongMessage("aaaa", "frown");

                AddReminderBottomSheet bottomSheet = new AddReminderBottomSheet();
                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
        }});

        priorityTasksModels = new ArrayList<>();
        priorityTasksRV = getView().findViewById(R.id.allPriorityTasksRecyclerView);
        priorityTasksAdapter = new AllTasksAdapter(getContext(), priorityTasksModels);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        priorityTasksRV.setLayoutManager(linearLayoutManager);
        priorityTasksRV.setAdapter(priorityTasksAdapter);

        TextView counterTaskAll = getActivity().findViewById(R.id.counterTaskAll);
        TextView counterTaskCompleted = getActivity().findViewById(R.id.counterTaskCompleted);
        progressBar = getActivity().findViewById(R.id.progressBar);

        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

        final boolean[] loading = new boolean[] {false, false, false};

        DBConn.getRequest(
            DBConn.getRecordURL("tasks?filter=task_owner_id,eq," + sharedPref.getString("user_id", "1") + "&filter=date_completed,is,null"),
            getContext(),
            new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {
                    System.out.println(object);
                    if (object instanceof JSONArray) {
                        counterTaskAll.setText(String.valueOf(((JSONArray) object).length()));
                        loading[0] = true;
                        if (loading[0] == true && loading[1] == true && loading[2] == true) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            },
                "Unable to connect to the database",
                "Unable to parse API response"
        );

        DBConn.getRequest(
            DBConn.getRecordURL("tasks?filter=task_owner_id,eq," + sharedPref.getString("user_id", "1") + "&filter=date_completed,nis,null"),
            getContext(),
            new DBConn.ResponseCallback() {
                @Override
                public void innerResponse(Object object) {
                    System.out.println(object);
                    if (object instanceof JSONArray) {
                        counterTaskCompleted.setText(String.valueOf(((JSONArray) object).length()));
                        loading[1] = true;
                        if (loading[0] == true && loading[1] == true && loading[2] == true) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            },
                "Unable to connect to the database",
                "Unable to parse API response"
        );

        DBConn.getRequest(
            DBConn.getRecordURL("tasks?order=due_date,asc&size=5&filter=priority,eq,1&filter=date_completed,is,null&filter=due_date,nis,null&filter=task_owner_id,eq," + sharedPref.getString("user_id", "1")),
            getContext(),
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

                                 if (!tasksModel.isCompleted() && tasksModel.getDueDate() != null && tasksModel.isPriority()) {
                                     priorityTasksModels.add(tasksModel);
                                 }
                             }
                             catch (JSONException e) {
                                 throw new RuntimeException(e);
                             }
                         }

                         loading[2] = true;
                         if (loading[0] == true && loading[1] == true && loading[2] == true) {
                             progressBar.setVisibility(View.GONE);
                         }
                         priorityTasksRV.setAdapter(priorityTasksAdapter);
                     }
                }
            },
                "Unable to connect to the database",
                "Unable to parse API response"
        );


        completedTaskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CompletedTasks.class);
                startActivity(intent);
            }
        });
        allTaskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllTasks.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onUpdateData() {
        progressBar.setVisibility(View.VISIBLE);


        DBConn.getRequest(
            DBConn.getRecordURL("tasks?order=due_date,asc&size=5&filter=priority,eq,1&filter=date_completed,is,null&filter=due_date,nis,null&filter=task_owner_id,eq," + sharedPref.getString("user_id", "1")),
            getContext(),
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

                         priorityTasksModels.clear();
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

                                 if (!tasksModel.isCompleted() && tasksModel.getDueDate() != null && tasksModel.isPriority()) {
                                     priorityTasksModels.add(tasksModel);
                                 }
                             }
                             catch (JSONException e) {
                                 throw new RuntimeException(e);
                             }
                         }

                         progressBar.setVisibility(View.GONE);
                         priorityTasksAdapter.notifyDataSetChanged();
                     }
                }
            },
                "Unable to connect to the database",
                "Unable to parse API response"
        );
    }
}