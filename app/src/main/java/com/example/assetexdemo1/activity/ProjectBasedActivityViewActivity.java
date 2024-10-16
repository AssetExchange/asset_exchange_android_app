package com.example.assetexdemo1.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.assetexdemo1.R;
import com.example.assetexdemo1.adapter.ProjectBasedActivityViewAdapter;
import com.example.assetexdemo1.model.ProjectModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProjectBasedActivityViewActivity extends AppCompatActivity {
    TabLayout projectBasedActivityViewTabLayout;
    ImageButton projectBasedActivityViewBackButton;
    ViewPager2 projectBasedActivityViewViewPager;

    ProjectModel projectModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_project_based_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        projectBasedActivityViewTabLayout = findViewById(R.id.projectBasedActivityViewTabLayout);
        projectBasedActivityViewBackButton = findViewById(R.id.projectBasedActivityViewBackButton);
        projectBasedActivityViewViewPager = findViewById(R.id.projectBasedActivityViewViewPager);

        if (getIntent() != null && getIntent().getExtras() != null) {
            projectBasedActivityViewBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            projectModel = getIntent().getParcelableExtra("project_model");

            projectBasedActivityViewViewPager.setAdapter(new ProjectBasedActivityViewAdapter(this, projectModel));

            new TabLayoutMediator(projectBasedActivityViewTabLayout, projectBasedActivityViewViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(TabLayout.Tab tab, int position) {
                    if (position == 0){
                        tab.setText("Has access");
                    }
                    else {
                        tab.setText("Revisions");
                    }
                }
            }).attach();

            projectBasedActivityViewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.view.setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }


    }
}