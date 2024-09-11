package com.example.assetexdemo1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment1 newInstance(String param1, String param2) {
        BlankFragment1 fragment = new BlankFragment1();
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
        return inflater.inflate(R.layout.fragment_blank1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView loadsRV =  getView().findViewById(R.id.loadsRecyclerView);

        ArrayList<LoadsModel> loadsModels = new ArrayList<LoadsModel>();

        loadsModels.add(new LoadsModel("Logo Refinement", "Bytecraft Innovations", "Awaiting Approval", LocalDateTime.now(), LocalDateTime.now(), R.drawable.mock_image_loads));
        loadsModels.add(new LoadsModel("Mock up UI UX", "Bytecraft Innovations", "Change Request", LocalDateTime.now(), LocalDateTime.now()));
        loadsModels.add(new LoadsModel("Card Design", "Bytecraft Innovations", "Change Request", LocalDateTime.now(), LocalDateTime.now()));
        loadsModels.add(new LoadsModel("Card Deasdasdsign", "Bytecraft Innovations", "Change Request", LocalDateTime.now(), LocalDateTime.now()));


        LoadsAdapter loadsAdapter = new LoadsAdapter(this.getContext(), loadsModels);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        loadsRV.setLayoutManager(linearLayoutManager);

        loadsRV.setAdapter(loadsAdapter);

        DBHelper.fetchData(this.getContext(), new DBHelper.OnDataReceivedListener() {
            @Override
            public void onDataReceived(Context context, List<LoadsModel> dataList) {
                LoadsAdapter adapterDB = new LoadsAdapter(context, (ArrayList<LoadsModel>) dataList);
                loadsRV.setAdapter(adapterDB);
            }
        });
    }
}