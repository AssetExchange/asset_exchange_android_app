package com.example.assetexdemo1;

import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingEmailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView fOEprivacyTV;
    EditText fOEemailEditText;
    AppCompatButton fOEcontinueButton;
    String email = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnboardingEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingEmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingEmailFragment newInstance(String param1, String param2) {
        OnboardingEmailFragment fragment = new OnboardingEmailFragment();
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

        return inflater.inflate(R.layout.fragment_onboarding_email, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {
//            Button continueButton = this.getView().findViewById(R.id.fOEcontinueButton);

            fOEemailEditText = getView().findViewById(R.id.fOEemailEditText);
            fOEcontinueButton = getView().findViewById(R.id.fOEcontinueButton);
            fOEprivacyTV = getView().findViewById(R.id.fOEprivacyTV);

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            fOEemailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    System.out.println(s.toString() + CustomInputValidation.validateEmail(s.toString().trim()));
                    if (CustomInputValidation.validateEmail(s.toString().trim())) {
                        fOEcontinueButton.setClickable(true);
                        fOEcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundEnabled, null)));
                        email = s.toString().trim();
                    }
                    else {
                        fOEcontinueButton.setClickable(false);
                        fOEcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundDisabled, null)));
                    }
                }
            });
            fOEcontinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(getContext(), MainActivity.class); // MainActivity.class
                    // startActivity(intent);

//                    navController.navigate(R.id.action_onboardingEmailFragment_to_onboardingEmailPasswordFragment);

                    if (CustomInputValidation.validateEmail(email)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);

                        DBConn.getRequest(
                                DBConn.getRecordURL("users?filter=email,eq," + email + "&include=user_id,email"),
                                getContext(),
                                new DBConn.ResponseCallback() {
                                    @Override
                                    public void innerResponse(Object object) {
                                        JSONArray jsonArray = (JSONArray) object;
                                        // If one user with such email exists
                                        if (!jsonArray.isNull(0) && jsonArray.length() == 1) {
                                            navController.navigate(R.id.action_onboardingEmailFragment_to_onboardingEmailPasswordFragment, bundle);
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Creating new account...", Toast.LENGTH_SHORT).show();
                                            navController.navigate(R.id.action_onboardingEmailFragment_to_onboardingFullNameFragment, bundle);
                                        }
                                    }
                                },
                                "Unable to connect to the database",
                                "Unable to parse API response"
                        );
                    }
                }
            });

            fOEprivacyTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_onboardingEmailFragment_to_onboardingTermsFragment);
                }
            });
        }
    }
}