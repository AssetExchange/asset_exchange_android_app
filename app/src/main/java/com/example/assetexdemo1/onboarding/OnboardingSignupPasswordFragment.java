package com.example.assetexdemo1.onboarding;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.assetexdemo1.CustomInputValidation;
import com.example.assetexdemo1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingSignupPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingSignupPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText fOSPpasswordEditText;
    AppCompatButton fOSPcontinueButton;
    ImageButton fOSPbackButton;
    Button fOSPpasswordShowButton;
    String password = "";
    boolean passwordShow = false;

    public OnboardingSignupPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingSignupPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingSignupPasswordFragment newInstance(String param1, String param2) {
        OnboardingSignupPasswordFragment fragment = new OnboardingSignupPasswordFragment();
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
        return inflater.inflate(R.layout.fragment_onboarding_signup_password, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.getView() != null) {

            fOSPpasswordEditText = getView().findViewById(R.id.fOSPpasswordEditText);
            fOSPcontinueButton = getView().findViewById(R.id.fOSPcontinueButton);
            fOSPbackButton = getView().findViewById(R.id.fOSPbackButton);
            fOSPpasswordShowButton = getView().findViewById(R.id.fOSPpasswordShowButton);

            ImageView imageView3 = getView().findViewById(R.id.imageView3);
            ImageView imageView4 = getView().findViewById(R.id.imageView4);
            ImageView imageView5 = getView().findViewById(R.id.imageView5);
            ImageView imageView6 = getView().findViewById(R.id.imageView6);
            ImageView imageView7 = getView().findViewById(R.id.imageView7);

            TextView textView = getView().findViewById(R.id.textView);
            TextView textView2 = getView().findViewById(R.id.textView2);
            TextView textView3 = getView().findViewById(R.id.textView3);
            TextView textView4 = getView().findViewById(R.id.textView4);
            TextView textView5 = getView().findViewById(R.id.textView5);

            NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.activity_onboarding_nav));

            fOSPpasswordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    System.out.println( getArguments().getString("full_name") + " " +  getArguments().getString("email"));
                    if (CustomInputValidation.validatePassword(s.toString(), getArguments().getString("full_name"), getArguments().getString("email"))) {
                        fOSPcontinueButton.setClickable(true);
                        fOSPcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundEnabled, null)));
//                        textView.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintGray, null));
                        textView.setText("Password strength: strong");
                    }
                    else {
                        fOSPcontinueButton.setClickable(false);
                        fOSPcontinueButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorOnboardingButtonBackgroundDisabled, null)));
//                        textView.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintRed, null));
                        textView.setText("Password strength: weak");
                    }

                    boolean[] checklist = CustomInputValidation.validatePasswordWithCheck(s.toString(), getArguments().getString("full_name"), getArguments().getString("email"));

                    if (checklist[0] == true) {
                        imageView4.setImageResource(R.drawable.password_check);
                        textView2.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintGreen, null));
                    }
                    else {
                        imageView4.setImageResource(R.drawable.password_cross);
                        textView2.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintRed, null));
                    }
                    if (checklist[1] == true) {
                        imageView5.setImageResource(R.drawable.password_check);
                        textView3.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintGreen, null));
                    }
                    else {
                        imageView5.setImageResource(R.drawable.password_cross);
                        textView3.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintRed, null));
                    }
                    if (checklist[2] == true) {
                        imageView6.setImageResource(R.drawable.password_check);
                        textView4.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintGreen, null));
                    }
                    else {
                        imageView6.setImageResource(R.drawable.password_cross);
                        textView4.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintRed, null));
                    }
                    if (checklist[3] == true) {
                        imageView7.setImageResource(R.drawable.password_check);
                        textView5.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintGreen, null));
                    }
                    else {
                        imageView7.setImageResource(R.drawable.password_cross);
                        textView5.setTextColor(getResources().getColor(R.color.colorOnboardingPasswordHintRed, null));
                    }

                    password = s.toString();
                }
            });

            fOSPpasswordShowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int startSelection, endSelection;
                    if (!passwordShow) {
                        passwordShow = true;

                        startSelection = fOSPpasswordEditText.getSelectionStart();
                        endSelection = fOSPpasswordEditText.getSelectionEnd();

                        fOSPpasswordEditText.setTransformationMethod(null);

                        fOSPpasswordEditText.setSelection(startSelection, endSelection);
                    }
                    else {
                        passwordShow = false;

                        startSelection = fOSPpasswordEditText.getSelectionStart();
                        endSelection = fOSPpasswordEditText.getSelectionEnd();

                        fOSPpasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
                        fOSPpasswordEditText.setSelection(startSelection, endSelection);
                    }
                }
            });

            fOSPcontinueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent(getContext(), MainActivity.class); // MainActivity.class
                    // startActivity(intent);

                    System.out.println(password);

                    if (CustomInputValidation.validatePassword(password, getArguments().getString("full_name"), getArguments().getString("email"))) {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", getArguments().getString("email"));
                        bundle.putString("full_name", getArguments().getString("full_name"));
                        bundle.putString("password", password);

                        navController.navigate(R.id.action_onboardingSignupPasswordFragment_to_onboardingPickRoleFragment, bundle);
                    }


                }
            });

            fOSPbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.popBackStack();
                }
            });
        }
    }
}