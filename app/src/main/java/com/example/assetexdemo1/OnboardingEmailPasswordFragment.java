package com.example.assetexdemo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import android.view.inputmethod.InputMethodManager;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingEmailPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingEmailPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView fOEPprivacyTV;
    EditText fOEPemailEditText;
    EditText fOEPpasswordEditText;
    AppCompatButton fOEPcontinueButton;

    String email = "";
    String password = "";

    public OnboardingEmailPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingEmailPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingEmailPasswordFragment newInstance(String param1, String param2) {
        OnboardingEmailPasswordFragment fragment = new OnboardingEmailPasswordFragment();
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
        return inflater.inflate(R.layout.fragment_onboarding_email_password, container, false);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        Button fOEPcontinueButton = this.getView().findViewById(R.id.fOEPcontinueButton);
//
//        fOEPcontinueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MainActivity.class); // MainActivity.class
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {
//            Button continueButton = this.getView().findViewById(R.id.fOEPcontinueButton);
            fOEPemailEditText = getView().findViewById(R.id.fOEPemailEditText);
            fOEPpasswordEditText = getView().findViewById(R.id.fOEPpasswordEditText);
            fOEPcontinueButton = getView().findViewById(R.id.fOEPcontinueButton);
            fOEPprivacyTV= getView().findViewById(R.id.fOEPprivacyTV);

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            email = getArguments().getString("email");
            fOEPemailEditText.setText(email, TextView.BufferType.EDITABLE);

            fOEPemailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    System.out.println(s.toString().trim() + CustomInputValidation.validateEmail(s.toString().trim()) + " " + password + CustomInputValidation.validatePassword(password, null,  s.toString().trim()));
                    if (CustomInputValidation.validateEmail(s.toString().trim()) && CustomInputValidation.validatePassword(password, null,  s.toString().trim())) {
                        fOEPcontinueButton.setClickable(true);
                        fOEPcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundEnabled, null)));
                    }
                    else {
                        fOEPcontinueButton.setClickable(false);
                        fOEPcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundDisabled, null)));
                    }
                    email = s.toString().trim();
                }
            });

            fOEPpasswordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    System.out.println(email + CustomInputValidation.validateEmail(email) + " " + s.toString().trim() + CustomInputValidation.validatePassword(s.toString(), null, email));
                    if (CustomInputValidation.validateEmail(email) && CustomInputValidation.validatePassword(s.toString(), null, email)) {
                        fOEPcontinueButton.setClickable(true);
                        fOEPcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundEnabled, null)));
                    }
                    else {
                        fOEPcontinueButton.setClickable(false);
                        fOEPcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundDisabled, null)));
                    }
                    password = s.toString();
                }
            });
            fOEPcontinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    navController.navigate(R.id.action_onboardingEmailPasswordFragment_to_onboardingFullNameFragment);
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if (CustomInputValidation.validateEmail(email) && CustomInputValidation.validatePassword(password, null, email)) {

                        Map<String,String> params = new HashMap<String, String>();
                        params.put("action", "login");
                        params.put("email", getArguments().getString("email"));
                        params.put("password", password);

                        Bundle bundle = new Bundle();
                        bundle.putString("email", getArguments().getString("email"));

                        DBConn.postRequest(
                                DBConn.getURL("login_signup.php"),
                                getContext(),
                                params,
                                new DBConn.ResponseCallback() {
                                    @Override
                                    public void innerResponse(Object object) {
//                                        String message = (String) object;
//                                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//                                        if (message.equals("Login successful")) {
//                                            Intent intent = new Intent(getActivity(), MainActivity.class); // MainActivity.class
//                                            startActivity(intent);
//                                        }

                                        if (object instanceof JSONObject) {
                                            JSONObject json = (JSONObject) object;
                                            try {
                                                if (json.getString("code").equals("Login successful")) {
                                                    Toast.makeText(getContext(), json.getString("code"), Toast.LENGTH_LONG).show();

                                                    SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

                                                    prefs.edit().putBoolean("logged_in", true).commit();
                                                    prefs.edit().putString("user_id", json.getString("user_id")).commit();
                                                    prefs.edit().putString("session_id", json.getString("session_id")).commit();
                                                    prefs.edit().putString("email", json.getString("email")).commit();
                                                    prefs.edit().putString("full_name", json.getString("full_name")).commit();


                                                    bundle.putString("session_id", json.getString("session_id"));

                                                    Intent intent = new Intent(getActivity(), MainActivity.class); // MainActivity.class
                                                    startActivity(intent, bundle);
                                                }

                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            }
                                    }
                                },
                                "Unable to connect to the database",
                                "Unable to parse API response");
//                        Intent intent = new Intent(getActivity(), MainActivity.class); // MainActivity.class
//                        startActivity(intent);
                    }
                }
            });

            fOEPprivacyTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_onboardingEmailPasswordFragment_to_onboardingTermsFragment);
                }
            });
        }
    }
}