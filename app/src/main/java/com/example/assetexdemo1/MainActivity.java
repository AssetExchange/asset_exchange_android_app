package com.example.assetexdemo1;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    BlankFragment1 blankFragment1 = new BlankFragment1();
    ProfileFragment profileFragment = new ProfileFragment();
    TaskActivity1 taskActivity1 = new TaskActivity1();
    ProjectOverviewFragment projectOverviewFragment = new ProjectOverviewFragment();
    UploadAssetFragment uploadAssetFragment = new UploadAssetFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_menu_command);
        bottomNavigationView.setItemIconTintList(null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int[][] states = new int[][] {
            new int[] { android.R.attr.state_checked}, // state_checked
            new int[] { }
        };

        int[] colors = new int[] {
            Color.argb(255,0,0,0),
            Color.argb(255, 150,150,150)
        };

        ColorStateList myColorList = new ColorStateList(states, colors);
        bottomNavigationView.setItemIconTintList(myColorList);
        bottomNavigationView.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);

        if (item.getItemId() == R.id.bottom_nav_menu_command) {
            item.setIconTintList(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) taskActivity1).commit();
            return true;
        }
        if (item.getItemId() == R.id.bottom_nav_menu_folder) {
            item.setIconTintList(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) projectOverviewFragment).commit();
            return true;
        }
        if (item.getItemId() == R.id.bottom_nav_menu_send) {
            item.setIconTintList(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) uploadAssetFragment).commit();
            return true;
        }
        if (item.getItemId() == R.id.bottom_nav_menu_profile) {
            item.setIconTintList(null);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) profileFragment).commit();
            return true;
        }
        return false;
    }
}