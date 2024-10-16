package com.example.assetexdemo1.bottomsheet;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.assetexdemo1.DBConn;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.activity.SelectProjectFromAssetActivity;
import com.example.assetexdemo1.ToastMessage;
import com.example.assetexdemo1.VolleyMultipartRequest;
import com.example.assetexdemo1.model.ProjectModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SendAssetBottomSheet extends BottomSheetDialogFragment {
    Button buttonAddNewAsset;
    EditText addAssetInputTitle, addAssetInputDescription;
    Button buttonBackSendIt;
    ImageButton buttonAddAsset;
    ImageView imageView6;
    TextView textView22, addAssetAddedProjectsSelector;
    ProgressBar progressBar;

    ProjectModel selectedProjectModel;

    private final ActivityResultLauncher<String> filePickerLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    fileUri = uri;

                    if (selectedProjectModel != null) {
                        buttonAddNewAsset.setTextColor(Color.parseColor("#0886F6"));
                    }

                    String fileName = getFileName(uri);

                    if (fileName.lastIndexOf(".") > 0) {
                        String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                        fileName = fileName.substring(0, fileName.lastIndexOf("."));

                        if (
                            Arrays.stream(new String[] {".png", ".jpg", ".jpeg", ".gif"})
                                .anyMatch(getFileName(uri)::contains)
                        ) {
                            System.out.println("a");
                            Glide.with(SendAssetBottomSheet.this).clear(buttonAddAsset);

                            Glide.with(SendAssetBottomSheet.this)
                                .load(fileUri)
                                .fitCenter()
                                .into(buttonAddAsset);

                            buttonAddAsset.setPadding(0,0,0,0);

                            textView22.setVisibility(View.GONE);
                            imageView6.setVisibility(View.GONE);
                        }
                        else {
                            Glide.with(SendAssetBottomSheet.this).clear(buttonAddAsset);

                            textView22.setVisibility(View.VISIBLE);
                            textView22.setText(getFileName(uri));
                            textView22.setTextColor(Color.parseColor("#71BAFB"));

                            imageView6.setVisibility(View.GONE);
                            buttonAddAsset.setPadding(0,0,0,0);
                        }
                    }

                    addAssetInputTitle.setText(fileName);
                }
            }
        });

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedProjectModel = getArguments().getParcelable("project_model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.send_asset_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (this.getActivity() != null) {
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the btn_back_sendIt in the layout
        buttonBackSendIt = view.findViewById(R.id.buttonBackNewAsset);
        buttonAddAsset = view.findViewById(R.id.buttonAddAsset);
        buttonAddNewAsset = view.findViewById(R.id.buttonAddNewAsset);
        addAssetInputTitle = view.findViewById(R.id.addAssetInputTitle);
        addAssetInputDescription = view.findViewById(R.id.addAssetInputDescription);
        imageView6 = view.findViewById(R.id.imageView6);
        textView22 = view.findViewById(R.id.textView22);
        progressBar = view.findViewById(R.id.progressBar9);

        CardView newAssetAddedProjectsContainer = view.findViewById(R.id.newAssetAddedProjectsContainer);
        addAssetAddedProjectsSelector = view.findViewById(R.id.addAssetAddedProjectsSelector);

        if (selectedProjectModel != null) {
            newAssetAddedProjectsContainer.setVisibility(View.GONE);
        }

        newAssetAddedProjectsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectProjectFromAssetActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        NestedScrollView scrollView = view.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(view);
                bottomSheetBehavior.setDraggable(scrollY == 0);
            }
        });

        // Set click listener on the back button to close the bottom sheet
        buttonBackSendIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the bottom sheet
                dismiss();
            }
        });

        buttonAddAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePickerLauncher.launch("*/*");
            }
        });

        buttonAddNewAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri != null) {
                    String fileName = getFileName(fileUri);
                    System.out.printf(fileName);

                    if (selectedProjectModel != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        uploadFile(fileUri);
                    }
                    else {
                        ToastMessage.getInstance(getContext()).showShortMessage("No project selected", "frown");
                        // Toast.makeText(getContext(), "No project selected", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    // Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
                    ToastMessage.getInstance(getContext()).showShortMessage("No file selected", "frown");
                }
            }
        });
    }

    private String getFileName(Uri uri) {
        String fileName = null;

        if (this.getActivity() != null) {
            if (uri != null) {
                if (uri.getScheme() != null && uri.getScheme().equals("content")) {
                    try (Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null)) {
                         if (cursor != null && cursor.moveToFirst()) {
                             fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                         }
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    fileName = uri.getLastPathSegment();
                }
            }
        }
        return fileName != null ? fileName : "file";
    }

    private byte[] getFileData(Uri uri) {
        try (
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream()
        ) {
            int bytesRead;
            // Set buffer size to 1 MB
            byte[] data = new byte[1024 * 1024]; // 1 MB buffer
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            while ((bytesRead = bufferedInputStream.read(data, 0, data.length)) != -1) {
                byteBuffer.write(data, 0, bytesRead);
            }

            return byteBuffer.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadFile(Uri uri) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
            Request.Method.POST,
            DBConn.getURL("upload_asset.php"),
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        // System.out.println(response.headers.toString());
                        String responseData = new String(response.data);
                        System.out.println(responseData);
                        // System.out.println(response.statusCode);

                        JSONObject obj = new JSONObject(responseData);

                        // Toast.makeText(getContext(), obj.getString("code"), Toast.LENGTH_SHORT).show();

                        if (obj.getString("code").equals("The asset has been added to the project.")) {
                            ToastMessage.getInstance(getContext()).showShortMessage(obj.getString("code"), "smile");
                            dismiss();
                        }
                        else {
                            ToastMessage.getInstance(getContext()).showShortMessage(obj.getString("code"), "frown");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error != null && error.getMessage() != null) {
                        // Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        ToastMessage.getInstance(getContext()).showShortMessage("Unable to upload asset", "frown");
                    }
                    else {
                        // Toast.makeText(getContext(), "The asset has been added", Toast.LENGTH_SHORT).show();
                        ToastMessage.getInstance(getContext()).showShortMessage("The asset has been added", "smile");
                        Log.e("Got Error", "" + error.getMessage() + error.networkResponse );
                    }
                }
            }
        ) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                params.put("file", new DataPart(getFileName(uri), getFileData(uri)));
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                 HashMap<String, String> params = new HashMap<>();

                 params.put("asset_title", addAssetInputTitle.getText().toString());
                 params.put("asset_description", addAssetInputDescription.getText().toString());
                 params.put("project_id", String.valueOf(selectedProjectModel.getProjectId()));

                 return params;
            }
        };

        if (this.getContext() != null) {
            //adding the request to volley
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 10 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                selectedProjectModel = (ProjectModel) data.getParcelableExtra("selected_project");

                addAssetAddedProjectsSelector.setText(selectedProjectModel.getProjectTitle());
                addAssetAddedProjectsSelector.setTypeface(null, Typeface.BOLD);
                addAssetAddedProjectsSelector.setTextColor(Color.parseColor("#6A6A6A"));

                if (fileUri != null) {
                    buttonAddNewAsset.setTextColor(Color.parseColor("#0886F6"));
                }
            }
        }
        catch (Exception e) {
            ToastMessage.getInstance(getContext()).showShortMessage(e.toString(), "frown");
            // Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
