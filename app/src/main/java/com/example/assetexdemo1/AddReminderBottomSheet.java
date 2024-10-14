package com.example.assetexdemo1;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.assetexdemo1.databinding.AddReminderBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddReminderBottomSheet extends BottomSheetDialogFragment {
    Button buttonBackNewReminder, buttonAddNewReminder;
    EditText newReminderInputTitle, newReminderInputDescription;
    CardView newReminderDueDateCardView;
    Switch switch1;
    TextView newReminderDueDateView;

    String dateTimeSet;

    BottomSheetListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_reminder_bottom_sheet, container, false);
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

        buttonBackNewReminder = view.findViewById(R.id.buttonBackNewReminder);
        buttonAddNewReminder = view.findViewById(R.id.buttonAddNewReminder);
        newReminderInputTitle = view.findViewById(R.id.newReminderInputTitle);
        newReminderInputDescription = view.findViewById(R.id.newReminderInputDescription);
        newReminderDueDateCardView = view.findViewById(R.id.newReminderDueDateCardView);
        switch1 = view.findViewById(R.id.switch1);
        newReminderDueDateView = view.findViewById(R.id.newReminderDueDateView);

        buttonBackNewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        newReminderDueDateCardView.setOnClickListener(new View.OnClickListener() {
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
                                newReminderDueDateView.setText(String.format("%02d-%02d-%d 12:00 AM", year, month + 1, dayOfMonth));
                                // Now show TimePickerDialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Handle selected date and time
                                                dateTimeSet = String.format("%02d-%02d-%d %02d:%02d:00",
                                                        year, month + 1, dayOfMonth, hourOfDay, minute);

                                                newReminderDueDateView.setText(String.format("%02d-%02d-%d %2d:%02d",
                                                        year, month + 1, dayOfMonth, hourOfDay % 12, minute) + (hourOfDay / 12 == 0 ? " AM" : " PM"));
                                            }
                                        }, hour, minute, true);
                                timePickerDialog.show();
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        newReminderInputTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    buttonAddNewReminder.setTextColor(Color.parseColor("#0886F6"));
                }
                else {
                    buttonAddNewReminder.setTextColor(Color.parseColor("#6A6A6A"));
                }
            }
        });

        buttonAddNewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newReminderInputTitle.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Missing reminder title", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

                    HashMap<String, String> params = new HashMap<>();
                    params.put("action", "add_reminder");
                    params.put("task_owner_id", sharedPref.getString("user_id", "1"));
                    params.put("task_title", newReminderInputTitle.getText().toString().trim());
                    params.put("task_description", newReminderInputTitle.getText().toString());
                    params.put("priority", (switch1.isChecked() ? "true" : "false"));

                    if (dateTimeSet != null) {
                        params.put("due_date", dateTimeSet);
                    }
                    DBConn.postRequest(
                        DBConn.getURL("add_reminder.php"),
                        getContext(),
                        params,
                        new DBConn.ResponseCallback() {
                            @Override
                            public void innerResponse(Object object) {
                                Toast.makeText(getContext(), object.toString(), Toast.LENGTH_LONG).show();

                                if (object.toString().equals("Reminder added successfully.")) {
                                    if (dateTimeSet != null) {
                                        System.out.println(dateTimeSet);

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
                                            intentNow.putExtra("title", "Task Due Reminder");
                                            intentNow.putExtra("message", "Your task is due now");

                                            Intent intentTomorrow = new Intent(getContext(), NotificationReceiver.class);
                                            intentTomorrow.putExtra("title", "Task Due Reminder");
                                            intentTomorrow.putExtra("message", "Your task is due tomorrow");

                                            PendingIntent pendingIntentNow = PendingIntent.getBroadcast(AssetExchangeApp.context, 0, intentNow, PendingIntent.FLAG_IMMUTABLE);

                                            PendingIntent pendingIntentTomorrow = PendingIntent.getBroadcast(AssetExchangeApp.context, 0, intentTomorrow, PendingIntent.FLAG_IMMUTABLE);

                                            AlarmManager alarmManager = (AlarmManager) AssetExchangeApp.context.getSystemService(Context.ALARM_SERVICE);

                                            if (alarmManager != null) {
                                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeNow, pendingIntentNow);
                                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeTomorrow, pendingIntentTomorrow);
                                            }
                                        }


                                    }
                                    updateData();
                                }
                                else {
                                    System.out.println(object.toString());
                                }
                            }
                        },
                        "Unable to connect to the database",
                        "Unable to parse API response"
                    );
                }
            }
        });
    }

    public interface BottomSheetListener {
        void onUpdateData(); // String data
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (getParentFragment() instanceof BottomSheetListener) {
            if (getParentFragment().getContext() != null) {
                super.onAttach(getParentFragment().getContext());
                listener = (BottomSheetListener) getParentFragment();
            }
        }
        else {
            super.onAttach(context);
            listener = (BottomSheetListener) context;
        }
    }

    private void updateData() {
        if (listener != null) {
            listener.onUpdateData();
        }
        dismiss();
    }
}
