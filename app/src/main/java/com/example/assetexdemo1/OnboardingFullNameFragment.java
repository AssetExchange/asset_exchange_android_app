package com.example.assetexdemo1;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingFullNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingFullNameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText fOFnameEditText;
    AppCompatButton fOFcontinueButton;
    ImageButton fOFbackButton;
    String email = "";
    String fullName = "";

    public OnboardingFullNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnBoardingFullNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingFullNameFragment newInstance(String param1, String param2) {
        OnboardingFullNameFragment fragment = new OnboardingFullNameFragment();
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
        return inflater.inflate(R.layout.fragment_onboarding_full_name, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {
            fOFnameEditText = getView().findViewById(R.id.fOFnameEditText);
            fOFcontinueButton = getView().findViewById(R.id.fOFcontinueButton);
            fOFbackButton = getView().findViewById(R.id.fOFbackButton);

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            fOFnameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().trim().isEmpty()) {
                        fOFcontinueButton.setClickable(true);
                        fOFcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundEnabled, null)));
                    }
                    else {
                        fOFcontinueButton.setClickable(false);
                        fOFcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundDisabled, null)));
                    }
                    fullName = s.toString().trim();
                }
            });

            fOFcontinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!fullName.trim().isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", getArguments().getString("email"));
                        bundle.putString("full_name", fullName);

                        navController.navigate(R.id.action_onboardingFullNameFragment_to_onboardingSignupPasswordFragment, bundle);
                    }
                }
            });

            fOFbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.popBackStack();
                }
            });
        }
    }
}