package com.example.assetexdemo1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class OnboardingActivity extends AppCompatActivity {

    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_onboarding), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        OnboardingEmailFragment onboardingEmailFragment = new OnboardingEmailFragment();
//
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.activity_onboarding, OnboardingEmailFragment.class, null); // onboardingEmailFragment
//
//        fragmentTransaction.commit();

//        navController.navigate(R.id.action_onboardingEmailFragment_to_onboardingEmailPasswordFragment);
    }
}