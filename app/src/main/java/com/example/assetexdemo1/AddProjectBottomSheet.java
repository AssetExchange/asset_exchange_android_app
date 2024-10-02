package com.example.assetexdemo1;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddProjectBottomSheet extends BottomSheetDialogFragment {
    Button buttonBackNewProject;
    ImageButton buttonAddProject;
    Button buttonAddNewProject;
    EditText newProjectInputTitle;
    EditText newProjectInputDescription;
    ImageView imageView6;
    TextView textView22;
    EditText newProjectShareWithEditText;
    Switch switch1;

    private final ActivityResultLauncher<String> filePickerLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    fileUri = uri;

                    String fileName = getFileName(uri);

                    if (fileName.lastIndexOf(".") > 0) {
                        String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                        fileName = fileName.substring(0, fileName.lastIndexOf("."));

                        if (
                            Arrays.stream(new String[] {".png", ".jpg", ".jpeg", ".gif"})
                                .anyMatch(getFileName(uri)::contains)
                        ) {
                            System.out.println("a");
                            Glide.with(AddProjectBottomSheet.this).clear(buttonAddProject);

                            Glide.with(AddProjectBottomSheet.this)
                                .load(fileUri)
                                .fitCenter()
                                .into(buttonAddProject);

                            textView22.setVisibility(View.GONE);
                            imageView6.setVisibility(View.GONE);
                        }
                    }

                    newProjectInputTitle.setText(fileName);
                }
            }
        });

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_project_bottom_sheet, container, false);
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

        buttonBackNewProject = view.findViewById(R.id.buttonBackNewProject);
        buttonAddProject = view.findViewById(R.id.buttonAddProject);
        buttonAddNewProject = view.findViewById(R.id.buttonAddNewProject);
        newProjectInputTitle = view.findViewById(R.id.newProjectInputTitle);
        imageView6 = view.findViewById(R.id.imageView6);
        textView22 = view.findViewById(R.id.textView22);
        newProjectShareWithEditText = view.findViewById(R.id.newProjectShareWithEditText);
        newProjectInputDescription = view.findViewById(R.id.newProjectInputDescription);
        switch1 = view.findViewById(R.id.switch1);

        NestedScrollView scrollView = view.findViewById(R.id.scrollViewNewProject);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(v);
                bottomSheetBehavior.setDraggable(scrollY == 0);
            }
        });

        // Set click listener on the back button to close the bottom sheet
        buttonBackNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the bottom sheet
                dismiss();
            }
        });

        buttonAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filePickerLauncher.launch("image/*");
            }
        });

        buttonAddNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = getFileName(fileUri);
                System.out.printf(fileName);

                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                email = newProjectShareWithEditText.getText().toString().trim();

                if (email.isEmpty() || CustomInputValidation.validateEmail(email)) {
                    uploadFile(fileUri);
                }
                else {
                    Toast.makeText(getContext(), "Invalid share email", Toast.LENGTH_SHORT).show();
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
        if (uri != null && fileUri != null) {
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                DBConn.getURL("add_project.php"),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            // System.out.println(response.headers.toString());
                            String responseData = new String(response.data);
                            System.out.println(responseData);
                            // System.out.println(response.statusCode);

                            JSONObject obj = new JSONObject(responseData);

                            Toast.makeText(getContext(), obj.getString("code"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Got Error", "" + error.getMessage());
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

                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

                    params.put("project_owner_id", sharedPref.getString("user_id", null));
                    params.put("project_title", newProjectInputTitle.getText().toString().trim());
                    params.put("project_description", newProjectInputDescription.getText().toString());
                    params.put("priority", (switch1.isChecked() ? "true" : "false"));
                    params.put("share_with", email);

                    return params;
                }
            };

            if (this.getContext() != null) {
                //adding the request to volley
                Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
            }
        }
        else {
            HashMap<String, String> params = new HashMap<>();

            SharedPreferences sharedPref = this.getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

            params.put("no_image", "true");
            params.put("project_owner_id", sharedPref.getString("user_id", null));
            params.put("project_title", newProjectInputTitle.getText().toString().trim());
            params.put("project_description", newProjectInputDescription.getText().toString());
            params.put("priority", (switch1.isChecked() ? "true" : "false"));
            params.put("share_with", email);

            DBConn.postRequest(
                DBConn.getURL("add_project.php"),
                getContext(),
                params,
                new DBConn.ResponseCallback() {
                    @Override
                    public void innerResponse(Object object) {
                        Toast.makeText(getContext(), object.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                "Unable to connect to the database",
                "Unable to parse API response"
            );
        }
    }
}

