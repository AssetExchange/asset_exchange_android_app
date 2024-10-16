package com.example.assetexdemo1.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.assetexdemo1.AssetExchangeApp;
import com.example.assetexdemo1.fragment.BlankFragment1;
import com.example.assetexdemo1.fragment.ProfileFragment;
import com.example.assetexdemo1.fragment.ProjectOverviewFragment;
import com.example.assetexdemo1.R;
import com.example.assetexdemo1.fragment.TaskActivity1;
import com.example.assetexdemo1.fragment.UploadAssetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    BlankFragment1 blankFragment1 = new BlankFragment1();
    ProfileFragment profileFragment = new ProfileFragment();
    TaskActivity1 taskActivity1 = new TaskActivity1();
    ProjectOverviewFragment projectOverviewFragment = new ProjectOverviewFragment();
    UploadAssetFragment uploadAssetFragment = new UploadAssetFragment();

    ArrayList<Drawable> icons = new ArrayList<>();

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

        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            icons.add(menuItem.getIcon());
        }

        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_menu_command);

        SharedPreferences sharedPref = AssetExchangeApp.context.getSharedPreferences(getResources().getString(R.string.pref_key_file), Context.MODE_PRIVATE);

        if (sharedPref.getString("role_id", "4").equals("2")) {
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_menu_send).setVisible(false);
        }

        bottomNavigationView.setItemIconTintList(null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);

            if (menuItem.getItemId() == item.getItemId()) {
                // Set selected item icon to original color
                menuItem.setIconTintList(null);
            }
            else {
                menuItem.setIconTintList(ColorStateList.valueOf(Color.parseColor("#969696"))); // Grey color
            }
        }

//        bottomNavigationView.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);

        // item.setIconTintList(null);

        if (item.getItemId() == R.id.bottom_nav_menu_command) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) taskActivity1).commit();
            return true;
        }
        if (item.getItemId() == R.id.bottom_nav_menu_folder) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) projectOverviewFragment).commit();
            return true;
        }
        if (item.getItemId() == R.id.bottom_nav_menu_send) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) uploadAssetFragment).commit();
            return true;
        }
        if (item.getItemId() == R.id.bottom_nav_menu_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, (Fragment) profileFragment).commit();
            return true;
        }
        return false;
    }
}