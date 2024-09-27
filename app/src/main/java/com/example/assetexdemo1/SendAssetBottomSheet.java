package com.example.assetexdemo1;

import android.database.Cursor;
import android.graphics.Color;
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

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
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
    EditText addAssetInputTitle;
    Button buttonBackSendIt;
    Button buttonAddAsset;
    ImageView imageView6;

    private final ActivityResultLauncher<String> filePickerLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                if (uri != null) {
                    fileUri = uri;

                    buttonAddNewAsset.setTextColor(Color.parseColor("#0886F6"));

                    String fileName = getFileName(uri);

                    if (fileName.lastIndexOf(".") > 0) {
                        String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                        fileName = fileName.substring(0, fileName.lastIndexOf("."));


                        if (
                            Arrays.stream(new String[] {".png", ".jpg", ".jpeg", ".gif"})
                                .anyMatch(getFileName(uri)::contains)
                        ) {
                            //
                        }
                        else {
                            buttonAddAsset.setText(getFileName(uri));
                            buttonAddAsset.setTextColor(Color.parseColor("#71BAFB"));
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
        imageView6 = view.findViewById(R.id.imageView6);

        ScrollView scrollView = view.findViewById(R.id.scrollView);
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

                    uploadFile(fileUri);
                }
                else {
                    Toast.makeText(getContext(), "No file selected!", Toast.LENGTH_SHORT).show();
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

                        System.out.println(response.headers.toString());
                        String responseData = new String(response.data);

                        System.out.println(responseData);
                        System.out.println(response.statusCode);

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
        };

        if (this.getContext() != null) {
            //adding the request to volley
            Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
        }
    }
}
