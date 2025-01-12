package com.example.assetexdemo1.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.assetexdemo1.R;
import com.example.assetexdemo1.bottomsheet.SendAssetBottomSheet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadAssetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadAssetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadAssetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadAssetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadAssetFragment newInstance(String param1, String param2) {
        UploadAssetFragment fragment = new UploadAssetFragment();
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
        return inflater.inflate(R.layout.fragment_upload_asset, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Button showBottomSheetButton = getView().findViewById(R.id.showBottomSheetButton);

        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAssetBottomSheet bottomSheet = new SendAssetBottomSheet();
                bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
            }
        });
    }
}