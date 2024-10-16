package com.example.assetexdemo1.onboarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.assetexdemo1.DBConn;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.ToastMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingVerifyEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingVerifyEmailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText fOVEpasswordEditText;
    TextView fOVEemailTextView;
    AppCompatButton fOVEcontinueButton;
    String verificationCode = "";

    public OnboardingVerifyEmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingVerifyEmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingVerifyEmailFragment newInstance(String param1, String param2) {
        OnboardingVerifyEmailFragment fragment = new OnboardingVerifyEmailFragment();
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
        return inflater.inflate(R.layout.fragment_onboarding_verify_email, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {
            fOVEcontinueButton = getView().findViewById(R.id.fOVEcontinueButton);
            fOVEpasswordEditText = getView().findViewById(R.id.fOVEpasswordEditText);
            fOVEemailTextView = getView().findViewById(R.id.fOVEemailTextView);

            fOVEemailTextView.setText(getArguments().getString("email"));

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            fOVEcontinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verificationCode = fOVEpasswordEditText.getText().toString().trim();

                    Bundle bundle = new Bundle();
                    bundle.putString("email", getArguments().getString("email"));
                    bundle.putString("full_name", getArguments().getString("full_name"));
                    bundle.putString("password", getArguments().getString("password"));
                    bundle.putString("role_id", getArguments().getString("role_id"));

                    if (verificationCode.length() == 6) {
                        System.out.println(verificationCode);

                        Map<String, String> params = new HashMap<>();
                        params.put("action", "send_verification_code");
                        params.put("email", getArguments().getString("email"));
                        params.put("password", getArguments().getString("password"));
                        params.put("email_verification_code", verificationCode);

                        DBConn.postRequest(
                                DBConn.getURL("login_signup.php"),
                                getContext(),
                                params,
                                new DBConn.ResponseCallback() {
                                    @Override
                                    public void innerResponse(Object object) {
//                                        String message = (String) object;
//                                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//                                        if (message.equals("Email is now verified")) {
//                                            SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);
//                                            prefs.edit().putBoolean("logged_in", true).commit();
//                                            navController.navigate(R.id.action_onboardingVerifyEmailFragment_to_onboardingNotificationFragment, bundle);
//                                        }
                                        if (object instanceof JSONObject) {
                                            JSONObject json = (JSONObject) object;
                                            try {
                                                if (json.getString("code").equals("Email is now verified")) {
                                                    // Toast.makeText(getContext(), json.getString("code"), Toast.LENGTH_LONG).show();
                                                    ToastMessage.getInstance(getContext()).showLongMessage(json.getString("code"), "smile");

                                                    SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

                                                    prefs.edit().putBoolean("logged_in", true).commit();
                                                    prefs.edit().putString("user_id", json.getString("user_id")).commit();
                                                    prefs.edit().putString("session_id", json.getString("session_id")).commit();
                                                    prefs.edit().putString("email", json.getString("email")).commit();
                                                    prefs.edit().putString("full_name", json.getString("full_name")).commit();
                                                    prefs.edit().putString("role_id", json.getString("role_id")).commit();


                                                    bundle.putString("session_id", json.getString("session_id"));

                                                    navController.navigate(R.id.action_onboardingVerifyEmailFragment_to_onboardingNotificationFragment, bundle);
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


                    }
                    else {
                        ToastMessage.getInstance(getContext()).showLongMessage("Invalid number of digits in code", "frown");
                        // Toast.makeText(getContext(), "Invalid number of digits in code", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}