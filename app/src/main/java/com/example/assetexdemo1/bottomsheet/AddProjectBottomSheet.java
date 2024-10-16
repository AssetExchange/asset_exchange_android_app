package com.example.assetexdemo1.bottomsheet;

import static com.example.assetexdemo1.MiscUtils.hideKeyboard;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.assetexdemo1.AssetExchangeApp;
import com.example.assetexdemo1.CustomInputValidation;
import com.example.assetexdemo1.DBConn;
import com.example.assetexdemo1.NotificationReceiver;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.ToastMessage;
import com.example.assetexdemo1.VolleyMultipartRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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
    ProgressBar progressBar;
    NestedScrollView scrollViewNewProject;
    CardView newProjectDueDateCardView;
    String dateTimeSet;
    TextView newProjectDueDateView;

    int oldHeight = 0;
    int keyboardHeight = 0;
    boolean isVisible = false;

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
                                .centerCrop()
                                .into(buttonAddProject);

                            buttonAddProject.setPadding(0,0,0,0);

                            textView22.setVisibility(View.GONE);
                            imageView6.setVisibility(View.GONE);

                            if (fileUri != null) {
                                buttonAddNewProject.setTextColor(Color.parseColor("#0886F6"));
                            }
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
        progressBar = view.findViewById(R.id.progressBar10);
        scrollViewNewProject = view.findViewById(R.id.scrollViewNewProject);
        newProjectDueDateCardView = view.findViewById(R.id.newProjectDueDateCardView);
        newProjectDueDateView = view.findViewById(R.id.newProjectDueDateView);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        scrollViewNewProject.setNestedScrollingEnabled(true);

        getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                view.getWindowVisibleDisplayFrame(rect);

                keyboardHeight = view.getRootView().getHeight() - rect.height();

                if (keyboardHeight > 200) {
                    isVisible = true;
                } else {
                    isVisible = false;
                    oldHeight = keyboardHeight;
                }
            }
        });

        newProjectShareWithEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    scrollViewNewProject.smoothScrollTo(0,newProjectShareWithEditText.getBottom());
                    scrollViewNewProject.post(()-> {
                        scrollViewNewProject.smoothScrollTo(0, newProjectShareWithEditText.getBottom() + keyboardHeight + 12);
                    });
                }
                else {
                    hideKeyboard(getActivity(), v);
                }
            }
        });

        newProjectDueDateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateTimeSet = String.format("%02d-%02d-%d 00:00:00", year, month + 1, dayOfMonth);
                                newProjectDueDateView.setText(String.format("%02d-%02d-%d 12:00 AM", year, month + 1, dayOfMonth));
                                // Now show TimePickerDialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Handle selected date and time
                                                dateTimeSet = String.format("%02d-%02d-%d %02d:%02d:00",
                                                        year, month + 1, dayOfMonth, hourOfDay, minute);

                                                newProjectDueDateView.setText(String.format("%02d-%02d-%d %2d:%02d",
                                                        year, month + 1, dayOfMonth, hourOfDay % 12, minute) + (hourOfDay / 12 == 0 ? " AM" : " PM"));
                                            }
                                        }, hour, minute, true);
                                timePickerDialog.show();
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        newProjectShareWithEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CustomInputValidation.validateEmail(newProjectShareWithEditText.getText().toString().trim())) { // fileUri != null ||
                    email = newProjectShareWithEditText.getText().toString().trim();
                    buttonAddNewProject.setTextColor(Color.parseColor("#0886F6"));
                }
            }
        });

//        scrollViewNewProject.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(v);
//                bottomSheetBehavior.setDraggable(scrollY == 0);
//            }
//        });


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
                    ToastMessage.getInstance(getContext()).showShortMessage("Invalid share email", "frown");
                }
            }
        });
    }

    private int onKeyboardVisibilityChanged() {
        if (isVisible) {
            System.out.println("Keyboard Height: " + keyboardHeight);
            return keyboardHeight;
        } else {
            System.out.println("Keyboard is hidden");
            return oldHeight;
        }

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
            progressBar.setVisibility(View.VISIBLE);
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                DBConn.getURL("add_project.php"),
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

                            // Toast.makeText(getContext(), , Toast.LENGTH_SHORT).show();

                            if (obj.getString("code").equals("Project added successfully.") || obj.getString("code").equals("Project added and shared successfully.")) {
                                ToastMessage.getInstance(getContext()).showLongMessage(obj.getString("code"), "smile");
//                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
//                                .setSmallIcon(R.drawable.logo)
//                                .setContentTitle("My notification")
//                                .setContentText("Hello World!")
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                .setAutoCancel(true);



//                                Intent intent = new Intent(getContext(), MainActivity.class);
//                                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                mBuilder.setContentIntent(pendingIntent);
//
//                                NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                                if (notificationManager != null) {
//                                    notificationManager.notify(34234, mBuilder.build());
//                                }

                                addNotification();

                                dismiss();
                            }
                            else {
                                ToastMessage.getInstance(getContext()).showLongMessage(obj.getString("code"), "yellow");
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
                            ToastMessage.getInstance(getContext()).showLongMessage(error.getMessage(), "frown");
                            Log.e("Got Error", "" + error.getMessage());
                        }
                        else {
                            ToastMessage.getInstance(getContext()).showLongMessage("An error occurred while creating your project. Please try again.", "frown");
                            // Toast.makeText(getContext(), "An error occurred while creating your project. Please try again.", Toast.LENGTH_LONG).show();
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

                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

                    params.put("project_owner_id", sharedPref.getString("user_id", null));
                    params.put("project_title", newProjectInputTitle.getText().toString().trim());
                    params.put("project_description", newProjectInputDescription.getText().toString());
                    params.put("due_date", (dateTimeSet == null || dateTimeSet.equals("0000-00-00 00:00:00") ? null : dateTimeSet));
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
            params.put("due_date", (dateTimeSet == null || dateTimeSet.equals("0000-00-00 00:00:00") ? "null" : dateTimeSet));
            params.put("priority", (switch1.isChecked() ? "true" : "false"));
            params.put("share_with", email);

            progressBar.setVisibility(View.VISIBLE);
            DBConn.postRequest(
                DBConn.getURL("add_project.php"),
                getContext(),
                params,
                new DBConn.ResponseCallback() {
                    @Override
                    public void innerResponse(Object object) {
                        progressBar.setVisibility(View.GONE);
                        // Toast.makeText(getContext(), object.toString(), Toast.LENGTH_LONG).show();

                        if (object.toString().equals("Project added successfully.") || object.toString().equals("Project added and shared successfully.")) {
                            ToastMessage.getInstance(getContext()).showLongMessage(object.toString(), "smile");
                            addNotification();
                            dismiss();
                        }
                        else {
                            ToastMessage.getInstance(getContext()).showLongMessage(object.toString(), "yellow");
                            dismiss();
                        }

                    }
                },
                "Unable to connect to the database",
                "Unable to parse API response"
            );
        }
    }

    private void addNotification() {
        if (dateTimeSet != null) {

            SharedPreferences sharedPref = AssetExchangeApp.context.getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

            if (!sharedPref.getBoolean("allow_notifications", false)) {
                int permissionState = 0;

                SharedPreferences.Editor editor = sharedPref.edit();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    permissionState = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS);
                    // If the permission is not granted, request it.
                    if (permissionState == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            editor.putBoolean("allow_notifications", true);
                            editor.apply();
                        }
                    }
                }
            }

            LocalDateTime dateTime = LocalDateTime.parse(dateTimeSet, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));

            long triggerTimeNow = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long triggerTimeTomorrow = triggerTimeNow + 24 * 60 * 60 * 1000;;

            if (triggerTimeNow >= System.currentTimeMillis()) {
                // Schedule the notification
                Intent intentNow = new Intent(getContext(), NotificationReceiver.class);
                intentNow.putExtra("title", "Project Due Reminder");
                intentNow.putExtra("message", "Your project " + newProjectInputTitle.getText().toString().trim() + " is due now");

                Intent intentTomorrow = new Intent(getContext(), NotificationReceiver.class);
                intentTomorrow.putExtra("title", "Project Due Reminder");
                intentTomorrow.putExtra("message", "Your project " + newProjectInputTitle.getText().toString().trim() + " is due tomorrow");

                PendingIntent pendingIntentNow = PendingIntent.getBroadcast(AssetExchangeApp.context, 0, intentNow, PendingIntent.FLAG_IMMUTABLE);

                PendingIntent pendingIntentTomorrow = PendingIntent.getBroadcast(AssetExchangeApp.context, 0, intentTomorrow, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) AssetExchangeApp.context.getSystemService(Context.ALARM_SERVICE);

                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeNow, pendingIntentNow);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeTomorrow, pendingIntentTomorrow);
                }
            }


        }
    }
}

