package com.example.assetexdemo1.onboarding;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.assetexdemo1.DBConn;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.ToastMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingPickRoleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingPickRoleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton fOPRbackButton;
    AppCompatButton[] fOPRroleButtons = new AppCompatButton[10];
    AppCompatButton fOPRcontinueButton;

    int roleNum = 5; // Role ID for UI/UX Designer in the roles table

    public OnboardingPickRoleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingPickRoleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingPickRoleFragment newInstance(String param1, String param2) {
        OnboardingPickRoleFragment fragment = new OnboardingPickRoleFragment();
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
        return inflater.inflate(R.layout.fragment_onboarding_pick_role, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {
            fOPRbackButton = getView().findViewById(R.id.fOPRbackButton);
            fOPRcontinueButton = getView().findViewById(R.id.fOPRcontinueButton);

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            int[] roleButtonIDs = {R.id.fOPRroleButton1, R.id.fOPRroleButton2, R.id.fOPRroleButton3, R.id.fOPRroleButton4, R.id.fOPRroleButton5, R.id.fOPRroleButton6, R.id.fOPRroleButton7, R.id.fOPRroleButton8, R.id.fOPRroleButton9, R.id.fOPRroleButton0};
            int[] roleIDs = {5, 6, 7, 8, 9, 10, 11, 4, 12, 2};

            for (int i = 0; i < roleButtonIDs.length; i++) {
                Button currentButton = getView().findViewById(roleButtonIDs[i]);
                currentButton.setFocusable(true);

                int innerI = i;

                currentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentButton.requestFocus();
                    }
                });

                currentButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            roleNum = roleIDs[innerI];
                        }
                    }
                });
            }

            fOPRcontinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("email", getArguments().getString("email"));
                    bundle.putString("full_name", getArguments().getString("full_name"));
                    bundle.putString("password", getArguments().getString("password"));
                    bundle.putString("role_id", String.valueOf(roleNum));

                    Map<String, String> params = new HashMap<>();
                    params.put("action", "signup");
                    params.put("email", getArguments().getString("email"));
                    params.put("full_name", getArguments().getString("full_name"));
                    params.put("password", getArguments().getString("password"));
                    params.put("role_id", String.valueOf(roleNum));

                    DBConn.postRequest(
                            DBConn.getURL("login_signup.php"),
                            getContext(),
                            params,
                            new DBConn.ResponseCallback() {
                                @Override
                                public void innerResponse(Object object) {
                                    String message = (String) object;
                                    ToastMessage.getInstance(getContext()).showLongMessage(message, "yellow");
                                    // Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    navController.navigate(R.id.action_onboardingPickRoleFragment_to_onboardingVerifyEmailFragment, bundle);
                                }
                            },
                            "Unable to connect to the database",
                            "Unable to parse API response"
                    );
                }
            });

            fOPRbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.popBackStack();
                }
            });
        }
    }
}