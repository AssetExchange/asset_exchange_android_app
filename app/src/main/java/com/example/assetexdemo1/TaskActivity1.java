package com.example.assetexdemo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskActivity1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskActivity1 extends Fragment {

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
        return inflater.inflate(R.layout.fragment_task_activity1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView completedTaskCard = getActivity().findViewById(R.id.taskCompleted);
        CardView allTaskCard = getActivity().findViewById(R.id.taskAll);

        TextView counterTaskAll = getActivity().findViewById(R.id.counterTaskAll);
        TextView counterTaskCompleted = getActivity().findViewById(R.id.counterTaskCompleted);
        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);

        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

        final boolean[] loading = new boolean[] {false, false};

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
                        if (loading[0] == true && loading[1] == true) {
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
                        if (loading[0] == true && loading[1] == true) {
                            progressBar.setVisibility(View.GONE);
                        }
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
}