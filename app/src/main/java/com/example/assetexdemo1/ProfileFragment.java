package com.example.assetexdemo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean notificationToggle = false;
    private Switch profileNotificationSwitch;
    private Button profileLogoutButton;
    LinearLayout profileUserProfileContainer;
    LinearLayout profileFragmentShareProfile;
    ImageView profileFragmentProfilePicture;
    ProgressBar progressBar;
    TextView profileFragmentFullName, profileFragmentEmailAddress;

    UserModel userModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.getView() != null) {
            profileFragmentProfilePicture = getView().findViewById(R.id.profileFragmentProfilePicture);
            profileFragmentFullName = getView().findViewById(R.id.profileFragmentFullName);
            profileFragmentEmailAddress = getView().findViewById(R.id.profileFragmentEmailAddress);
            profileUserProfileContainer = getView().findViewById(R.id.profileFragmentProfileContainer);
            profileFragmentShareProfile = getView().findViewById(R.id.profileFragmentShareProfile);

            SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            profileNotificationSwitch = getView().findViewById(R.id.profileNotificationSwitch);
            profileLogoutButton = getView().findViewById(R.id.profileLogoutButton);

            profileFragmentFullName.setText(sharedPref.getString("full_name", "Name"));
            profileFragmentEmailAddress.setText(sharedPref.getString("email", "Email"));

            progressBar = getView().findViewById(R.id.progressBar4);
            progressBar.setVisibility(View.GONE);

            progressBar.setVisibility(View.VISIBLE);
            DBConn.getRequest(
                DBConn.getRecordURL("users/" + sharedPref.getString("user_id", "1")),
                getContext(),
                new DBConn.ResponseCallback() {
                    @Override
                    public void innerResponse(Object object) {
                        if (object instanceof JSONObject) {
                            try {
                                JSONObject jsonObject = (JSONObject) object;

                                if (!jsonObject.has("code")) {
                                    progressBar.setVisibility(View.GONE);

                                    userModel = new UserModel(
                                        jsonObject.getInt("user_id"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("full_name"),
                                        jsonObject.getInt("role_id"),
                                        jsonObject.isNull("profile_pic_path") ? null : jsonObject.getString("profile_pic_path")
                                    );
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    ToastMessage.getInstance(getContext()).showShortMessage("Cannot get user profile", "frown");
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                "Unable to connect to the database",
                "Unable to parse API response"
            );

            profileUserProfileContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);

                    if (userModel != null) {
                        Intent intent = new Intent(getActivity(), ProfileViewerActivity.class);
                        intent.putExtra("user_model", userModel);
                        startActivity(intent);
                    }
                    else {
                        ToastMessage.getInstance(getContext()).showShortMessage("Cannot get user profile", "frown");
                    }
                }
            });

            profileFragmentShareProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userModel != null) {
                        Intent intent = new Intent(getActivity(), ProfileSharingOptionsActivity.class);
                        intent.putExtra("user_model", userModel);
                        startActivity(intent);
                    }
                    else {
                        ToastMessage.getInstance(getContext()).showShortMessage("Cannot get user profile", "frown");
                    }
                }
            });

            profileNotificationSwitch.setChecked(sharedPref.getBoolean("allow_notifications", false));

            profileNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int permissionState = 0;

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
                    else {
                        editor.putBoolean("allow_notifications", false);
                        editor.apply();
                    }
                }
            });

            profileLogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Clear user preferences
                                editor.clear().commit();

                                Intent intent = new Intent(getActivity(), OnboardingActivity.class);
                                startActivity(intent);

                                ToastMessage.getInstance(getActivity().getApplicationContext()).showLongMessage("You have successfully logged out", "smile");

                                // Toast.makeText(getActivity().getApplicationContext(), "You have successfully logged out", Toast.LENGTH_SHORT).show();

                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                    );
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

        }
    }
}