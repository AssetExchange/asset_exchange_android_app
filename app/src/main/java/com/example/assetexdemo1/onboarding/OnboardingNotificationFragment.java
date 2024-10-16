package com.example.assetexdemo1.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.assetexdemo1.R;
import com.example.assetexdemo1.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingNotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AppCompatButton fONcontinueButton;
    Button fONskipButton;

    public OnboardingNotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingNotificationFragment newInstance(String param1, String param2) {
        OnboardingNotificationFragment fragment = new OnboardingNotificationFragment();
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
        return inflater.inflate(R.layout.fragment_onboarding_notification, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            fONcontinueButton = getView().findViewById(R.id.fONcontinueButton);
            fONskipButton = getView().findViewById(R.id.fONskipButton);

            fONcontinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int permissionState = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        permissionState = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS);
                        // If the permission is not granted, request it.
                        if (permissionState == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("allow_notifications", true);
                                editor.apply();
                            }
                        }
                    }

                    Intent intent = new Intent(getContext(), MainActivity.class); // MainActivity.class
                    startActivity(intent);
                }
            });

            fONskipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MainActivity.class); // MainActivity.class
                    startActivity(intent);
                }
            });
        }
    }
}