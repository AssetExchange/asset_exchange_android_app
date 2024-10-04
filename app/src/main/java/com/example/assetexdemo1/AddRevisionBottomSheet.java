package com.example.assetexdemo1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.assetexdemo1.databinding.AddReminderBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class AddRevisionBottomSheet extends BottomSheetDialogFragment {
    Button buttonBackNewRevision, buttonAddNewRevision;
    EditText newRevisionInputDescription;
    ImageButton buttonAddRevision;
    TextView textView22;
    ImageView imageView6;

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;

    AssetModel assetModel;

//    private final ActivityResultLauncher<String> filePickerLauncher =
//        registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri uri) {
//                if (uri != null) {
//                    fileUri = uri;
//
//                    if (assetModel != null) {
//                        buttonAddNewRevision.setTextColor(Color.parseColor("#0886F6"));
//                    }
//
//                    String fileName = getFileName(uri);
//
//                    if (fileName.lastIndexOf(".") > 0) {
//                        String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
//                        fileName = fileName.substring(0, fileName.lastIndexOf("."));
//
//                        if (
//                            Arrays.stream(new String[] {".png", ".jpg", ".jpeg", ".gif"})
//                                .anyMatch(getFileName(uri)::contains)
//                        ) {
//                            System.out.println("a");
//                            Glide.with(AddRevisionBottomSheet.this).clear(buttonAddRevision);
//
//                            Glide.with(AddRevisionBottomSheet.this)
//                                .load(fileUri)
//                                .fitCenter()
//                                .into(buttonAddRevision);
//
//                            textView22.setVisibility(View.GONE);
//                            imageView6.setVisibility(View.GONE);
//                        }
//                        else {
//                            Glide.with(AddRevisionBottomSheet.this).clear(buttonAddRevision);
//
//                            textView22.setVisibility(View.VISIBLE);
//                            textView22.setText(getFileName(uri));
//                            textView22.setTextColor(Color.parseColor("#71BAFB"));
//
//                            imageView6.setVisibility(View.GONE);
//                            buttonAddRevision.setPadding(0,0,0,0);
//                        }
//                    }
//
//                    newRevisionInputDescription.setText(fileName);
//                }
//            }
//        });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            assetModel = getArguments().getParcelable("asset_model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_revision_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.getActivity() != null) {
            getActivity().overridePendingTransition(0, 0);
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonBackNewRevision = view.findViewById(R.id.buttonBackNewRevision);
        buttonAddNewRevision = view.findViewById(R.id.buttonAddNewRevision);
        newRevisionInputDescription = view.findViewById(R.id.newRevisionInputDescription);

        buttonBackNewRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        newRevisionInputDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (fileUri != null && !s.toString().trim().isEmpty()) {
                    buttonAddNewRevision.setTextColor(Color.parseColor("#0886F6"));
                }
                else {
                    buttonAddNewRevision.setTextColor(Color.parseColor("#6A6A6A"));
                }
            }
        });

//        buttonAddNewRevision.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (newRevisionInputDescription.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(getContext(), "Missing revision title", Toast.LENGTH_SHORT).show();
//                }
//                else if (assetModel != null) {
//                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);
//
//                    HashMap<String, String> params = new HashMap<>();
//
//                    params.put("action", "add_revision");
//                    params.put("asset_revision_id", String.valueOf(assetModel.getAssetId()));
//                    params.put("previous_revision_id", assetModel.getLatestRevision());
//                    params.put("comment", newRevisionInputDescription.getText().toString());
//
//                    DBConn.postRequest(
//                        DBConn.getURL("add_revision.php"),
//                        getContext(),
//                        params,
//                        new DBConn.ResponseCallback() {
//                            @Override
//                            public void innerResponse(Object object) {
//                                Toast.makeText(getContext(), object.toString(), Toast.LENGTH_LONG).show();
//                                if (object.toString().equals("Revision added successfully.")) {
//                                    // updateData();
//                                }
//                                else {
//                                    System.out.println(object.toString());
//                                }
//                            }
//                        },
//                        "Unable to connect to the database",
//                        "Unable to parse API response"
//                    );
//                }
//            }
//        });
    }
}
